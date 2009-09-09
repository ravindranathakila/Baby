package ai.ilikeplaces;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.ejb.Stateful;
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
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CrudService<T> implements CrudServiceLocal<T> {

    @PersistenceContext(unitName = "adimpression_ilikeplaces_war_1.6-SNAPSHOTPU", type = PersistenceContextType.EXTENDED)
    public EntityManager entityManager;

//    public EntityManager entityManager = GlobalEntityManager.getSingleton().em;
    public T create(final T t) {
        entityManager.persist(t);
        entityManager.flush();
        entityManager.refresh(t);
        return t;
    }

    public boolean is() {
        return entityManager == null;
    }

    @SuppressWarnings("unchecked")
    public T find(final Class type, final Object id) {
        return (T) entityManager.find(type, id);
    }

    @SuppressWarnings("unchecked")
    public void delete(final Class type, final Object id) {
        final Object ref = this.entityManager.getReference(type, id);
        entityManager.remove(ref);
    }

    public T update(final T t) {
        return entityManager.merge(t);
    }

    public List findWithNamedQuery(final String namedQueryName) {
        return entityManager.createNamedQuery(namedQueryName).getResultList();
    }

    public List findWithNamedQuery(final String namedQueryName, final Map parameters) {
        return findWithNamedQuery(namedQueryName, parameters, 0);
    }

    public List findWithNamedQuery(final String queryName, final int resultLimit) {
        return entityManager.createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    public List findByNativeQuery(final String sql, final Class type) {
        return this.entityManager.createNativeQuery(sql, type).getResultList();
    }

    public List findWithNamedQuery(final String namedQueryName, final Map parameters, final int resultLimit) {
        Set<Entry> rawParameters = parameters.entrySet();
        Query query = entityManager.createNamedQuery(namedQueryName);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }
        for (Entry entry : rawParameters) {
            query.setParameter((String) entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }
}

