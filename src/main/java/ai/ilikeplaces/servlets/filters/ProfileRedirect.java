package ai.ilikeplaces.servlets.filters;

import ai.ilikeplaces.logic.Listeners.ListenerI;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.SmartLogger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
 * User: Ravindranath Akila
 * Date: Jun 10, 2010
 * Time: 3:27:18 PM
 *
 */

public class ProfileRedirect implements Filter {
    private static final String SLASH = "/";
    private static final String PROFILE_PAGE_FORMAT = "/page/_i?" + ListenerI.USER_PROFILE + "=";
    public static final String PROFILE_URL = "/i/";
    private static final String REDIRECTING_USER_TO = "Redirecting User To:";
    private static final String REQUESTED_PATH = "Requested Path:";

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        //So far nothing to put here
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.DEBUG, "Processing Profile Redirect", 100, null, true);
        try {

            final String unformattedurl = ((HttpServletRequest) request).getRequestURL().toString();
            sl.appendToLogMSG(REQUESTED_PATH + unformattedurl);

            final int index = unformattedurl.lastIndexOf(SLASH);

            final String url = unformattedurl.substring(index + 1);

            //final String userURL = url.substring(3);// that is, "/i/username" becomes "username"

            sl.appendToLogMSG(REDIRECTING_USER_TO + url);

            /**
             * Check if moving the DB.getHumanCRUDHumanLocal to a class variable during init is sensible. The only concern is
             * does the container destroy the bean without warning?
             */
            ((HttpServletResponse) servletResponse).sendRedirect(PROFILE_PAGE_FORMAT + url);
            sl.complete(Loggers.DONE);
        } catch (final Throwable e) {
            sl.multiComplete(new Loggers.LEVEL[]{Loggers.LEVEL.DEBUG, Loggers.LEVEL.ERROR}, Loggers.FAILED);
        }
    }

    @Override
    public void destroy() {
        //So far nothing to put here
    }
}
