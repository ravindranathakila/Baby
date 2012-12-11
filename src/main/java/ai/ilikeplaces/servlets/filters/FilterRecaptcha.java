package ai.ilikeplaces.servlets.filters;

import ai.scribble.License;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * What we are trying to do here is intercept a request on the Controller and validate if recaptcha is true.
 * <p/>
 * We will check if a "spam" variable is there in the call.
 * <p/>
 * If present, then we will see if it is recaptch valid. If valid, then we will let the request pass through.
 * <p/>
 * If not
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: May 29, 2010
 * Time: 3:12:56 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class FilterRecaptcha implements Filter {


    public void init(FilterConfig filterConfig) throws ServletException {
    }


    /**
     * @param request
     * @param response
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse servletResponse = (HttpServletResponse) response;
    }

    public void destroy() {
    }

}
