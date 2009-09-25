package ai.ilikeplaces;

import ai.ilikeplaces.util.AbstractSLBCallbacks;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

/**
 *
 * @param <T>
 * @author Ravindranath Akila
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
final public class CrudService<T> extends AbstractSLBCallbacks implements CrudServiceLocal<T> {

    /**
     *
     * Please not that this is a field of Stateless session bean
     */
    @PersistenceContext(unitName = "adimpression_ilikeplaces_war_1.6-SNAPSHOTPU", type = PersistenceContextType.TRANSACTION)
    public EntityManager entityManager;

    /**
     *
     * @param t
     * @return
     */
    @Override
    public T create(final T t) {
        entityManager.persist(t);
        entityManager.flush();
        entityManager.refresh(t);
        return t;
    }

    /**
     *
     * @param type
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public T find(final Class type, final Object id) {
        return (T) entityManager.find(type, id);
    }

    /**
     *
     * @param type
     * @param id
     */
    @SuppressWarnings("unchecked")
    @Override
    public void delete(final Class type, final Object id) {
        entityManager.remove(this.entityManager.getReference(type, id));
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public T update(final T t) {
        return entityManager.merge(t);
    }

    /**
     *
     * @param namedQueryName
     * @return
     */
    @Override
    public List findWithNamedQuery(final String namedQueryName) {
        return entityManager.createNamedQuery(namedQueryName).getResultList();
    }

    /**
     *
     * @param namedQueryName
     * @param parameters
     * @return
     */
    @Override
    public List findWithNamedQuery(final String namedQueryName, final Map parameters) {
        return findWithNamedQuery(namedQueryName, parameters, 0);
    }

    /**
     *
     * @param queryName
     * @param resultLimit
     * @return
     */
    @Override
    public List findWithNamedQuery(final String queryName, final int resultLimit) {
        return entityManager.createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    /**
     *
     * @param sql
     * @param type
     * @return
     */
    public List findByNativeQuery(final String sql, final Class type) {
        return this.entityManager.createNativeQuery(sql, type).getResultList();
    }

    /**
     *
     * @param namedQueryName
     * @param parameters
     * @param resultLimit
     * @return
     */
    @Override
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
     *
     * @return toString_
     */
    @Override
    public String toString() {
        String toString_ = new String(getClass().getName());
        try {
            final Field[] fields = {getClass().getDeclaredField("entityManager")};

            for (final Field field : fields) {
                try {
                    toString_ += "\n{" + field.getName() + "," + field.get(this) + "}";
                } catch (IllegalArgumentException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        } catch (NoSuchFieldException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return toString_;
    }

    /**
     *
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = new String(toString() + "\n");
        changeLog += "20090915 Added Javadoc\n";
        return showChangeLog__ ? changeLog : toString();
    }
}

