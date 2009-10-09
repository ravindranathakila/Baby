package ai.ilikeplaces.servlets;

import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.SBLoggedOnUserFace;
//import ai.ilikeplaces.entities.Human.GenderCode.IllegalEnumValueException;
import ai.ilikeplaces.doc.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.HumansPrivatePhoto;
import ai.ilikeplaces.entities.HumansPublicPhoto;
import ai.ilikeplaces.exception.ExceptionConstructorInvokation;
import ai.ilikeplaces.security.blowfish.jbcrypt.BCrypt;
import ai.ilikeplaces.security.face.SingletonHashingFace;
import ai.ilikeplaces.servlets.Controller.Page;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@FIXME(issue = "XSS")
final public class ServletSignup extends HttpServlet {

    final Logger logger = Logger.getLogger(ServletSignup.class.getName());

    /*Session related static id references*/
    final static private String Username = "Username";
    final static private String Password = "Password";
    final static private String ConfirmPassword = "ConfirmPassword";
    final static private String Email = "Email";
    final static private String Gender = "Gender";
    final static private String DateOfBirth = "DateOfBirth";
    final static private String UsernameError = "UsernameError";
    final static private String PasswordError = "PasswordError";
    final static private String ConfirmPasswordError = "ConfirmPasswordError";
    final static private String EmailError = "EmailError";
    final static private String GenderError = "GenderError";
    final static private String DateOfBirthError = "DateOfBirthError";
    final static private String UsernameErrorMsg = "UsernameErrorMsg";
    final static private String PasswordErrorMsg = "PasswordErrorMsg";
    final static private String ConfirmPasswordErrorMsg = "ConfirmPasswordErrorMsg";
    final static private String EmailErrorMsg = "EmailErrorMsg";
    final static private String GenderErrorMsg = "GenderErrorMsg";
    final static private String DateOfBirthErrorMsg = "DateOfBirthErrorMsg";
    final static private String ErrorStatusTrue = "true";

    /*Container Related Services*/
    final private Properties p_ = new Properties();
    private Context context = null;
    private CrudServiceLocal<Human> crudServiceLocal_ = null;
    private SingletonHashingFace singletonHashingFace = null;
    private CrudServiceLocal<HumansAuthentication> crudServiceLocalHuman_ = null;

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
     * @throws ServletException
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(final HttpServletRequest request__, final HttpServletResponse response__)
            throws ServletException, IOException {

        response__.setContentType("text/html;charset=UTF-8");

        final Enumeration enumerated = request__.getParameterNames();
        logger.log(Level.SEVERE, "PARAMETERs");
        while (enumerated.hasMoreElements()) {
            String param = (String) enumerated.nextElement();
            logger.info("PARAMETER NAME:" + param);
            logger.info("VALUE:" + request__.getParameter(param));

        }
        final String username = request__.getParameter(Username);/*DO NOT ASSIGN TO NEW STRING. NULL REPRESENTS FIRST ENTRANCE*/
        final String password = request__.getParameter(Password);/*DO NOT ASSIGN TO NEW STRING. NULL REPRESENTS FIRST ENTRANCE*/
        final String email = request__.getParameter(Email);/*DO NOT ASSIGN TO NEW STRING. NULL REPRESENTS FIRST ENTRANCE*/
        final String gender = request__.getParameter(Gender);/*DO NOT ASSIGN TO NEW STRING. NULL REPRESENTS FIRST ENTRANCE*/
        final String dateOfBirth = request__.getParameter(DateOfBirth);/*DO NOT ASSIGN TO NEW STRING. NULL REPRESENTS FIRST ENTRANCE*/

        processSignup:
        {

            if (request__.getSession(false) == null) {
                request__.getRequestDispatcher("/signup.jsp").forward(request__, response__);
                request__.getSession();
                break processSignup;
            }

            /**
             * Remove an existing session
             * 1 user already logged in?
             * 2.returning from signup error(which adds a new session)
             *
             */
            final HttpSession oldUserSession_ = request__.getSession(false);
            if (oldUserSession_ != null) {
                oldUserSession_.removeAttribute(ServletLogin.SBLoggedOnUser);
                oldUserSession_.invalidate();
            }

            /**
             * Now lets maake a new session for this user
             */
            final HttpSession userSession_ = request__.getSession();

            /**
             * Lets leave time for a very inexperienced user to signup.
             * i.e. call her friend, ask how to fill the form. 20 mins is
             * fine.
             */
            userSession_.setMaxInactiveInterval(1200);

            /*Lets set session values in case signup fails and user does not require to fill all fields again.
            Remember though, we will never be sing these values for persisting, as a conventioin.*/
            userSession_.setAttribute(Username, username);
            userSession_.setAttribute(Password, "");
            userSession_.setAttribute(Email, email);
            userSession_.setAttribute(Gender, gender);
            userSession_.setAttribute(DateOfBirth, dateOfBirth);

            Boolean allParametersNotOkFlag = Boolean.FALSE;

            if (username == null || username.equals("") || username.equals("null")) {/*username*/
                userSession_.setAttribute(UsernameError, ErrorStatusTrue);
                userSession_.setAttribute(UsernameErrorMsg, "Did not fill");
                allParametersNotOkFlag = Boolean.TRUE;
            } else if (crudServiceLocal_.find(Human.class, username) != null) {
                userSession_.setAttribute(UsernameError, ErrorStatusTrue);
                userSession_.setAttribute(UsernameErrorMsg, "Sorry! " + username + " is unavailable.");
                allParametersNotOkFlag = Boolean.TRUE;
            }

            /*Do NOT put the password into the session.*/
            if (password == null || password.equals("") || password.equals("null")) {/*password*/
                userSession_.setAttribute(PasswordError, ErrorStatusTrue);
                userSession_.setAttribute(PasswordErrorMsg, "Sorry! You will have to re-enter the password.");
                allParametersNotOkFlag = Boolean.TRUE;
            }
            if (email == null || email.equals("") || email.equals("null")) {/*email*/
                userSession_.setAttribute(EmailError, ErrorStatusTrue);
                userSession_.setAttribute(EmailErrorMsg, "Did not fill");
                allParametersNotOkFlag = Boolean.TRUE;
            }
            if (gender == null || gender.equals("") || gender.equals("null")) {/*gender*/
                userSession_.setAttribute(GenderError, ErrorStatusTrue);
                userSession_.setAttribute(GenderErrorMsg, gender);
                allParametersNotOkFlag = Boolean.TRUE;
            }
            if (dateOfBirth == null || dateOfBirth.equals("") || dateOfBirth.equals("null")) {/*dateofbirth*/
                userSession_.setAttribute(DateOfBirthError, ErrorStatusTrue);
                userSession_.setAttribute(DateOfBirthErrorMsg, "Did not fill");
                allParametersNotOkFlag = Boolean.TRUE;
            }
            if (allParametersNotOkFlag) {
                request__.getRequestDispatcher("/signup.jsp").forward(request__, response__);
                break processSignup;
            }

            final Human newUser = new Human();
            newUser.setHumanId(username);
            final HumansAuthentication ha = new HumansAuthentication();
            ha.setHumanId(newUser.getHumanId());
            ha.setHumanAuthenticationSalt(BCrypt.gensalt());
            ha.setHumanAuthenticationHash(singletonHashingFace.getHash(password, ha.getHumanAuthenticationSalt()));
            newUser.setHumansAuthentications(ha);
            newUser.setHumanAlive(true);

            final HumansIdentity hid = new HumansIdentity();
            hid.setHumanId(newUser.getHumanId());
            newUser.setHumansIdentity(hid);

            final HumansPrivatePhoto hprp = new HumansPrivatePhoto();
            hprp.setHumanId(newUser.getHumanId());
            newUser.setHumansPrivatePhoto(hprp);

            final HumansPublicPhoto hpup = new HumansPublicPhoto();
            hpup.setHumanId(newUser.getHumanId());
            newUser.setHumansPublicPhoto(hpup);

            crudServiceLocal_.create(newUser);

            final Page login = Page.login;
            request__.getRequestDispatcher(login.toString()).forward(request__, response__);
            break processSignup;
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
