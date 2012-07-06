package ai.ilikeplaces.entities;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/5/12
 * Time: 8:27 PM
 */
public interface NSEntityLifecycleCallbacks<ENTITY> {

    public void postPersist(final ENTITY entity) throws CloneNotSupportedException;

    public void postLoad(final ENTITY entity);

    public void postUpdate(final ENTITY entity) throws CloneNotSupportedException;

    public void postRemove(final ENTITY entity);
}
