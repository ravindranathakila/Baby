package ai.ilikeplaces.jpa;

import ai.ilikeplaces.doc.CONVENTION;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.exception.DBDishonourException;
import ai.ilikeplaces.exception.DBHazelcastRuntimeException;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.EntityManagerInjector;
import ai.ilikeplaces.util.Loggers;
import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.IMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @param <T>
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@CONVENTION(convention = "Caller assumes this class methods are of tx scope supports, " +
        "but create and updates here are of scope mandatory which will ensure caller does no mistake.")
@OK
public class CrudService<T> extends AbstractSLBCallbacks implements CrudServiceLocal<T> {


    /**
     * Lazily initialized
     */
    @WARNING("Lazily initialized upon entity lifecycle callbacks")
    private static HazelcastClient hazelcastClient;

    private HazelcastClient getHCClient() {
        if (hazelcastClient == null) {
            ClientConfig clientConfig = new ClientConfig();

            clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
            clientConfig.addAddress("127.0.0.1:5701");

            if (DEBUG_ENABLED) {
                Loggers.debug("THESE ARE THE HAZELCAST ADDRESSES FYR:");
                for (final InetSocketAddress inetSocketAddress : clientConfig.getAddressList()) {
                    Loggers.debug("Host " + inetSocketAddress.getHostName() + " on Port " + inetSocketAddress.getPort());
                }
            }

            hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);

            if (DEBUG_ENABLED) {
                Loggers.debug("THESE ARE THE HAZELCAST CLIENT(ME) DETAILS FYR:");
                Loggers.debug("clientConfig.getGroupConfig().getName():");
                Loggers.debug(clientConfig.getGroupConfig().getName());
                Loggers.debug("clientConfig.getGroupConfig().toString():");
                Loggers.debug(clientConfig.getGroupConfig().toString());
            }
        } else {
            if (!hazelcastClient.isActive()) {
                Loggers.info(Loggers.CODE_HC + "Hazelcast client is shutdown. Restarting...");
                hazelcastClient.restart();
                Loggers.info(Loggers.CODE_HC + "Hazelcast client start state after attempted restart:" + hazelcastClient.isActive());
            }
        }

        return hazelcastClient;
    }


    private Object getId(Object entity) throws IllegalAccessException, InvocationTargetException {
        Object key = null;

        final Method[] methods = entity.getClass().getMethods();

        for (final Method method : methods) {
            final Id annotation = method.getAnnotation(Id.class);
            if (annotation != null) {
                key = method.invoke(entity);
            }
        }

        if (DEBUG_ENABLED) {
            Loggers.debug(Loggers.CODE_HC + "Entity's @Id:" + key);
        }

        return key;
    }

    private IMap<Object, Object> getHCMap(final Object entity) {
        final String mapName = entity.getClass().getName();
        if (DEBUG_ENABLED) {
            Loggers.debug(Loggers.CODE_HC + "Attempting to fetch map named from:" + mapName);
        }
        return getHCClient().getMap(mapName);
    }

    /**
     * @param t
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    @Override
    public T create(final T t) {
        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
        entityManager.persist(t);
        //entityManager.refresh(t);
        return t;
    }

    @SuppressWarnings("unchecked")
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T find(final Class type, final Object id) {
        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
        return (T) entityManager.find(type, id);
    }

    /**
     * @param type
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @Deprecated//Hazelcast, thereby HBase cannot get updated this way unless a manual update goes through.
    public T getReference(final Class type, final Object id) {
        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
        return (T) entityManager.getReference(type, id);
    }

    /**
     * @param typeOfEntity
     * @param idByWhichToLookup
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T findBadly(Class typeOfEntity, Object idByWhichToLookup) {
        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
        final Object object = entityManager.find(typeOfEntity, idByWhichToLookup);
        if (object != null) {
            return (T) object;
        } else {
            throw DBDishonourException.QUERYING_AFFIRMATIVELY_A_NON_EXISTING_ENTITY;
        }
    }

    /**
     * @param type
     * @param id
     */
    @SuppressWarnings("unchecked")
    @Override
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void delete(final Class type, final Object id) {
        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
        entityManager.remove(entityManager.getReference(type, id));

        {
            try {
                Object key = getId(id);

                IMap<Object, Object> hcMap = getHCMap(id);

                hcMap.lock(key);
                hcMap.remove(key);
                hcMap.unlock(key);

            } catch (final Throwable t) {
                throw new DBHazelcastRuntimeException(Loggers.CODE_HC + "ERROR NEGOTIATING DATA VIA HAZELCAST", t);
            }
        }
    }

    /**
     * @param t
     * @return
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public T update(final T t) {
        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
        final T mmanaged = entityManager.merge(t);

        {
            try {

                Object key = getId(t);

                IMap<Object, Object> hcMap = getHCMap(t);

                hcMap.lock(key);
                hcMap.put(key, t);
                hcMap.unlock(key);

            } catch (final Throwable throwable) {
                throw new DBHazelcastRuntimeException(Loggers.CODE_HC + "ERROR NEGOTIATING DATA VIA HAZELCAST", throwable);

            }
        }

        return mmanaged;
    }

    /**
     * @param namedQueryName
     * @return
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List findWithNamedQuery(final String namedQueryName) {
        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
        return entityManager.createNamedQuery(namedQueryName).getResultList();
    }

    /**
     * @param namedQueryName
     * @param parameters
     * @return
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List findWithNamedQuery(final String namedQueryName, final Map parameters) {
        return findWithNamedQuery(namedQueryName, parameters, 0);
    }

    /**
     * @param queryName
     * @param resultLimit
     * @return
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List findWithNamedQuery(final String queryName, final int resultLimit) {
        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
        return entityManager.createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    /**
     * @param sql
     * @param type
     * @return
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List findByNativeQuery(final String sql, final Class type) {
        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
        return entityManager.createNativeQuery(sql, type).getResultList();
    }

    /**
     * @param namedQueryName
     * @param parameters
     * @param resultLimit
     * @return
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List findWithNamedQuery(final String namedQueryName, final Map parameters, final int resultLimit) {
        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();

        @SuppressWarnings("unchecked")
        final Set<Entry> rawParameters = parameters.entrySet();
        final Query query = entityManager.createNamedQuery(namedQueryName);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }
        for (final Entry entry : rawParameters) {
            query.setParameter((String) entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    /**
     * @return toString_
     */
    @Override
    public String toString() {
        String toString_ = getClass().getName();
        try {
            final Field[] fields = {getClass().getDeclaredField("entityManager")};

            for (final Field field : fields) {
                try {
                    toString_ += "\n{" + field.getName() + "," + field.get(this) + "}";
                } catch (IllegalArgumentException ex) {
                    INFO.info(null, ex);
                } catch (IllegalAccessException ex) {
                    INFO.info(null, ex);
                }
            }
        } catch (NoSuchFieldException ex) {
            INFO.info(null, ex);
        } catch (SecurityException ex) {
            INFO.info(null, ex);
        }

        return toString_;
    }

    /**
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = toString() + "\n";
        changeLog += "20090915 Added Javadoc\n";
        return showChangeLog__ ? changeLog : toString();
    }
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////


//
//package ai.ilikeplaces.jpa;
//
//import ai.ilikeplaces.doc.*;
//import ai.ilikeplaces.exception.DBDishonourException;
//import ai.ilikeplaces.exception.DBException;
//import ai.ilikeplaces.exception.DBHazelcastRuntimeException;
//import ai.ilikeplaces.util.AbstractSLBCallbacks;
//import ai.ilikeplaces.util.Loggers;
//import com.hazelcast.client.ClientConfig;
//import com.hazelcast.client.HazelcastClient;
//import com.hazelcast.core.IMap;
//
//import javax.ejb.Stateless;
//import javax.ejb.TransactionAttribute;
//import javax.ejb.TransactionAttributeType;
//import javax.persistence.*;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.InetSocketAddress;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
///**
//* @param <T>
//* @author Ravindranath Akila
//*/
//
//@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
//@Stateless@Interceptors({EntityManagerInjector.class})

//@CONVENTION(convention = "Caller assumes this class methods are of tx scope supports, " +
//        "but create and updates here are of scope mandatory which will ensure caller does no mistake.")
//@OK
//public class CrudService<T> extends AbstractSLBCallbacks implements CrudServiceLocal<T> {
//
//    /**
//     * Please not that this is a field of Stateless session bean
//     */
//    @NOTE(note = "find out how the manager handles concurrent requests. same cache or different? if different, come on, this a class variable!" +
//            "resolved! check out the hibernate site article on this. apparently, container does this. amazing!")
//    @PersistenceContext(unitName = "adimpression_ilikeplaces_war_1.6-SNAPSHOTPU_DN\"", type = PersistenceContextType.TRANSACTION)
//    public EntityManager entityManager;
//
//
//    /**
//     * Lazily initialized
//     */
//    @WARNING("Lazily initialized upon entity lifecycle callbacks")
//    private static HazelcastClient hazelcastClient;
//    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU_DN");
//
//    private HazelcastClient getHCClient() {
//        if (hazelcastClient == null) {
//            ClientConfig clientConfig = new ClientConfig();
//
//            clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
//            clientConfig.addAddress("127.0.0.1:5701");
//
//            if (DEBUG_ENABLED) {
//                Loggers.debug("THESE ARE THE HAZELCAST ADDRESSES FYR:");
//                for (final InetSocketAddress inetSocketAddress : clientConfig.getAddressList()) {
//                    Loggers.debug("Host " + inetSocketAddress.getHostName() + " on Port " + inetSocketAddress.getPort());
//                }
//            }
//
//            hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);
//
//            if (DEBUG_ENABLED) {
//                Loggers.debug("THESE ARE THE HAZELCAST CLIENT(ME) DETAILS FYR:");
//                Loggers.debug("clientConfig.getGroupConfig().getName():");
//                Loggers.debug(clientConfig.getGroupConfig().getName());
//                Loggers.debug("clientConfig.getGroupConfig().toString():");
//                Loggers.debug(clientConfig.getGroupConfig().toString());
//            }
//        } else {
//            if (!hazelcastClient.isActive()) {
//                Loggers.info(Loggers.CODE_HC + "Hazelcast client is shutdown. Restarting...");
//                hazelcastClient.restart();
//                Loggers.info(Loggers.CODE_HC + "Hazelcast client start state after attempted restart:" + hazelcastClient.isActive());
//            }
//        }
//
//        return hazelcastClient;
//    }
//
//
//    private Object getId(Object entity) throws IllegalAccessException, InvocationTargetException {
//        Object key = null;
//
//        final Method[] methods = entity.getClass().getMethods();
//
//        for (final Method method : methods) {
//            final Id annotation = method.getAnnotation(Id.class);
//            if (annotation != null) {
//                key = method.invoke(entity);
//            }
//        }
//
//        if (DEBUG_ENABLED) {
//            Loggers.debug(Loggers.CODE_HC + "Entity's @Id:" + key);
//        }
//
//        return key;
//    }
//
//    private IMap<Object, Object> getHCMap(final Object entity) {
//        final String mapName = entity.getClass().getName();
//        if (DEBUG_ENABLED) {
//            Loggers.debug(Loggers.CODE_HC + "Attempting to fetch map named from:" + mapName);
//        }
//        return getHCClient().getMap(mapName);
//    }
//
//    /**
//     *
//     * @param t
//     * @return
//     */
//    @TransactionAttribute(TransactionAttributeType.MANDATORY)
//    @Override
//    public T create(final T t) {
//        final EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//
//        try {
//            entityManager.persist(t);
//            entityManager.flush();
//            entityManager.refresh(t);
//        } catch (final Exception e) {
//            throw new DBException(e);
//        } finally {
//            entityManager.close();
//        }
//
//        {
//            try {
//                Object id = getId(t);
//
//                IMap<Object, Object> hcMap = getHCMap(t);
//
//                hcMap.lock(id);
//                hcMap.put(id, t);
//                hcMap.unlock(id);
//
//            } catch (final Throwable throwable) {
//                throw new DBHazelcastRuntimeException(Loggers.CODE_HC + "ERROR NEGOTIATING DATA VIA HAZELCAST", throwable);
//            }
//        }
//        return t;
//    }
// /**
//     *
//     * @param t
//     * @return
//     */
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    @Override
//    public T create(final EntityManager entityManager, final T t) {
//
//        try {
//            entityManager.persist(t);
//            entityManager.refresh(t);
//        } catch (final Exception e) {
//            throw new DBException(e);
//        } finally {
//        }
//
//        {
//            try {
//                Object id = getId(t);
//
//                IMap<Object, Object> hcMap = getHCMap(t);
//
//                hcMap.lock(id);
//                hcMap.put(id, t);
//                hcMap.unlock(id);
//
//            } catch (final Throwable throwable) {
//                throw new DBHazelcastRuntimeException(Loggers.CODE_HC + "ERROR NEGOTIATING DATA VIA HAZELCAST", throwable);
//            }
//        }
//        return t;
//    }
//
//    /**
//     * @param type
//     * @param id
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
//    public T find(final Class type, final Object id) {
//        final EntityManager entityManager = entityManagerFactory.createEntityManager();
//        final Object o;
//        try {
//            entityManager.getTransaction().begin();
//            o = entityManager.find(type, id);
//            entityManager.getTransaction().commit();
//        } finally {
//            entityManager.close();
//        }
//        return (T) o;
//    }
//
//    /**
//     * @param type
//     * @param id
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public T find(final EntityManager entityManager,final Class type, final Object id) {
//        final Object o;
//        try {
//            entityManager.getTransaction().begin();
//            o = entityManager.find(type, id);
//            entityManager.getTransaction().commit();
//        } finally {
//        }
//        return (T) o;
//    }
//
//    @Override
//    public T getReference(final EntityManager entityManager, Class type, final Object id) {
//        return (T) entityManager.getReference(type, id);
//    }
//
//
//    /**
//     * @param typeOfEntity
//     * @param idByWhichToLookup
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public T findBadly(Class typeOfEntity, Object idByWhichToLookup) {
//        final EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//        try {
//            entityManager.getTransaction().begin();
//            final Object object = entityManager.find(typeOfEntity, idByWhichToLookup);
//            entityManager.getTransaction().commit();
//            if (object != null) {
//                return (T) object;
//            } else {
//                throw DBDishonourException.QUERYING_AFFIRMATIVELY_A_NON_EXISTING_ENTITY;
//            }
//        } finally {
//            entityManager.close();
//        }
//    }
//
//    /**
//     * @param typeOfEntity
//     * @param idByWhichToLookup
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public T findBadly(final EntityManager entityManager,final Class typeOfEntity,final Object idByWhichToLookup) {
//
//        try {
//            entityManager.getTransaction().begin();
//            final Object object = entityManager.find(typeOfEntity, idByWhichToLookup);
//            entityManager.getTransaction().commit();
//            if (object != null) {
//                return (T) object;
//            } else {
//                throw DBDishonourException.QUERYING_AFFIRMATIVELY_A_NON_EXISTING_ENTITY;
//            }
//        } finally {
//        }
//    }
//
//    /**
//     * @param type
//     * @param id
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public void delete(final Class type, final Object id) {
//        final EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//        try {
//            entityManager.remove(entityManager.getReference(type, id));
//        } catch (final Exception e) {
//            throw new DBException(e);
//        } finally {
//            entityManager.close();
//        }
//
//        {
//            try {
//                Object key = getId(id);
//
//                IMap<Object, Object> hcMap = getHCMap(id);
//
//                hcMap.lock(key);
//                hcMap.remove(key);
//                hcMap.unlock(key);
//
//            } catch (final Throwable t) {
//                throw new DBHazelcastRuntimeException(Loggers.CODE_HC + "ERROR NEGOTIATING DATA VIA HAZELCAST", t);
//            }
//        }
//    }
//
//    /**
//     * @param t
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.MANDATORY)
//    public T update(final T t) {
//        final EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//
//        final T mmanaged;
//        try {
//            mmanaged = entityManager.merge(t);
//        } catch (final Exception e) {
//            throw new DBException(e);
//        } finally {
//            entityManager.close();
//        }
//
//        {
//            try {
//
//                Object key = getId(t);
//
//                IMap<Object, Object> hcMap = getHCMap(t);
//
//                hcMap.lock(key);
//                hcMap.put(key, t);
//                hcMap.unlock(key);
//
//            } catch (final Throwable throwable) {
//                throw new DBHazelcastRuntimeException(Loggers.CODE_HC + "ERROR NEGOTIATING DATA VIA HAZELCAST", throwable);
//
//            }
//        }
//
//        return mmanaged;
//    }
//
//
//    /**
//     * @param t
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.MANDATORY)
//    public T update(final EntityManager entityManager,final T t) {
//
//
//        final T mmanaged;
//        try {
//            mmanaged = entityManager.merge(t);
//        } catch (final Exception e) {
//            throw new DBException(e);
//        } finally {        }
//
//        {
//            try {
//
//                Object key = getId(t);
//
//                IMap<Object, Object> hcMap = getHCMap(t);
//
//                hcMap.lock(key);
//                hcMap.put(key, t);
//                hcMap.unlock(key);
//
//            } catch (final Throwable throwable) {
//                throw new DBHazelcastRuntimeException(Loggers.CODE_HC + "ERROR NEGOTIATING DATA VIA HAZELCAST", throwable);
//
//            }
//        }
//
//        return mmanaged;
//    }
//
//
//    /**
//     * @param namedQueryName
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public List findWithNamedQuery(final String namedQueryName) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        final List resultList;
//        try {
//            entityManager.getTransaction().begin();
//            resultList = entityManager.createNamedQuery(namedQueryName).getResultList();
//            entityManager.getTransaction().commit();
//        } finally {
//            entityManager.close();
//        }
//
//        return resultList;
//    }
//
//
//    /**
//     * @param namedQueryName
//     * @param parameters
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public List findWithNamedQuery(final String namedQueryName, final Map parameters) {
//        return findWithNamedQuery(namedQueryName, parameters, 0);
//    }
//
//
//    /**
//     * @param queryName
//     * @param resultLimit
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public List findWithNamedQuery(final String queryName, final int resultLimit) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//        final List resultList;
//        try {
//            entityManager.getTransaction().begin();
//            resultList = entityManager.createNamedQuery(queryName).
//                    setMaxResults(resultLimit).
//                    getResultList();
//            entityManager.getTransaction().commit();
//        } finally {
//            entityManager.close();
//        }
//
//        return resultList;
//    }
//
//
//    /**
//     * @param sql
//     * @param type
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public List findByNativeQuery(final String sql, final Class type) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        final List resultList;
//        try {
//            entityManager.getTransaction().begin();
//            resultList = entityManager.createNativeQuery(sql, type).getResultList();
//            entityManager.getTransaction().commit();
//        } finally {
//            entityManager.close();
//        }
//        entityManager.close();
//        return resultList;
//    }
//
//
//    /**
//     * @param namedQueryName
//     * @param parameters
//     * @param resultLimit
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public List findWithNamedQuery(final String namedQueryName, final Map parameters, final int resultLimit) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        try {
//            entityManager.getTransaction().begin();
//            @SuppressWarnings("unchecked")
//            final Set<Entry> rawParameters = parameters.entrySet();
//            final Query query = entityManager.createNamedQuery(namedQueryName);
//            if (resultLimit > 0) {
//                query.setMaxResults(resultLimit);
//            }
//            for (final Entry entry : rawParameters) {
//                query.setParameter((String) entry.getKey(), entry.getValue());
//            }
//            entityManager.getTransaction().commit();
//            return query.getResultList();
//        } finally {
//            entityManager.close();
//        }
//    }
//
//    public EntityManagerFactory getEntityManagerFactory() {
//        return entityManagerFactory;
//    }
//}
//
