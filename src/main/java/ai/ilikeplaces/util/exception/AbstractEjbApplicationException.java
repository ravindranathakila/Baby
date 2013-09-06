package ai.ilikeplaces.util.exception;

import ai.scribble.License;

import javax.ejb.ApplicationException;

/**
 * This exception is the super class of all the checked application exceptions specific to I Like Places.
 * <p/>
 * This means, catching this exception as parent would be appropriate to catch all checked application exceptions.
 * <p/>
 * As implied, this means they will all be checked exceptions.
 * <p/>
 * <p/>
 * Note that the rollback value, regardless of the default(false) is set to false.
 * <p/>
 * Implementing classes can fine tune this by setting @ApplicationException(rollback = true)
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jun 17, 2010
 * Time: 9:32:33 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@ApplicationException(rollback = false)
public class AbstractEjbApplicationException extends Exception {
    public AbstractEjbApplicationException(final String message) {
        super(message);
    }

    public AbstractEjbApplicationException() {
    }

    public AbstractEjbApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AbstractEjbApplicationException(final Throwable cause) {
        super(cause);
    }
}
