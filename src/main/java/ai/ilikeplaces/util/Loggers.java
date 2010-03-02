package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 30, 2010
 * Time: 3:09:36 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class Loggers {
    private static final String NULL = "null";

    private Loggers() {
        throw new UnsupportedOperationException("Static usage only");
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

    final static public Object ifNullToString(final Object object) {
        return object != null ? object : NULL;
    }
}
