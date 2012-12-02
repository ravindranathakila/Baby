package ai.ilikeplaces.entities.etc;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/5/12
 * Time: 8:27 PM
 */
public interface NSEntityLifecycleCallbacks<ENTITY> {

    public void create(final ENTITY entity) throws CloneNotSupportedException;

    public void read(final ENTITY entity);

    public void update(final ENTITY entity) throws CloneNotSupportedException;

    public void remove(final ENTITY entity);
}
