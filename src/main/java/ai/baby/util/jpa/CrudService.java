//package ai.ilikeplaces.util.jpa;
//
//import DBDishonourException;
//import DBHazelcastRuntimeException;
//import AbstractSLBCallbacks;
//import EntityManagerInjector;
//import Loggers;
//import ai.scribble.License;
//import ai.scribble.WARNING;
//import ai.scribble._convention;
//import ai.scribble._ok;
//import com.hazelcast.client.ClientConfig;
//import com.hazelcast.client.HazelcastClient;
//import com.hazelcast.core.IMap;
//
//import javax.ejb.Stateless;
//import javax.ejb.TransactionAttribute;
//import javax.ejb.TransactionAttributeType;
//import javax.persistence.EntityManager;
//import javax.persistence.Id;
//import javax.persistence.Query;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.InetSocketAddress;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
///**
// * @param <T>
// * @author Ravindranath Akila
// */
//
//@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
//@Stateless
//@_convention(convention = "Caller assumes this class methods are of tx scope supports, " +
//        "but create and updates here are of scope mandatory which will ensure caller does no mistake.")
//@_ok
//public class CrudService<T> extends AbstractSLBCallbacks implements CrudServiceLocal<T> {
//
//
//    /**
//     * Lazily initialized
//     */
//    @WARNING("Lazily initialized upon entity lifecycle callbacks")
//    private static HazelcastClient hazelcastClient;
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
//     * @param t
//     * @return
//     */
//    @TransactionAttribute(TransactionAttributeType.MANDATORY)
//    @Override
//    public T create(final T t) {
//        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
//        entityManager.persist(t);
//        //entityManager.refresh(t);
//        return t;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
//    public T find(final Class type, final Object id) {
//        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
//        return (T) entityManager.find(type, id);
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
//    @Deprecated//Hazelcast, thereby HBase cannot get updated this way unless a manual update goes through.
//    public T getReference(final Class type, final Object id) {
//        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
//        return (T) entityManager.getReference(type, id);
//    }
//
//    /**
//     * @param typeOfEntity
//     * @param idByWhichToLookup
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
//    public T findBadly(Class typeOfEntity, Object idByWhichToLookup) {
//        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
//        final Object object = entityManager.find(typeOfEntity, idByWhichToLookup);
//        if (object != null) {
//            return (T) object;
//        } else {
//            throw DBDishonourException.QUERYING_AFFIRMATIVELY_A_NON_EXISTING_ENTITY;
//        }
//    }
//
//    /**
//     * @param type
//     * @param id
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    @TransactionAttribute(TransactionAttributeType.MANDATORY)
//    public void delete(final Class type, final Object id) {
//        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
//        entityManager.remove(entityManager.getReference(type, id));
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
//        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
//        final T mmanaged = entityManager.merge(t);
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
//    /**
//     * @param namedQueryName
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
//    public List findWithNamedQuery(final String namedQueryName) {
//        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
//        return entityManager.createNamedQuery(namedQueryName).getResultList();
//    }
//
//    /**
//     * @param namedQueryName
//     * @param parameters
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
//    public List findWithNamedQuery(final String namedQueryName, final Map parameters) {
//        return findWithNamedQuery(namedQueryName, parameters, 0);
//    }
//
//    /**
//     * @param queryName
//     * @param resultLimit
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
//    public List findWithNamedQuery(final String queryName, final int resultLimit) {
//        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
//        return entityManager.createNamedQuery(queryName).
//                setMaxResults(resultLimit).
//                getResultList();
//    }
//
//    /**
//     * @param sql
//     * @param type
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
//    public List findByNativeQuery(final String sql, final Class type) {
//        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
//        return entityManager.createNativeQuery(sql, type).getResultList();
//    }
//
//    /**
//     * @param namedQueryName
//     * @param parameters
//     * @param resultLimit
//     * @return
//     */
//    @Override
//    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
//    public List findWithNamedQuery(final String namedQueryName, final Map parameters, final int resultLimit) {
//        final EntityManager entityManager = EntityManagerInjector.THREAD_SAFE_ENTITY_MANAGER.get();
//
//        @SuppressWarnings("unchecked")
//        final Set<Entry> rawParameters = parameters.entrySet();
//        final Query query = entityManager.createNamedQuery(namedQueryName);
//        if (resultLimit > 0) {
//            query.setMaxResults(resultLimit);
//        }
//        for (final Entry entry : rawParameters) {
//            query.setParameter((String) entry.getKey(), entry.getValue());
//        }
//        return query.getResultList();
//    }
//
//    /**
//     * @return toString_
//     */
//    @Override
//    public String toString() {
//        String toString_ = getClass().getName();
//        try {
//            final Field[] fields = {getClass().getDeclaredField("entityManager")};
//
//            for (final Field field : fields) {
//                try {
//                    toString_ += "\n{" + field.getName() + "," + field.get(this) + "}";
//                } catch (IllegalArgumentException ex) {
//                    INFO.info(null, ex);
//                } catch (IllegalAccessException ex) {
//                    INFO.info(null, ex);
//                }
//            }
//        } catch (NoSuchFieldException ex) {
//            INFO.info(null, ex);
//        } catch (SecurityException ex) {
//            INFO.info(null, ex);
//        }
//
//        return toString_;
//    }
//
//    /**
//     * @param showChangeLog__
//     * @return changeLog
//     */
//    public String toString(final boolean showChangeLog__) {
//        String changeLog = toString() + "\n";
//        changeLog += "20090915 Added Javadoc\n";
//        return showChangeLog__ ? changeLog : toString();
//    }
//}


///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////


package ai.baby.util.jpa;

import ai.baby.util.exception.DBDishonourException;
import ai.baby.util.AbstractSLBCallbacks;
import ai.scribble.License;
import ai.scribble._convention;
import ai.scribble._note;
import ai.scribble._ok;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.lang.reflect.Field;
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
@_convention(convention = "Caller assumes this class methods are of tx scope supports, " +
        "but create and updates here are of scope mandatory which will ensure caller does no mistake.")
@_ok
public class CrudService<T> extends AbstractSLBCallbacks implements CrudServiceLocal<T> {

    /**
     * Please not that this is a field of Stateless session bean
     */
    @_note(note = "find out how the manager handles concurrent requests. same cache or different? if different, come on, this a class variable!" +
            "resolved! check out the hibernate site article on this. apparently, container does this. amazing!")
    @PersistenceContext(unitName = "ilpMainSchema", type = PersistenceContextType.TRANSACTION)
    public EntityManager entityManager;

    /**
     * @param t
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    @Override
    public T create(final T t) {
        entityManager.persist(t);
        entityManager.flush();
        entityManager.refresh(t);
        return t;
    }

    /**
     * @param type
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T find(final Class type, final Object id) {
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
    public T getReference(final Class type, final Object id) {
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
        entityManager.remove(this.entityManager.getReference(type, id));
    }

    /**
     * @param t
     * @return
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public T update(final T t) {
        return entityManager.merge(t);
    }

    /**
     * @param namedQueryName
     * @return
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List findWithNamedQuery(final String namedQueryName) {
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
        return this.entityManager.createNativeQuery(sql, type).getResultList();
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





