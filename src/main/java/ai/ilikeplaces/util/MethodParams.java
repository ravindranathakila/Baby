package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 15, 2010
 * Time: 1:14:18 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class MethodParams {

    final static private Logger logger = LoggerFactory.getLogger(MethodParams.class);

    @AroundInvoke
    public Object profile(InvocationContext invocation) throws Exception {
        final Object[] args = invocation.getParameters();
        try {
            return invocation.proceed();
        } finally {
            for (final Object arg : args) {
                logger.debug("HELLO, METHOD " + invocation.getMethod() + " RECEIVED PARAMETERS:" + Arrays.toString(args));
            }
        }
    }


}