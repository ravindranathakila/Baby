package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Feb 23, 2010
 * Time: 6:56:58 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class Parameter {
    final static private String EQUALS = "=";
    final static private String QMARK = "?";
    final static private String AMPERSAND = "&";

    final static public String get(final String key, final String value, final boolean... isFirst) {
        return (isFirst == null ? AMPERSAND : (isFirst[0] ? QMARK : AMPERSAND)) + key + EQUALS + value;
    }
}
