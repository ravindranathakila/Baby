package ai.ilikeplaces.servlets;

import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.logic.Listeners.ListenerMain;
import ai.ilikeplaces.SBLoggedOnUserFace;
import ai.ilikeplaces.SessionBoundBadReferenceWrapper;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.ilikeplaces.exception.ExceptionConstructorInvokation;
import ai.ilikeplaces.security.face.SingletonHashingFace;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.doc.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ravindranath Akila
 */
@FIXME(issue="XSS")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class ServletLogin extends HttpServlet {

    /**
     * Stateful Session Bean Containing User Data
     */
    public final static String SBLoggedOnUser = "SBLoggedOnUser";
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
    final Logger logger = Logger.getLogger(ListenerMain.class.getName());
    final private Properties p_ = new Properties();
    private Context context = null;
    private CrudServiceLocal<Human> crudServiceLocal_ = null;
    private CrudServiceLocal<HumansAuthentication> crudServiceLocalHuman_ = null;
    private SingletonHashingFace singletonHashingFace = null;

    /**
     *
     */
    /**
     *
     */
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

                crudServiceLocal_ = (CrudServiceLocal) context.lookup("CrudServiceLocal");
                if (crudServiceLocal_ == null) {
                    log.append("\nVARIABLE crudServiceLocal_ IS NULL! ");
                    log.append(crudServiceLocal_);
                    break init;
                }
                crudServiceLocalHuman_ = (CrudServiceLocal) context.lookup("CrudServiceLocal");
                if (crudServiceLocalHuman_ == null) {
                    log.append("\nVARIABLE crudServiceLocalHuman_ IS NULL! ");
                    log.append(crudServiceLocalHuman_);
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
                logger.log(Level.SEVERE, "\nCOULD NOT INITIALIZE SIGNUP SERVLET DUE TO A NAMING EXCEPTION!", ex);
                break init;
            }

            /**
             * break. Do not let this statement be reachable if initialization
             * failed. Instead, break immediately where initialization failed.
             * At this point, we set the initializeFailed to false and thereby,
             * allow initialization of an instance
             */
            initializeFailed = false;
        }
        if (initializeFailed) {
            throw new ExceptionConstructorInvokation(log.toString());
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request__
     * @param response__
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(final HttpServletRequest request__, final HttpServletResponse response__)
            throws ServletException, IOException {

        response__.setContentType("text/html;charset=UTF-8");

        //merchantservices@moneybookers.com

        /**
         * Make user session anyway as he camed to log in
         */
        final HttpSession userSession_ = request__.getSession();

        /**
         * Set a timeout compatible with the stateful session bean handling user
         */
        userSession_.setMaxInactiveInterval(600);

        final Enumeration enumerated = request__.getParameterNames();
        logger.log(Level.SEVERE, "PARAMETERs");
        while (enumerated.hasMoreElements()) {
            String param = (String) enumerated.nextElement();
            logger.info("PARAMETER NAME:" + param);
            logger.info("VALUE:" + request__.getParameter(param));
        }
        doLogin:
        if (userSession_.getAttribute(SBLoggedOnUser) == null) {
            /*Ok the session does not have the bean, initialize it with the user with email id and password*/
            if (request__.getParameter(Username) != null && request__.getParameter(Password) != null) {/*We need both these to sign in a user*/
                try {
                    final SBLoggedOnUserFace sBLoggedOnUserFace = (SBLoggedOnUserFace) context.lookup("SBLoggedOnUserLocal");

                    Human loggedOnUser = crudServiceLocal_.find(Human.class, request__.getParameter(Username));
                    if (loggedOnUser != null) {/*Ok user name valid but now we check for passworld*/
                        HumansAuthentication humansAuthentications = crudServiceLocalHuman_.find(HumansAuthentication.class, request__.getParameter(Username));


                        if (humansAuthentications.getHumanAuthenticationHash().equals(singletonHashingFace.getHash(request__.getParameter(Password), humansAuthentications.getHumanAuthenticationSalt()))) {

                            sBLoggedOnUserFace.setLoggedOnUserId(request__.getParameter(Username));
                            userSession_.setAttribute(SBLoggedOnUser, (new SessionBoundBadReferenceWrapper<SBLoggedOnUserFace>(sBLoggedOnUserFace, userSession_, true)));
                            final PageFace home = Page.home;
                            logger.info("HELLO, SIGNON SUCCESSFUL! " + ((SessionBoundBadReferenceWrapper<SBLoggedOnUserFace>)userSession_.getAttribute(SBLoggedOnUser)).boundInstance.getLoggedOnUserId());
                            response__.sendRedirect(home.toString());
                            break doLogin;/*This is unnecessary but lets not leave chance for introducing bugs*/
                        } else {/*Ok password wrong. What do we do with this guy? First lets make his session object null*/
                            userSession_.invalidate();
                            logger.info("SORRY! PASSWORD WRONG!");
                            final PageFace home = Page.home;
                            response__.sendRedirect(home.toString());
                            break doLogin;
                        }
                    } else {/*There is no such user. Ask if he forgor username or whether to create a new account :)*/
                        response__.sendRedirect("/ilikeplaces/signup");
                        logger.info("SORRY! NO USER IN THAT NAME!");
                        break doLogin;
                    }

                } catch (NamingException ex) {/*Send back to login page or homepage*/
                    logger.log(Level.SEVERE, "SORRY! COULD NOT LOGIN USER DUE TO A NAMING EXCEPTION!", ex);
                    final PageFace signup = Page.signup;
                    response__.sendRedirect(signup.toString());
                    break doLogin;
                }
            } else {/*Why was the user sent here without either username or password or both(by the page)? Send him back!*/
                logger.severe("SORRY! PAGE SENDS USER TO LOGIN WITHOUT USERNAME PASSWORD OR BOTH. PROGRAMMATICAL ERROR! WHODIDIT:" + request__.getRequestURL().toString());
                final PageFace home = Page.home;
                response__.sendRedirect(home.toString());
                break doLogin;
            }

        } else {/*Why did the user come to this page if he was already logged on? Send him back!*/
            logger.info("SORRY! AN ALREADY LOGGED IN USER COMES TO LOGIN! REDIRECTED! USERNAME:" + ((SessionBoundBadReferenceWrapper<SBLoggedOnUserFace>)userSession_.getAttribute(SBLoggedOnUser)).boundInstance.getLoggedOnUserId());
            final PageFace home = Page.home;
            response__.sendRedirect(home.toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Nothing interesting here!";
    }// </editor-fold>
}
