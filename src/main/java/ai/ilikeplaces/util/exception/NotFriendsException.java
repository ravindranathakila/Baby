package ai.ilikeplaces.util.exception;

import ai.scribble.License;

/**
 * Throw this exception if constructor failed to initialize
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class NotFriendsException extends AbstractEjbApplicationRuntimeException {

    final static private String MSG = "\nSORRY! I ENCOUNTERED AN OPERATION WHICH REQUIRES TWO USERS TO BE FRIEND AND FOUND THEY AREN'T FRIENDS ANYMORE.\n";

    /**
     * @param message
     */
    public NotFriendsException(final String message) {
        super(MSG + message);
    }

    public NotFriendsException(String humanId, String friend) {
        super(MSG + "User " + humanId + " claims " + friend + " is a friend but actually, is not.");
    }
}
