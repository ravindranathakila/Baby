package ai.ilikeplaces.servlets;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.ilikeplaces.exception.ConstructorInvokationException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.security.face.SingletonHashingFace;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.SessionBoundBadRefWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@FIXME(issue = "XSS")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@TODO(task = "USE A STATIC METHOD TO GET THE LOGGED ON USER INSTANCE.")
final public class ServletLogin extends HttpServlet {

    /**
     * Stateful Session Bean Containing User Data
     */
    public final static String HumanUser = HumanUserLocal.NAME;
    /**
     * Username, an email address to which the reset password link can be mailed
     */
    public final static String Username = "Username";
    /**
     * Password hash
     */
    public final static String Password = "Password";
    /**
     *
     */
    final Logger logger = LoggerFactory.getLogger(ServletLogin.class.getName());
    final private Properties p_ = new Properties();
    private Context context = null;
    private SingletonHashingFace singletonHashingFace = null;


    @Override
    @SuppressWarnings("unchecked")
    public void init() {
        boolean initializeFailed = true;
        final StringBuilder log = new StringBuilder();
        init:
        {
            try {
                p_.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
                context = new InitialContext(p_);
                if (context == null) {
                    log.append("\nVARIABLE context IS NULL! ");
                    log.append(context);
                    break init;
                }

                singletonHashingFace = (SingletonHashingFace) context.lookup("SingletonHashingLocal");
                if (singletonHashingFace == null) {
                    log.append("\nVARIABLE singletonHashingFace IS NULL! ");
                    log.append(singletonHashingFace);
                    break init;
                }
            } catch (NamingException ex) {
                log.append("\nCOULD NOT INITIALIZE SIGNUP SERVLET DUE TO A NAMING EXCEPTION!");
                logger.info("\nCOULD NOT INITIALIZE SIGNUP SERVLET DUE TO A NAMING EXCEPTION!", ex);
                break init;
            }

            /**
             * 
             * break. Do not let this statement be reachable if initialization
             * failed. Instead, break immediately where initialization failed.
             * At this point, we set the initializeFailed to false and thereby,
             * allow initialization of an instance
             */
            initializeFailed = false;
        }
        if (initializeFailed) {
            throw new ConstructorInvokationException(log.toString());
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request__
     * @param response__
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(final HttpServletRequest request__, final HttpServletResponse response__)
            throws ServletException, IOException {

        response__.setContentType("text/html;charset=UTF-8");

        /**
         * Make user session anyway as he came to log in
         */
        final HttpSession userSession_ = request__.getSession();

        /**
         * Set a timeout compatible with the stateful session bean handling user
         */
        userSession_.setMaxInactiveInterval(Integer.parseInt(RBGet.config.getString("UserSessionIdleInterval")));

        final Enumeration enumerated = request__.getParameterNames();
        logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0006"));
        while (enumerated.hasMoreElements()) {
            final String param = (String) enumerated.nextElement();
            logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0007"), param);
            logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0008"), request__.getParameter(param).length());
        }
        doLogin:
        {
            if (!isSignOnPermitted()) {
                final PageFace home = Page.home;
                response__.sendRedirect(home.toString());
                break doLogin;
            }
            if (userSession_.getAttribute(HumanUser) == null) {
                /*Ok the session does not have the bean, initialize it with the user with email id and password*/
                if (request__.getParameter(Username) != null && request__.getParameter(Password) != null) {/*We need both these to sign in a user*/
                    try {
                        final HumanUserLocal humanUserLocal = (HumanUserLocal) context.lookup(HumanUserLocal.NAME);

                        Human loggedOnUser = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(request__.getParameter(Username));
                        if (loggedOnUser != null) {/*Ok user name valid but now we check for password*/
                            final HumansAuthentication humansAuthentications = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(request__.getParameter(Username)).getHumansAuthentications();


                            if (humansAuthentications.getHumanAuthenticationHash().equals(singletonHashingFace.getHash(request__.getParameter(Password), humansAuthentications.getHumanAuthenticationSalt()))) {

                                humanUserLocal.setHumanUserId(request__.getParameter(Username));
                                userSession_.setAttribute(HumanUser, (new SessionBoundBadRefWrapper<HumanUserLocal>(humanUserLocal, userSession_, true)));
                                final PageFace home = Page.home;
                                logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0001") + ((SessionBoundBadRefWrapper<HumanUserLocal>) userSession_.getAttribute(HumanUser)).boundInstance.getHumanUserId());
                                response__.sendRedirect(home.toString());
                                break doLogin;/*This is unnecessary but lets not leave chance for introducing bugs*/
                            } else {/*Ok password wrong. What do we do with this guy? First lets make his session object null*/
                                userSession_.invalidate();
                                logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0002"));
                                final PageFace home = Page.home;
                                response__.sendRedirect(home.toString());
                                break doLogin;
                            }
                        } else {/*There is no such user. Ask if he forgor username or whether to create a new account :)*/
                            response__.sendRedirect("/ilikeplaces/signup");
                            logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0003"));
                            break doLogin;
                        }

                    } catch (NamingException ex) {/*Send back to login page or homepage*/
                        logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0004"), ex);
                        final PageFace signup = Page.signup;
                        response__.sendRedirect(signup.toString());
                        break doLogin;
                    }
                } else {/*Why was the user sent here without either username or password or both(by the page)? Send him back!*/
                    logger.warn(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0004") + request__.getRequestURL().toString());
                    final PageFace home = Page.home;
                    response__.sendRedirect(home.toString());
                    break doLogin;
                }

            } else {/*Why did the user come to this page if he was already logged on? Send him back!*/
                logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0005") + ((SessionBoundBadRefWrapper<HumanUserLocal>) userSession_.getAttribute(HumanUser)).boundInstance.getHumanUserId());
                final PageFace home = Page.home;
                response__.sendRedirect(home.toString());
            }
        }
    }

    final private boolean isSignOnPermitted() {
        return RBGet.getGlobalConfigKey("signOnEnabled") != null && RBGet.getGlobalConfigKey("signOnEnabled").equals("true");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Nothing interesting here!";
    }// </editor-fold>
}
