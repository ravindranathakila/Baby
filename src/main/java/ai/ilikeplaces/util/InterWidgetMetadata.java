package ai.ilikeplaces.util;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 8/21/11
 * Time: 12:18 PM
 */
public interface InterWidgetMetadata<T> {
    public Object[] getMetadata();

    public T setMetadata(final Object... metadata);
}
