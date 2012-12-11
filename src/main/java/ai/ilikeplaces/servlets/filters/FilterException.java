package ai.ilikeplaces.servlets.filters;

import ai.ilikeplaces.logic.role.HumanUser;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.SessionBoundBadRefWrapper;
import ai.scribble.License;

import javax.ejb.NoSuchEJBException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: May 29, 2010
 * Time: 3:12:56 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class FilterException implements Filter {

    private FilterConfig filterConfig;


    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }


    /**
     * We expect the client to detect the last modified date. Apart from that, two weaks of caching would be ok for now.
     *
     * @param request
     * @param response
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse servletResponse = (HttpServletResponse) response;
        try {
            chain.doFilter(request, servletResponse);
        } catch (final NoSuchEJBException t) {
            final HttpSession session = ((HttpServletRequest) request).getSession(false);
            if (session != null) {
                //The following step is quite close to a risky zone where we are now assigning the user based simply on his session's email and no password. However, email would not be present had he not given the password before
                session.setAttribute(HumanUserLocal.NAME, (new SessionBoundBadRefWrapper<HumanUserLocal>(HumanUser.getHumanUserLocal(true), session)));
                servletResponse.sendRedirect(Controller.Page.home.getURL());
            }
        }
    }

    @Override
    public void destroy() {
        //Nothing interesting to do here :-(
    }


}
