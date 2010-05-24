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

    private static final String SORRY_YOU_CANNOT_ASSIGN_A_NULL = "SORRY! YOU CANNOT ASSIGN A NULL.";
    public static final RefObj<Boolean> DO_LOG = new Obj<Boolean>(true) {

        @Override
        public void setObj(final Boolean status) {
            if (status != null) {
                obj = status;
            } else {
                throw new SecurityException(SORRY_YOU_CANNOT_ASSIGN_A_NULL);
            }
        }
    };

    @AroundInvoke
    public Object profile(InvocationContext invocation) throws Exception {
        if (DO_LOG.getObj()) {

            logger.debug(
                    "TARGET:\n\t"+invocation.getTarget().toString()+
                        "\nMETHOD NAME:\n\t" + invocation.getMethod().getName() +
                        "\nPARAMETERS:\n\t" + Arrays.toString(invocation.getParameters()));
            return invocation.proceed();
        } else {
            return invocation.proceed();
        }
    }
}