package ai.ilikeplaces.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 1, 2010
 * Time: 4:29:55 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class ReturnImpl<T> implements Return {

    final static private Logger logger = LoggerFactory.getLogger(ReturnImpl.class);

    final static private String logMsgBeginning = "SORRY! I ENCOUNTERED AN EXCEPTION! HOWEVER, THE APPLICATION SHOULD REMAIN INTACT. SEE BELOW FOR MORE DETAILS.\n\n";

    private int returnStatus = 1;

    private T returnValue = null;

    private Throwable returnError = null;

    private String returnMsg = null;

    public ReturnImpl(final int returnStatus, final T returnValue, final Throwable returnError, final String returnMsg) {
        this.returnStatus = returnStatus;
        this.returnValue = returnValue;
        this.returnError = returnError;
        this.returnMsg = returnMsg;
    }

    public ReturnImpl(final T returnValue, final String returnMsg) {
        this.returnStatus = 0;
        this.returnValue = returnValue;
        this.returnMsg = returnMsg;
    }

    public ReturnImpl(final Throwable returnError, final String returnMsg, final boolean shouldLog) {
        if (shouldLog) {
            Loggers.EXCEPTION.error(logMsgBeginning + returnMsg + "\n", returnError);
        }
        this.returnStatus = 1;
        this.returnError = returnError;
        this.returnMsg = returnMsg;
    }

    @Override
    final public int returnStatus() {
        return returnStatus;
    }

    @Override
    final public T returnValue() {
        return returnValue;
    }

    @Override
    final public Throwable returnError() {
        return returnError;
    }

    @Override
    public String returnMsg() {
        return returnMsg;
    }

    @Override
    public String toString() {
        return "Return{" +
                "returnStatus=" + returnStatus +
                ", returnValue=" + (returnValue != null ? returnValue.toString() : "null") +
                ", returnError=" + (returnError != null ? returnError.toString() : "null") +
                ", returnMsg='" + (returnMsg != null ? returnMsg : "") + '\'' +
                '}';
    }
}