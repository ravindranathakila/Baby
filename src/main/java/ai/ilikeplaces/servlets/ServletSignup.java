package ai.ilikeplaces.servlets;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@FIXME(issue = "XSS")
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
    final static private String ErrorStatusTrue = "true";

    private ResourceBundle logMsgs = ResourceBundle.getBundle("ai.ilikeplaces.rbs.LogMsgs");

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
        final String email = request__.getParameter(Email);/*DO NOT ASSIGN TO NEW STRING. NULL REPRESENTS FIRST ENTRANCE*/
        final String gender = request__.getParameter(Gender);/*DO NOT ASSIGN TO NEW STRING. NULL REPRESENTS FIRST ENTRANCE*/
        final String dateOfBirth = request__.getParameter(DateOfBirth);/*DO NOT ASSIGN TO NEW STRING. NULL REPRESENTS FIRST ENTRANCE*/

        processSignup:
        {

            if(!isSignUpPermitted()){
                request__.getRequestDispatcher("/signup.jsp").forward(request__, response__);
                break processSignup;
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
            userSession_.setAttribute(Password, "");
            userSession_.setAttribute(Email, email);
            userSession_.setAttribute(Gender, gender);
            userSession_.setAttribute(DateOfBirth, dateOfBirth);

            Boolean allParametersNotOkFlag = Boolean.FALSE;
            /**
             * No breaking the block as we need to notify the user all mistakes he did
             */
            validation:
            {
                validate_username:
                {
                    if (username == null || username.equals("") || username.equals("null")) {/*username*/
                        userSession_.setAttribute(UsernameError, ErrorStatusTrue);
                        userSession_.setAttribute(UsernameErrorMsg, gUI.getString("ai.ilikeplaces.servlets.ServletSignup.0002"));
                        allParametersNotOkFlag = Boolean.TRUE;
                    } else if (DB.getHumanCRUDHumanLocal(true).doDirtyCheckHuman(username)) {
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
                    if (password == null || password.equals("") || password.equals("null")) {/*password*/
                        userSession_.setAttribute(PasswordError, ErrorStatusTrue);
                        userSession_.setAttribute(PasswordErrorMsg, gUI.getString("ai.ilikeplaces.servlets.ServletSignup.0004"));
                        allParametersNotOkFlag = Boolean.TRUE;
                    } else {
                        logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletSignup.0007"));
                    }
                }

                validate_email:
                {
                    if (email == null || email.equals("") || email.equals("null")) {/*email*/
                        userSession_.setAttribute(EmailError, ErrorStatusTrue);
                        userSession_.setAttribute(EmailErrorMsg, gUI.getString("ai.ilikeplaces.servlets.ServletSignup.0002"));
                        allParametersNotOkFlag = Boolean.TRUE;
                    } else {
                        logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletSignup.0008"));
                    }
                }

                validate_gender:
                {
                    if (gender == null || gender.equals("") || gender.equals("null")) {/*gender*/
                        userSession_.setAttribute(GenderError, ErrorStatusTrue);
                        userSession_.setAttribute(GenderErrorMsg, gender);
                        allParametersNotOkFlag = Boolean.TRUE;
                    }   else {
                        logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletSignup.0009"));
                    }
                }

                validate_dateOfBirth:
                {
                    if (dateOfBirth == null || dateOfBirth.equals("") || dateOfBirth.equals("null")) {/*dateofbirth*/
                        userSession_.setAttribute(DateOfBirthError, ErrorStatusTrue);
                        userSession_.setAttribute(DateOfBirthErrorMsg, gUI.getString("ai.ilikeplaces.servlets.ServletSignup.0002"));
                        allParametersNotOkFlag = Boolean.TRUE;
                    } else {
                        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
                        try {
                            sdf.parse(dateOfBirth);
                            logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletSignup.0010"));
                            logger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.ServletSignup.0010"));
                        } catch (final ParseException e) {
                            //logger.error("{}", e);
                            userSession_.setAttribute(DateOfBirthError, ErrorStatusTrue);
                            userSession_.setAttribute(DateOfBirthErrorMsg, gUI.getString("ai.ilikeplaces.servlets.ServletSignup.0005"));
                            allParametersNotOkFlag = Boolean.TRUE;
                        }
                    }
                }
            }


            if (allParametersNotOkFlag) {
                request__.getRequestDispatcher("/signup.jsp").forward(request__, response__);
                break processSignup;
            }

            try {
                DB.getHumanCRUDHumanLocal(true).doCHuman(username, password);
            } catch (
                    @NOTE(note = "COINCIDENCE. ANOTHER USER SIGNED UP IN SAME NAME WHILE THIS USER WAS DOING IT.")
                    final IllegalAccessException e) {
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

    final private boolean isSignUpPermitted(){
         return RBGet.getGlobalConfigKey("signUpEnabled") != null && RBGet.getGlobalConfigKey("signUpEnabled").equals("true");
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
