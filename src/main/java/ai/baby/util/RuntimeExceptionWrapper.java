package ai.baby.util;

import ai.baby.util.exception.AbstractEjbApplicationRuntimeException;
import ai.reaver.ReturnImpl;
import ai.scribble.License;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 15, 2010
 * Time: 1:14:18 AM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class RuntimeExceptionWrapper {
    public static final String DUE_TO_INTERNAL_ERROR_S_THE_OPERATION_FAILED = "Due to Internal Error(s) The Operation FAILED!";
    public static final String DUE_TO_CRITICAL_INTERNAL_ERROR_S_THE_OPERATION_FAILED = "Due to CRITICAL Internal Error(s) The Operation FAILED!";

    @AroundInvoke
    public Object wrap(InvocationContext invocation) throws Exception {
        try {
            return invocation.proceed();
        } catch (final AbstractEjbApplicationRuntimeException e) {
            if (invocation.getMethod().getReturnType().isAssignableFrom(ReturnImpl.class)) {
                return new ReturnImpl<Object>(e, DUE_TO_INTERNAL_ERROR_S_THE_OPERATION_FAILED, true);
            } else {
                throw e;
            }
        } catch (final RuntimeException e) {//If this interceptor is used, this indicates that any checked exceptions have been ignored by the logic.
            if (invocation.getMethod().getReturnType().isAssignableFrom(ReturnImpl.class)) {
                return new ReturnImpl<Object>(e, DUE_TO_INTERNAL_ERROR_S_THE_OPERATION_FAILED, true);
            } else {
                throw e;
            }
        } catch (final Throwable e) {//If this interceptor is used, this indicates that any checked exceptions have been ignored by the logic.
            if (invocation.getMethod().getReturnType().isAssignableFrom(ReturnImpl.class)) {
                return new ReturnImpl<Object>(e, DUE_TO_CRITICAL_INTERNAL_ERROR_S_THE_OPERATION_FAILED, true);
            } else {
                throw new RuntimeException(e);
            }
        }
    }
}
