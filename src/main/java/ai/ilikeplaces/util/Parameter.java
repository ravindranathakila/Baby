package ai.ilikeplaces.util;

import ai.ilikeplaces.logic.validators.unit.SimpleString;
import ai.scribble.License;
import ai.scribble._todo;
import org.apache.commons.httpclient.URI;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Feb 23, 2010
 * Time: 6:56:58 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_todo(task = "Refactor to URLParameter")
public class Parameter {
    final static private String EQUALS = "=";
    final static private String QMARK = "?";
    final static private String AMPERSAND = "&";

    private SimpleString parameterString = new SimpleString("");


    /**
     * Warning: only first argument, which is the entire parameter string, will be taken, if given
     *
     * @param prependString
     */
    public Parameter(final String... prependString) {
        if (prependString.length == 1) {
            parameterString.setObj(prependString[0]);
        }
    }

    /**
     * To be used in static context only. Do not use it even with this classes objects
     *
     * @param key
     * @param value   toString will be called on this object to obtain the required String which means passing a String is also valid
     * @param isFirst
     * @return
     */
    static public String get(final String key, final String value, final boolean... isFirst) {
        return (isFirst.length == 0 ? AMPERSAND : (isFirst[0] ? QMARK : AMPERSAND)) + key + EQUALS + value;
    }

    /**
     * To be used in non-static context only
     *
     * @param key
     * @param value
     * @param isFirst which if not present is taken as false
     * @return
     */
    public Parameter append(final String key, final Object value, final boolean... isFirst) {
        parameterString.setObj(
                parameterString.getObj() +
                        (
                                (isFirst.length == 0 ? AMPERSAND : (isFirst[0] ? QMARK : AMPERSAND)) + key + EQUALS + value.toString()
                        )
        );
        return this;
    }

    public String get() {
        return parameterString.getObj();
    }

    public String toURL() {
        try {
            final URL url = new URL(parameterString.getObjectAsValid());
            final URI returnVal = new URI(url.getProtocol(), null, url.getHost(), 80, url.getPath(), url.getQuery(), null);
            return returnVal.toString();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
