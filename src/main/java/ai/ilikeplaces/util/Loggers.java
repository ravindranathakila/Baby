package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Any loggers used outside the context of the variable name, might result in losing log entries.
 * For example, do not use DEBUG.info("msg"). Use, DEBUG.debug("msg").
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 30, 2010
 * Time: 3:09:36 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class Loggers {
    private static final String NULL = "null";
    private static final UnsupportedOperationException UNSUPPORTED_OPERATION_EXCEPTION = new UnsupportedOperationException("Static usage only");
    private static final String CAUSED = " caused ";

    final static public String EMBED = "{}";
    final static public String DONE = "done. ";
    final static public String FAILED = "FAILED! ";

    final static public String CODE_GFG = "[GFG]";//Code for Generic File Grabber servlet
    final static public String CODE_MEMC = "[MEMC]";//Code for Memc
    final static public String CODE_MAIL = "[MAIL]";//Code to indicate mail delivery

    public enum LEVEL {
        DEBUG,
        INFO,
        WARN,
        ERROR,
        SERVER_STATUS,
        USER,
        USER_EXCEPTION,
        NON_USER
    }

    private Loggers() {
        throw UNSUPPORTED_OPERATION_EXCEPTION;
    }

    /**
     * For the purpose of logging user activity
     */
    final static public Logger USER = LoggerFactory.getLogger("USER");

    /**
     * For the purpose of logging non user activity
     */
    final static public Logger NON_USER = LoggerFactory.getLogger("NON_USER");

    /**
     * Exceptions in one logger is easy for monitoring
     */
    final static public Logger EXCEPTION = LoggerFactory.getLogger("EXCEPTION");

    /**
     * Log server status/performance logs here
     */
    final static public Logger STATUS = LoggerFactory.getLogger("SERVER_STATUS");

    /**
     * User generated exceptions due to various mishandling(wrong parameters) of which logging should be
     * possible to be disabled. This is because high traffic with wrong parameters can introduce millions of
     * exceptions. So a separated logger is in place to log them. Logging them is important for analysis.
     */
    final static public Logger USER_EXCEPTION = LoggerFactory.getLogger("USER_EXCEPTION");

    /**
     * Just for ease of user instead of creating loggers everywhere
     */
    final static public Logger DEBUG = LoggerFactory.getLogger("DEBUG");

    /**
     * Just for ease of user instead of creating loggers everywhere
     */
    final static public Logger INFO = LoggerFactory.getLogger("INFO");

    /**
     * Just for ease of user instead of creating loggers everywhere
     */
    final static public Logger WARN = LoggerFactory.getLogger("WARN");

    static public Object ifNullToString(final Object object) {
        return object != null ? object : NULL;
    }

    static public void userError(final String username, final String error) {
        USER_EXCEPTION.error(username + CAUSED + error);
    }

    /**
     * Used to indicate that an object was finalized.
     *
     * @param msg
     */
    static public void finalized(final String msg) {
        DEBUG.debug(msg);
    }

    /**
     * Note that in the case of exception, the object will be/should be able to be,
     * cast to {@link Throwable}
     *
     * @param level
     * @param message
     * @param obj
     */
    static public void log(final LEVEL level, final String message, final Object obj) {
        switch (level) {
            case DEBUG:
                Loggers.DEBUG.debug(message, obj);
                break;
            case INFO:
                Loggers.INFO.info(message, obj);
                break;
            case WARN:
                Loggers.WARN.warn(message, obj);
                break;
            case ERROR:
                Loggers.EXCEPTION.error(message, (Throwable) obj);
                break;
            case SERVER_STATUS:
                Loggers.STATUS.info(message, obj);
                break;
            case USER:
                Loggers.USER.info(message, obj);
                break;
            case USER_EXCEPTION:
                Loggers.USER_EXCEPTION.error(message, obj);
                break;
            case NON_USER:
                Loggers.NON_USER.info(message, obj);
                break;
            default:
                Loggers.DEBUG.debug(
                        message + "(WARNING:MSG DEFAULTED DUE TO INAPPROPRIATE USAGE OF THIS METHOD",
                        obj);
        }
    }
}
