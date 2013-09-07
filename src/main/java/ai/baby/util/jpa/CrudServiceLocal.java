package ai.baby.util.jpa;

import ai.scribble.License;
import ai.scribble._ok;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
@_ok
public interface CrudServiceLocal<T> {

    /**
     * @param t
     * @return
     */
    public T create(T t);

    /**
     * @param type
     * @param id
     * @return
     */
    public T find(Class type, Object id);

    /**
     * @param type
     * @param id
     * @return
     */
    public T getReference(final Class type, final Object id);

    /**
     * This method can be used for eager loading, but with a
     * thrown npe exception if null. In fact, this method
     * makes sure you can avoid a null check. If you are doing
     * a null check, you do not need to call this. use find
     * instead.
     * <p/>
     * A common approach to using this method is when you are sure a given ID is in the database when looking it up.
     * While this maybe tagged defensive, this approach actually reveals a possible null at the earliest.
     * Noticing a null, which is an attribute of all classes and has no type, at the earliest is best.
     * <p/>
     * <p/>
     * Use find if this check is not required.
     * <p/>
     * Use getReference if you need to load lazy with this behavior.
     *
     * @param typeOfEntity
     * @param idByWhichToLookup
     * @return
     */
    public T findBadly(Class typeOfEntity, Object idByWhichToLookup);

    /**
     * @param t
     * @return
     */
    public T update(T t);

    /**
     * @param type
     * @param id
     */
    public void delete(Class type, Object id);

    /**
     * @param queryName
     * @return
     */
    public List findWithNamedQuery(String queryName);

    /**
     * @param queryName
     * @param resultLimit
     * @return
     */
    public List findWithNamedQuery(String queryName, int resultLimit);

    /**
     * @param namedQueryName
     * @param parameters
     * @return
     */
    public List findWithNamedQuery(String namedQueryName, Map parameters);

    /**
     * @param namedQueryName
     * @param parameters
     * @param resultLimit
     * @return
     */
    public List findWithNamedQuery(String namedQueryName, Map parameters, int resultLimit);

    /**
     * @param sql
     * @param type
     * @return ListF
     */
    public List findByNativeQuery(final String sql, final Class type);
}
