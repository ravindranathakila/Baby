package ai.baby.servlets.filters;

import ai.scribble.License;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: May 29, 2010
 * Time: 3:12:56 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class Debug implements Filter {


    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        //Nothing to do here
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);//Now one can debug the entire call flow, provided this users / as the filter and resides on top of all filters
    }

    @Override
    public void destroy() {
        //Nothing to do here
    }

}
