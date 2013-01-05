package ai.ilikeplaces.servlets.filters;

import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.SmartLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

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

public class ProfileRedirect extends HttpServlet {
    public static final String PROFILE_URL = "/i/";
    private static final String SLASH = "/";
    private static final String PROFILE_PAGE_FORMAT = "/page/_i?" + "up" + "=";
    private static final String REDIRECTING_USER_TO = "Redirecting User To:";
    private static final String REQUESTED_PATH = "Requested Path:";

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(final HttpServletRequest request, final HttpServletResponse servletResponse) throws IOException, ServletException {
        final SmartLogger sl = SmartLogger.g();
        sl.l("Processing Profile Redirect");

        final String unformattedurl = request.getRequestURI();

        sl.l("unformattedurl:" + unformattedurl);

        if (unformattedurl.length() > 3 && unformattedurl.startsWith("/i/")) {// So the url could be /a/ too... but now we can safely proceed on splitting

            sl.l("Profile Redirect basic format matched");

            sl.appendToLogMSG(REQUESTED_PATH + unformattedurl);


            final String[] _split = unformattedurl.split("/i/");


            Loggers.info(Arrays.toString(_split));

            sl.appendToLogMSG(REDIRECTING_USER_TO + _split[_split.length - 1]);

            /**
             * Check if moving the DB.getHumanCRUDHumanLocal to a class variable during init is sensible. The only concern is
             * does the container destroy the bean without warning?
             */
            final String _profilePageFormat = PROFILE_PAGE_FORMAT;
            servletResponse.sendRedirect(_profilePageFormat + _split[1]);
            sl.complete("Profile Redirect successful:" + _profilePageFormat);
        } else {
            servletResponse.sendRedirect("/");
            sl.complete("Not a profile redirect. Sending back home.");
        }
    }

}
