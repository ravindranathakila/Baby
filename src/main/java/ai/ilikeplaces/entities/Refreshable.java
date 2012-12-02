package ai.ilikeplaces.entities;

/**
 * This interface is to be implemented by entities containing {@link java.util.Collection}s.
 * The main purpose is to encourage lazy fetching into the persistence context, and refreshing ONLY required collections.
 * <p/>
 * For example an entity student may have Set<Subject> and Set<ClassRoom>.
 * This interface promotes the fetc
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 2/9/11
 * Time: 6:43 PM
 */
public interface Refreshable<T> {
    public T refresh(final RefreshSpec refreshSpec) throws RefreshException;
}
