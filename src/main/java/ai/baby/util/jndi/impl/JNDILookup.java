package ai.baby.util.jndi.impl;

import javax.naming.NamingException;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 12/19/10
 * Time: 7:38 AM
 */
public interface JNDILookup {
    public Object lookup() throws NamingException;
}
