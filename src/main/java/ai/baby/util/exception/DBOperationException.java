package ai.baby.util.exception;

import ai.scribble.License;

/**
 * Throw this exception if the DB is being dishonored by callers
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class DBOperationException extends RuntimeException {

    final static private String MSG = "SORRY! I ENCOUNTERED AN ERROR DURING OPERATION WITH THE DB. THE SOURCES SHOULD REFLECT THIS WHERE THE EXCEPTION WAS THROWN. SEE BELOW FOR MORE DETAILS.\n";

    /**
     * @param message
     */
    public DBOperationException(final String message) {
        super(MSG + message);
    }

    /**
     * @param t
     */
    public DBOperationException(final Throwable t) {
        super(t);
    }
}
