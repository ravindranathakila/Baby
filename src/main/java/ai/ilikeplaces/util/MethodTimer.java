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

    public static final RefObj<Boolean> DO_LOG = new Obj<Boolean>(true) {

        @Override
        public void setObj(final Boolean status) {
            if (status != null) {
                obj = status;
            } else {
                throw new SecurityException("SORRY! YOU CANNOT ASSIGN A NULL.");
            }
        }
    };

    @AroundInvoke
    public Object profile(InvocationContext invocation) throws Exception {
        if (DO_LOG.getObj()) {
            final long startTime = System.currentTimeMillis();
            try {
                return invocation.proceed();
            } finally {
                final long endTime = System.currentTimeMillis() - startTime;
                logger.debug("METHOD NAME:\n\t" + invocation.getMethod() + "\nTOOK:\n\t " + endTime + "(ms)");
            }
        } else {
            return invocation.proceed();
        }
    }
}
