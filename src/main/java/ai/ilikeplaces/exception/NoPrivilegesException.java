package ai.ilikeplaces.exception;

import ai.scribble.License;

/**
 * Throw this exception if constructor failed to initialize
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class NoPrivilegesException extends DBDishonourCheckedException {

    final static private String MSG = "\nSORRY! I ENCOUNTERED AN OPERATION WHICH REQUIRES THIS USER TO PERFORM AN ACTION AND FOUND HE DOES NOT HAVE NECESSARY PRIVILEGES TO DO SO.\n";

    /**
     * @param message
     */
    public NoPrivilegesException(final String message) {
        super(MSG + message);
    }

    public NoPrivilegesException(String humanId, String action) {
        super(MSG + "User " + humanId + " has no privileges to " + action);
    }
}
