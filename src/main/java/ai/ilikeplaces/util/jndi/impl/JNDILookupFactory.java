package ai.ilikeplaces.util.jndi.impl;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 12/19/10
 * Time: 7:39 AM
 */
public interface JNDILookupFactory {
    public JNDILookup getInstance(final String lookupValue);
}
