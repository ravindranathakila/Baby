package ai.baby.servlets.filters;

import ai.baby.util.SmartLogger;
import ai.baby.util.Loggers;
import ai.scribble.License;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
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
public class SubdomainForward implements Filter {
    public static final String ON_PARAMETER_IN_WEB_DOT_XML = "ON";

    protected static final String EMPTY = "";

    private static final String REQUESTED_PATH = "Requested Path:";

    protected static boolean ON = false;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        ON = Boolean.valueOf(filterConfig.getInitParameter(ON_PARAMETER_IN_WEB_DOT_XML));
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {

        if (!ON) {
            filterChain.doFilter(request, servletResponse);
            return;
        }

        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.DEBUG, null, 100, null, true);
        try {
            final String unformattedurl = ((HttpServletRequest) request).getRequestURL().toString();

            sl.appendToLogMSG(REQUESTED_PATH + unformattedurl);

            filterChain.doFilter(request, servletResponse);
            sl.complete(Loggers.LEVEL.DEBUG, "Simple Forward. Done.");

        } catch (final Throwable e) {
            sl.complete(Loggers.LEVEL.ERROR, e);
            filterChain.doFilter(request, servletResponse);
        }
    }

    @Override
    public void destroy() {
        //So far nothing to put here
    }
}
