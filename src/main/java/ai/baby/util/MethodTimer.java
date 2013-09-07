package ai.baby.util;

import ai.reaver.RefObj;
import ai.scribble.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
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
    public static final String TIMING = "Timing:";

    @AroundInvoke
    public Object profile(InvocationContext invocation) throws Exception {
        if (DO_LOG.getObj()) {
            final long startTime = System.currentTimeMillis();
            final SmartLogger smartLogger = SmartLogger.start(
                    Loggers.LEVEL.SERVER_STATUS,
                    TIMING + invocation.getMethod(),
                    0,//Escapes timeout
                    null,//We don't need a start message (tedious when it comes out a lot in logs)
                    true//This is what we need actually
            );
            try {
                return invocation.proceed();
            } finally {
                smartLogger.multiComplete(new Loggers.LEVEL[]{Loggers.LEVEL.DEBUG, Loggers.LEVEL.SERVER_STATUS}, Loggers.DONE);
//                final long endTime = System.currentTimeMillis() - startTime;
//                Loggers.STATUS.info("INVOKING:\n" + invocation.getMethod() + "\nTOOK:\n " + endTime + "(millis)");
            }
        } else {
            return invocation.proceed();
        }
    }
}
