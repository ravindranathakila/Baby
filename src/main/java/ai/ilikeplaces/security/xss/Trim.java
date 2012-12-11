package ai.ilikeplaces.security.xss;

import ai.scribble.License;
import ai.scribble._fix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_fix(issue = "trim before db and after db. say trimallin trimallout. reason: sql escape only upon in")
public class Trim {

    private Trim() {
    }

    final static Logger logger = LoggerFactory.getLogger(Trim.class);

    /**
     * Trims the given string to escape HTML JavaScript and SQL.
     *
     * @param inputString
     * @param useNewString ...
     * @return trimmed string
     */
    final static public String trimAll(final String inputString, final Boolean... useNewString) {
        return useNewString.length == 1 && useNewString[0] ? trimAllLocal(new String(inputString)) : trimAllLocal(inputString);
    }

    final static private String trimAllLocal(final String inputString) {
        //return escapeSql(escapeJavaScript(escapeHtml(inputString)));
        throw new UnsupportedOperationException("Apache commons lang has a memory leak this this method was removed. Try finding a new XSS solution.");
    }
}
