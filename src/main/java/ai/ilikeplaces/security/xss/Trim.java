package ai.ilikeplaces.security.xss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import static org.apache.commons.lang.StringEscapeUtils.*;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class Trim {

    private Trim() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", Trim.class, this.hashCode());
    }
    final static Logger logger = LoggerFactory.getLogger(Trim.class);

    final static public String trimAll(final String inputString, final Boolean... useNewString) {
        return useNewString.length == 1 && useNewString[0] ? trimAllLocal(new String(inputString)) : trimAllLocal(inputString);
    }

    final static private String trimAllLocal(final String inputString) {
        return escapeSql(escapeJavaScript(escapeHtml(inputString)));
    }
}
