package ai.ilikeplaces.servlets;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.Return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK(details = "Reviewed:20100620")
final public class ServletSignup extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(ServletSignup.class.getName());

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
    private static final String TRUE = "true";
    final static private String ErrorStatusTrue = TRUE;

    private ResourceBundle logMsgs = ResourceBundle.getBundle("ai.ilikeplaces.rbs.LogMsgs");
    private static final String JS_NULL = "null";
    private static final String EMPTY = "";

    @Override
    @SuppressWarnings("unchecked")
    public void init() {
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request__
     * @param response__
     * @throws ServletException
     * @throws IOException      if an I/O error occurs
     */
    @TODO(task = "GET USERS NAME IN JSP. SET THE OTHER PARAMETERS TAKEN FROM THE USER!")
    protected void processRequest(final HttpServletRequest request__, final HttpServletResponse response__)
            throws ServletException, IOException {

        response__.setContentType("text/html;charset=UTF-8");
        final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");

        final Enumeration enumerated = request__.getParameterNames();
        logger.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletSignup.0003"));
        while (enumerated.hasMoreElements()) {
            String param = (String) enumerated.nextElement();
            logger.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletSignup.0001"), param);
            logger.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletSignup.0002"), request__.getParameter(param).length());

        }
        final String username = request__.getParameter(Username);/*DO NOT ASSIGN TO NEW STRING. NULL REPRESENTS FIRST ENTRANCE*/
        final String password = request__.getParameter(Password);/*DO NOT ASSIGN TO NEW STRING. NULL REPRESENTS FIRST ENTRANCE*/

        processSignup:
        {
            if (!isSignUpPermitted()) {
                request__.getRequestDispatcher("/signup.jsp").forward(request__, response__);
                break processSignup;
            }

            SleepForSpam:
            {
                try {
                    Thread.sleep(signUpSleep());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

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
                oldUserSession_.removeAttribute(ServletLogin.HumanUser);
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
            Remember though, we will never be using these values for persisting, as a conventioin.*/
            userSession_.setAttribute(Username, username);
            userSession_.setAttribute(Password, EMPTY);

            Boolean allParametersNotOkFlag = Boolean.FALSE;
            /**
             * No breaking the block as we need to notify the user all mistakes he did
             */
            validation:
            {
                validate_username:
                {
                    final Email eemail = new Email(username);
                    if (username == null || username.equals(EMPTY) || username.equals(JS_NULL)) {/*username*/
                        userSession_.setAttribute(UsernameError, ErrorStatusTrue);
                        userSession_.setAttribute(UsernameErrorMsg, gUI.getString("ai.ilikeplaces.servlets.ServletSignup.0002"));
                        allParametersNotOkFlag = Boolean.TRUE;
                    } else if ((new Email(username).validate()) != 0) {
                        userSession_.setAttribute(UsernameError, ErrorStatusTrue);
                        userSession_.setAttribute(UsernameErrorMsg, gUI.getString("ai.ilikeplaces.servlets.ServletSignup.0007"));
                        allParametersNotOkFlag = Boolean.TRUE;
                    } else if (DB.getHumanCRUDHumanLocal(true).doDirtyCheckHuman(username).returnValue()) {
                        userSession_.setAttribute(UsernameError, ErrorStatusTrue);
                        userSession_.setAttribute(UsernameErrorMsg, gUI.getString("ai.ilikeplaces.servlets.ServletSignup.0003") + username);
                        allParametersNotOkFlag = Boolean.TRUE;
                    } else {
                        logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletSignup.0006"));
                    }
                }

                validate_password:
                {
                    /*Do NOT put the password into the session.*/
                    if (password == null || password.equals(EMPTY) || password.equals(JS_NULL)) {/*password*/
                        userSession_.setAttribute(PasswordError, ErrorStatusTrue);
                        userSession_.setAttribute(PasswordErrorMsg, gUI.getString("ai.ilikeplaces.servlets.ServletSignup.0004"));
                        allParametersNotOkFlag = Boolean.TRUE;
                    } else if (new Password(password).validate() != 0) {
                        userSession_.setAttribute(PasswordError, ErrorStatusTrue);
                        userSession_.setAttribute(PasswordErrorMsg, gUI.getString("ai.ilikeplaces.servlets.ServletSignup.0008"));
                        allParametersNotOkFlag = Boolean.TRUE;
                    } else {
                        logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletSignup.0007"));
                    }
                }
            }


            if (allParametersNotOkFlag) {
                request__.getRequestDispatcher("/signup.jsp").forward(request__, response__);
                break processSignup;
            }

            try {
                final Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doCHuman(
                        new HumanId().setObjAsValid(username),
                        new Password().setObjAsValid(password),
                        new Email().setObjAsValid(username));
                
                if (r.returnStatus() != 0) {
                    throw r.returnError();
                } else {
                    try {
                        SendMail.getSendMailLocal().sendAsSimpleText(
                                username,
                                gUI.getString("SIGNUP_HEADER"),
                                MessageFormat.format(gUI.getString("SIGNUP_BODY"), RBGet.globalConfig.getString("noti_mail")));
                    } catch (final Throwable t) {
                        Loggers.EXCEPTION.error("{}", t);
                    }
                }
            } catch (
                    @NOTE(note = "COINCIDENCE. ANOTHER USER SIGNED UP IN SAME NAME WHILE THIS USER WAS DOING IT.")
                    final Throwable e) {
                userSession_.setAttribute(UsernameError, ErrorStatusTrue);
                userSession_.setAttribute(UsernameErrorMsg, gUI.getString("ai.ilikeplaces.servlets.ServletSignup.0001") + username);
                request__.getRequestDispatcher("/signup.jsp").forward(request__, response__);
                break processSignup;
            }
            final Page login = Page.login;
            request__.getRequestDispatcher(login.toString()).forward(request__, response__);
            break processSignup;
        }

    }

    final private boolean isSignUpPermitted() {
        return RBGet.getGlobalConfigKey("signUpEnabled") != null && RBGet.getGlobalConfigKey("signUpEnabled").equals(TRUE);
    }

    final private int signUpSleep() {
        return RBGet.getGlobalConfigKey("signUpSleep") != null ? Integer.parseInt(RBGet.getGlobalConfigKey("signUpSleep")) : 1;
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
