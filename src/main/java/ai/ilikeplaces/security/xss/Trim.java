package ai.ilikeplaces.security.xss;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang.StringEscapeUtils.*;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@FIXME(issue = "trim before db and after db. say trimallin trimallout. reason: sql escape only upon in")
public class Trim {

    private Trim() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", Trim.class, this.hashCode());
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
        return escapeSql(escapeJavaScript(escapeHtml(inputString)));
    }
}
