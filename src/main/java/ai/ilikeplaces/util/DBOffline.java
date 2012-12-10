package ai.ilikeplaces.util;

import ai.doc.License;
import ai.reaver.ReturnImpl;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Type;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 15, 2010
 * Time: 1:14:18 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class DBOffline {

    public static boolean OFFLINE = false;
    private static Throwable DB_OFFLINE_EXCEPTION = new Throwable("DATABASE IS OFFLINE!");

    @AroundInvoke
    public Object notify(InvocationContext invocation) throws Exception {
        return OFFLINE ? new ReturnImpl<Type>(DB_OFFLINE_EXCEPTION, "I'm offline for maintenance.", true) : invocation.proceed();
    }
}
