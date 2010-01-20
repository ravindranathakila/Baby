package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Type;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 15, 2010
 * Time: 1:14:18 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class DBOffilne {

    //final static private Logger logger = LoggerFactory.getLogger(DBOffilne.class);

    public static boolean OFFLINE = false;

    @AroundInvoke
    public Object notify(InvocationContext invocation) throws Exception {
        return OFFLINE ? new ReturnImpl<Type>(null, "Blush blush:P I'm offline for maintenance.", true) : invocation.proceed();
    }
}