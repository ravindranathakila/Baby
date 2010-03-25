package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Mar 8, 2010
 * Time: 3:47:30 PM
 * Reusable exceptions
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ExceptionCache {
    final static public UnsupportedOperationException UNSUPPORTED_OPERATION_EXCEPTION = new UnsupportedOperationException("SORRY! THIS OPERATION IS NO SUPPORTED YET.");
    final static public UnsupportedOperationException STATIC_USAGE_ONLY_EXCEPTION = new UnsupportedOperationException("SORRY! THIS CLASS IS DESIGNED FOR STATIC USAGE ONLY.");
}
