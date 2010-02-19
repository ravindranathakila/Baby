package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 15, 2010
 * Time: 1:14:18 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class MethodTimer {

    final static private Logger logger = LoggerFactory.getLogger(MethodTimer.class);

    @AroundInvoke
    public Object profile(InvocationContext invocation) throws Exception {
        final long startTime = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            final long endTime = System.currentTimeMillis() - startTime;
            logger.debug("HELLO, METHOD " + invocation.getMethod() + " TOOK " + endTime + " (ms)");
        }
    }


}
