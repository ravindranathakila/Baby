package ai.ilikeplaces.util;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 1, 2010
 * Time: 5:19:59 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface Factory<T> {

    /**
     * Returns a factory object of the given type
     * @param initArgs
     * @return a factory object of the given type
     */
    public Factory getFactory(final Object ... initArgs);

    public T getInstance(final Object ... initArgs);
}
