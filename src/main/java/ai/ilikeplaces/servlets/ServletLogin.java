package ai.ilikeplaces.servlets;

import ai.doc.License;
import ai.doc._fix;
import ai.doc._todo;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.ilikeplaces.exception.ConstructorInvokationException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.security.face.SingletonHashingRemote;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.SessionBoundBadRefWrapper;
import ai.util.HumanId;
import net.sf.oval.exception.ConstraintsViolatedException;
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

@_fix(issue = "XSS")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_todo(task = "USE A STATIC METHOD TO GET THE LOGGED ON USER INSTANCE.")
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

    final Logger logger = LoggerFactory.getLogger(ServletLogin.class.getName());
    final private Properties p_ = new Properties();
    private Context context = null;
    private SingletonHashingRemote singletonHashingRemote = null;
    public static final String HEADER_REFERER = "Referer";

    final PageFace home = Page.home;
    final PageFace signup = Page.signup;

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

                singletonHashingRemote = (SingletonHashingRemote) context.lookup("SingletonHashingLocal");
                if (singletonHashingRemote == null) {
                    log.append("\nVARIABLE singletonHashingRemote IS NULL! ");
                    log.append(singletonHashingRemote);
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

        final HttpSession userSession_;
        handleHttpSession:
        {
            /**
             * Remove any redundant session
             */
            if (request__.getSession(false) != null) {
                request__.getSession(false).invalidate();
            }

            /**
             * Make user session anyway as he came to log in
             */
            userSession_ = request__.getSession();


            /**
             * Set a timeout compatible with the stateful session bean handling user
             */
            userSession_.setMaxInactiveInterval(Integer.parseInt(RBGet.globalConfig.getString("UserSessionIdleInterval")));
        }


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
                response__.sendRedirect(request__.getRequestURI());
                break doLogin;
            }
            if (userSession_.getAttribute(HumanUser) == null) {
                /*Ok the session does not have the bean, initialize it with the user with email id and password*/
                if (request__.getParameter(Username) != null && request__.getParameter(Password) != null) {/*We need both these to sign in a user*/
                    try {
                        Human existingUser = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(request__.getParameter(Username));
                        if (existingUser != null && existingUser.getHumanAlive()) {/*Ok user name valid but now we check for password*/
                            final HumansAuthentication humansAuthentications = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(request__.getParameter(Username))).returnValue();

                            if (humansAuthentications.getHumanAuthenticationHash().equals(singletonHashingRemote.getHash(request__.getParameter(Password), humansAuthentications.getHumanAuthenticationSalt()))) {
                                final HumanUserLocal humanUserLocal = (HumanUserLocal) context.lookup(HumanUserLocal.NAME);
                                humanUserLocal.setHumanUserId(request__.getParameter(Username));
                                userSession_.setAttribute(HumanUser, (new SessionBoundBadRefWrapper<HumanUserLocal>(humanUserLocal, userSession_)));

                                logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0001") + ((SessionBoundBadRefWrapper<HumanUserLocal>) userSession_.getAttribute(HumanUser)).getBoundInstance().getHumanUserId());

                                final String referer = request__.getHeader(HEADER_REFERER);
                                response__.sendRedirect(!referer.contains("signup") ? referer : home.getURL());

                                break doLogin;/*This is unnecessary but lets not leave chance for introducing bugs*/
                            } else {/*Ok password wrong or not activated. What do we do with this guy? First lets make his session object null*/
                                userSession_.invalidate();
                                logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0002"));
                                Loggers.USER.info(existingUser.getHumanId() + " entered wrong password.");
                                response__.sendRedirect(request__.getHeader(HEADER_REFERER));
                                break doLogin;
                            }
                        } else {/*There is no such user. Ask if he forgot username or whether to create a new account :)*/
                            logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0003"));
                            response__.sendRedirect(request__.getHeader(HEADER_REFERER));
                            break doLogin;
                        }

                    } catch (final Exception ex) {
                        logger.error(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0004"), ex);
                        response__.sendRedirect(request__.getHeader(HEADER_REFERER));
                        break doLogin;
                    }
                } else {/*Why was the user sent here without either username or password or both(by the page)? Send him back!*/
                    logger.warn(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0009") + request__.getRequestURL().toString());
                    response__.sendRedirect(request__.getHeader(HEADER_REFERER));
                    break doLogin;
                }

            } else {/*Why did the user come to this page if he was already logged on? Send him back!*/
                logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletLogin.0005") + ((SessionBoundBadRefWrapper<HumanUserLocal>) userSession_.getAttribute(HumanUser)).getBoundInstance().getHumanUserId());
                response__.sendRedirect(request__.getHeader(HEADER_REFERER));
            }
        }
    }

    private boolean isSignOnPermitted() {
        return RBGet.getGlobalConfigKey("signOnEnabled") != null && RBGet.getGlobalConfigKey("signOnEnabled").equals("true");
    }


    static public boolean isCorrectPassword(final HumanId humanId, final String password) {
        final boolean isCorrect;
        if (humanId.validate() == 0) {
            final HumansAuthentication humansAuthentications = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(humanId).returnValue();
            isCorrect = humansAuthentications.getHumanAuthenticationHash().equals(DB.getSingletonHashingFaceLocal(false).getHash(password, humansAuthentications.getHumanAuthenticationSalt()));
        } else {
            throw new ConstraintsViolatedException(humanId.getViolations());
        }
        return isCorrect;
    }


    static public Return<Boolean> changePassword(final HttpSession httpSession, final HumanId humanId, final Password currentPass, final Password newPass) {
        Return<Boolean> returnVal;
        if (humanId.validate() != 0) {
            throw new ConstraintsViolatedException(humanId.getViolations());
        } else if (currentPass.validate() != 0) {
            throw new ConstraintsViolatedException(currentPass.getViolations());
        } else if (newPass.validate() != 0) {
            throw new ConstraintsViolatedException(newPass.getViolations());
        } else {
            returnVal = DB.getHumanCRUDHumanLocal(true).doUHumanPassword(humanId, currentPass, newPass);
            if (returnVal.returnStatus() == 0 && returnVal.returnValue()) {
                Loggers.USER.info("Password changed for user " + humanId.getObj());
                {
                    Loggers.USER.info("Attempting to invalidating session due to password change for user " + humanId.getObj());
                    if (httpSession != null) {
                        try {
                            httpSession.invalidate();
                            Loggers.USER.info("Successfully to invalidated session due to password change for user " + humanId.getObj());
                        } catch (final Exception e) {
                            Loggers.USER.info("FAILED to invalidated session due to password change for user " + humanId.getObj());
                            Loggers.EXCEPTION.error("", e);
                        }
                    }

                }
            }
        }
        return returnVal;
    }

    static public Return<Boolean> changePassword(final HumanId humanId, final Password newPass) {
        Return<Boolean> returnVal;
        if (humanId.validate() != 0) {
            throw new ConstraintsViolatedException(humanId.getViolations());
        } else if (newPass.validate() != 0) {
            throw new ConstraintsViolatedException(newPass.getViolations());
        } else {
            returnVal = DB.getHumanCRUDHumanLocal(true).doUHumanPassword(humanId, newPass);
            if (returnVal.returnStatus() == 0 && returnVal.returnValue()) {
                Loggers.USER.info("Password changed for user " + humanId.getObj());
            }
        }
        return returnVal;
    }


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
