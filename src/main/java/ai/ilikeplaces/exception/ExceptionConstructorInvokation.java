package ai.ilikeplaces.exception;

/**
 * Throw this exception if constructor failed to initialize
 * @author Ravindranath Akila
 */
final public class ExceptionConstructorInvokation extends RuntimeException {

    final static private String SeeLogs = "\nSORRY! I COULD NOT INITIALIZE THIS INSTANCE. PLEASE SEE LOGS FOR MORE DETAILS.";

    public ExceptionConstructorInvokation(final Throwable cause) {
        super(cause);
    }

    public ExceptionConstructorInvokation(final String message, final Throwable cause) {
        super(message + SeeLogs, cause);
    }

    public ExceptionConstructorInvokation(final String message) {
        super(message + SeeLogs);
    }
}
