package ai.ilikeplaces;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

    @PersistenceContext(unitName = "adimpression_ilikeplaces_war_1.6-SNAPSHOTPU", type = PersistenceContextType.TRANSACTION)
    public EntityManager entityManager;

//    public EntityManager entityManager = GlobalEntityManager.getSingleton().em;
    @Override
    public T create(final T t) {
        entityManager.persist(t);
        entityManager.flush();
        entityManager.refresh(t);
        return t;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T find(final Class type, final Object id) {
        return (T) entityManager.find(type, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(final Class type, final Object id) {
        entityManager.remove(this.entityManager.getReference(type, id));
    }

    @Override
    public T update(final T t) {
        return entityManager.merge(t);
    }

    @Override
    public List findWithNamedQuery(final String namedQueryName) {
        return entityManager.createNamedQuery(namedQueryName).getResultList();
    }

    @Override
    public List findWithNamedQuery(final String namedQueryName, final Map parameters) {
        return findWithNamedQuery(namedQueryName, parameters, 0);
    }

    @Override
    public List findWithNamedQuery(final String queryName, final int resultLimit) {
        return entityManager.createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    public List findByNativeQuery(final String sql, final Class type) {
        return this.entityManager.createNativeQuery(sql, type).getResultList();
    }

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
}

