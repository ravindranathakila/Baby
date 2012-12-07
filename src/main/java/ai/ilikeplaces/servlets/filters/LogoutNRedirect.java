package ai.ilikeplaces.servlets.filters;

import ai.doc.License;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.SmartLogger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Okay this class if for the profiles. However, still we do not support "very" short urls.
 * Instead, we support www.ilikeplaces/i/username kind of urls here. Looks better huh? :D
 * <p/>
 * <p/>
 * Hey this filter I say again, is to support www.ilikeplaces.com/i/ urls. No other filter should mess with this url too.
 * <p/>
 * <p/>
 * Well, nevertheless, when a request comes in the above format, we redirect it to the
 * ItsNat servlet, (current url format is /page/_i?up=userurl and maybe /page/_i_userurl.
 * We send a redirect there.
 * <p/>
 * <p/>
 * Let's try to make it fast and simple.
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jun 10, 2010
 * Time: 3:27:18 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class LogoutNRedirect implements Filter {
    private static final String SLASH = "/";
    private static final String LOG_OUT_REQUEST_FORMAT = "/page/_so";
    private static final String HEADER_REFERER = "Referer";
    private static final String LOG_OUT_REQUEST_RECEIVED = "Log-out Request Received.";
    private static final String SESSION_REMOVED = "Session Removed.";
    private static final String REDIRECTING_USER_TO_WHERE_REFERRER_PAGE = "Redirecting User To Where Referrer Page";

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        //So far nothing to put here
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        try {
            if (((HttpServletRequest) request).getRequestURL().toString().endsWith(LOG_OUT_REQUEST_FORMAT)) {
                final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.DEBUG, LOG_OUT_REQUEST_RECEIVED, 100, null, true);

                final HttpServletRequest httpServletRequest = ((HttpServletRequest) request);

                final HttpSession httpSession = httpServletRequest.getSession(false);
                if (httpSession != null) {
                    httpSession.invalidate();
                    sl.appendToLogMSG(SESSION_REMOVED);
                } else {
                    sl.appendToLogMSG("No Session To Be Removed.");
                }

                sl.appendToLogMSG(REDIRECTING_USER_TO_WHERE_REFERRER_PAGE + Controller.LOCATION_HUB);

                ((HttpServletResponse) servletResponse).sendRedirect(httpServletRequest.getHeader(HEADER_REFERER));

                sl.complete(Loggers.DONE);
            } else {
                filterChain.doFilter(request, servletResponse);
            }
        } catch (final Throwable e) {
            Loggers.EXCEPTION.error(Loggers.EMBED, e);
        }
    }

    @Override
    public void destroy() {
        //So far nothing to put here
    }
}
