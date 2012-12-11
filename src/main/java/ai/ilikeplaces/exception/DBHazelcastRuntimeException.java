package ai.ilikeplaces.exception;

import ai.scribble.License;

import javax.ejb.ApplicationException;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/24/12
 * Time: 10:18 AM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@ApplicationException(rollback = true)
public class DBHazelcastRuntimeException extends AbstractEjbApplicationRuntimeException {

    public DBHazelcastRuntimeException() {
        super();
    }

    public DBHazelcastRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
