package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.exception.DBDishonourException;
import ai.ilikeplaces.exception.PendingEqualsMethodException;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Mar 8, 2010
 * Time: 3:47:30 PM
 * Reusable exceptions
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ExceptionCache {
    final static public UnsupportedOperationException UNSUPPORTED_OPERATION_EXCEPTION = new UnsupportedOperationException("SORRY! THIS OPERATION IS NOT SUPPORTED YET.");
    final static public UnsupportedOperationException UNSUPPORTED_SWITCH = new UnsupportedOperationException("SORRY! THIS CASE IS NOT SUPPORTED YET.");
    final static public UnsupportedOperationException STATIC_USAGE_ONLY_EXCEPTION = new UnsupportedOperationException("SORRY! THIS CLASS IS DESIGNED FOR STATIC USAGE ONLY.");
    final static public PendingEqualsMethodException NO_EQUALS_METHOD_EXCEPTION = new PendingEqualsMethodException();
    final static public IllegalStateException NO_LOGIN = new IllegalStateException("SORRY! PLEASE LOG-IN TO PERFORM THIS OPERATION.");
    final static public RuntimeException FILE_DELETE_FAILED = new RuntimeException("SORRY! FILE DELETION FROM PATH FAILED.");
    final static public RuntimeException FILE_RENAME_FAILED = new RuntimeException("SORRY! FILE RENAME FAILED.");
    final static public RuntimeException CDN_FILE_UPLOAD_FAILED = new RuntimeException("SORRY! FILE UPLOAD TO CDN FAILED.");
    final static public RuntimeException FILE_SIZE_EXCEPTION = new RuntimeException("SORRY! FILE SIZE TOO BIG.");
}
