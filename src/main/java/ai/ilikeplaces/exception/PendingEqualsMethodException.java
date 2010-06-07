package ai.ilikeplaces.exception;

import ai.ilikeplaces.doc.License;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Feb 3, 2010
 * Time: 5:07:40 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class PendingEqualsMethodException extends RuntimeException {
    final static private String MSG = "SORRY! YOU HAVE NOT IMPLEMENTED THE BODY OF EQUALS METHOD.";

    final static public PendingEqualsMethodException SINGLETON = new PendingEqualsMethodException();

    public PendingEqualsMethodException() {
        super(MSG);
    }
}
