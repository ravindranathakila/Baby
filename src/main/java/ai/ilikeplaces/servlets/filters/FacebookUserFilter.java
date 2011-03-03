package ai.ilikeplaces.servlets.filters;


import com.google.code.facebookapi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * The Facebook User Filter ensures that a Facebook client that pertains to
 * the logged in user is available in the session object named "facebook.user.client".
 * <p/>
 * The session ID is stored as "facebook.user.session". It's important to get
 * the session ID only when the application actually needs it. The user has to
 * authorise to give the application a session key.
 *
 * @author Dave
 */
public class FacebookUserFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(FacebookUserFilter.class);

    private String api_key;
    private String secret;

    private static final String FACEBOOK_USER_CLIENT = "facebook.user.client";

    public void init(FilterConfig filterConfig) throws ServletException {
        api_key = filterConfig.getInitParameter("facebook_api_key");
        secret = filterConfig.getInitParameter("facebook_secret");
        if (api_key == null || secret == null) {
            throw new ServletException("Cannot initialise Facebook User Filter because the " +
                    "facebook_api_key or facebook_secret context init " +
                    "params have not been set. Check that they're there " +
                    "in your servlet context descriptor.");
        } else {
            logger.debug("Using facebook API key: " + api_key);
        }
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            logger.debug("user ip:" + req.getRemoteAddr());

            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;

            HttpSession session = request.getSession(true);
            IFacebookRestClient<Document> userClient = getUserClient(session);
            if (userClient == null) {
                logger.debug("User session doesn't have a Facebook API client setup yet. Creating one and storing it in the user's session.");
                userClient = new FacebookXmlRestClient(api_key, secret);
                session.setAttribute(FACEBOOK_USER_CLIENT, userClient);
            }

            logger.debug("Creating a FacebookWebappHelper, which copies fb_ request param data into the userClient");
            FacebookWebappHelper<Document> facebook = new FacebookWebappHelper<Document>(request, response, api_key, secret, userClient);
            String nextPage = request.getRequestURI();
            nextPage = nextPage.substring(nextPage.indexOf("/", 1) + 1); //cut out the first /, the context path and the 2nd /
            logger.debug(nextPage);
            boolean redirectOccurred = facebook.requireLogin(nextPage);
//            if (redirectOccurred) {
//                return;
//            }
            redirectOccurred = facebook.requireFrame(nextPage);
//            if (redirectOccurred) {
//                return;
//            }

            long facebookUserID;
            try {
                facebookUserID = userClient.users_getLoggedInUser();
            } catch (FacebookException ex) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while fetching user's facebook ID");
                logger.error("Error while getting cached (supplied by request params) value " +
                        "of the user's facebook ID or while fetching it from the Facebook service " +
                        "if the cached value was not present for some reason. Cached value = {}", userClient.getCacheUserId());
                return;
            }

            logger.debug("facebookUserId:" + facebookUserID);


            chain.doFilter(request, response);
        } finally {

        }
    }

    public static FacebookXmlRestClient getUserClient(HttpSession session) {
        return (FacebookXmlRestClient) session.getAttribute(FACEBOOK_USER_CLIENT);
    }
}