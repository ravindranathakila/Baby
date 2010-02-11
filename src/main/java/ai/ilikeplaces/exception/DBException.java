package ai.ilikeplaces.exception;

import ai.ilikeplaces.doc.License;

/**
 * Throw this exception if constructor failed to initialize
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class DBException extends RuntimeException {

    final static private String MSG = "\nSORRY! I ENCOUNTERED SOME ERRORS IN THIS DATABASE OPERATION. SEE BELOW FOR MORE DETAILS.\n";

    /**
     * @param cause
     */
    public DBException(final Throwable cause) {
        super(MSG, cause);
    }

    /**
     * @param message
     * @param cause
     */
    public DBException(final String message, final Throwable cause) {
        super(message + MSG, cause);
    }

    /**
     * @param message
     */
    public DBException(final String message) {
        super(message + MSG);
    }
}