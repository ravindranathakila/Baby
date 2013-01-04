package ai.ilikeplaces.servlets.filters;

/**
 *
 * http://stackoverflow.com/questions/138948/how-to-get-utf-8-working-in-java-webapps
 *
 * Please note that the location import itself, without wrong charsets(maybe) had data entries in
 * database such as ?? instead of the location name. Try doing
 *
 * select * from ilp.location where woeid=24865698 or any other id having encoding issues to verify
 *
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jul 5, 2010
 * Time: 12:34:33 PM
 */

import ai.scribble.License;

import javax.servlet.*;
import java.io.IOException;

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class FilterCharset implements Filter {
    private static final String TEXT_HTML_CHARSET_UTF_8 = "text/html; charset=UTF-8";
    private static final String UTF_8 = "UTF-8";
    private String encoding;

    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("requestEncoding");

        if (encoding == null) {
            encoding = UTF_8;
        }
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain next)
            throws IOException, ServletException {
        // Respect the client-specified character encoding
        // (see HTTP specification section 3.4.1)
        if (null == request.getCharacterEncoding()) {
            request.setCharacterEncoding(encoding);
        }


        /**
         * Set the default response content type and encoding
         */
        response.setContentType(TEXT_HTML_CHARSET_UTF_8);
        response.setCharacterEncoding(UTF_8);


        next.doFilter(request, response);
    }

    public void destroy() {
    }
}
