package ai.ilikeplaces.util.exception;

import ai.scribble.License;

/**
 * Throw this exception if constructor failed to initialize
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class ConstructorInvokationException extends RuntimeException {

    final static private String SeeLogs = "\nSORRY! I COULD NOT INITIALIZE THIS INSTANCE. PLEASE SEE LOGS FOR MORE DETAILS.";

    /**
     * @param cause
     */
    public ConstructorInvokationException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ConstructorInvokationException(final String message, final Throwable cause) {
        super(message + SeeLogs, cause);
    }

    /**
     * @param message
     */
    public ConstructorInvokationException(final String message) {
        super(message + SeeLogs);
    }
}
