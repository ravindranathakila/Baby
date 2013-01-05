package ai.ilikeplaces.servlets.filters;

import ai.ilikeplaces.util.SmartLogger;
import ai.scribble.License;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
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
public class LogoutNRedirect extends HttpServlet {
    private static final String HEADER_REFERER = "Referer";

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(final ServletRequest request, final ServletResponse servletResponse) throws IOException, ServletException {
        final SmartLogger sl = SmartLogger.g();

        final HttpServletRequest httpServletRequest = ((HttpServletRequest) request);

        final HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) {
            httpSession.invalidate();
            sl.appendToLogMSG("Session Removed.");
        } else {
            sl.appendToLogMSG("No Session To Be Removed.");
        }

        sl.appendToLogMSG("Redirecting User To Where Referrer Page");

        ((HttpServletResponse) servletResponse).sendRedirect(httpServletRequest.getHeader(HEADER_REFERER));

        sl.complete("Logout and Redirect processing successful");

    }

}
