package ai.ilikeplaces.servlets;

import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.logic.Listeners.widgets.Bate;
import ai.ilikeplaces.logic.Listeners.widgets.WallWidgetTribe;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.role.HumanUser;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.ilikeplaces.util.*;
import ai.reaver.HumanId;
import ai.reaver.Return;
import ai.reaver.ReturnImpl;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 11/13/11
 * Time: 8:03 PM
 */
public class ServletMobile extends HttpServlet {


    private static final String URL = "url";


    protected void processRequest(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession activeSession = req.getSession(true);

        try {

            final Map parameterMap = req.getParameterMap();


            if (parameterMap.containsKey("login")) {

                final HumanId email = new HumanId((((String[]) parameterMap.get("email"))[0]));
                final Password password = new Password((((String[]) parameterMap.get("password"))[0]));

                final Human existingUser = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(email.getObjectAsValid());

                if (existingUser != null && existingUser.getHumanAlive()) {/*Ok user name valid but now we check for password*/

                    final HumansAuthentication humansAuthentication = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(email).returnValue();

                    final String hash = humansAuthentication.getHumanAuthenticationHash();
                    final String salt = humansAuthentication.getHumanAuthenticationSalt();

                    final Gson gson = new Gson();


                    if (hash.equals(DB.getSingletonHashingFaceLocal(true).getHash(password.getObjectAsValid(), salt))) {
                        final HumanUserLocal humanUserLocal = HumanUser.getHumanUserLocal(true);

                        humanUserLocal.setHumanUserId(email.getObjectAsValid());

                        activeSession.setAttribute(HumanUserLocal.NAME, (new SessionBoundBadRefWrapper<HumanUserLocal>(humanUserLocal, activeSession)));

                        activeSession.setAttribute(ServletLogin.Username, existingUser.getHumanId());


                        resp.getWriter().flush();
                        resp.getWriter().append(gson.toJson(true));
                    } else {
                        resp.getWriter().flush();
                        resp.getWriter().append(gson.toJson(false));
                    }
                }


            } else if (parameterMap.containsKey("signup")) {

                final HumanId myemail = new HumanId((((String[]) parameterMap.get("email"))[0]));
                final Password mypassword = new Password((((String[]) parameterMap.get("password"))[0]));

                Return<Boolean> humanCreateReturn = new ReturnImpl<Boolean>(false, "Could not create account");

                if (myemail.validate() == 0 && mypassword.validate() == 0) {
                    if (!DB.getHumanCRUDHumanLocal(true).doDirtyCheckHuman(myemail.getObj()).returnValue()) {

                        humanCreateReturn = DB.getHumanCRUDHumanLocal(true).doCHuman(
                                new HumanId().setObjAsValid(myemail.getObj()),
                                mypassword,
                                myemail);

                        if (humanCreateReturn.valid() && humanCreateReturn.returnValue()) {

                            UserIntroduction.createIntroData(new HumanId(myemail.getObj()));

                            final String activationURL = new Parameter("http://www.ilikeplaces.com/" + "activate")
                                    .append(ServletLogin.Username, myemail.getObj(), true)
                                    .append(ServletLogin.Password,
                                            DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(myemail.getObj()))
                                                    .returnValue()
                                                    .getHumanAuthenticationHash())
                                    .get();


                            String htmlBody = Bate.getHTMLStringForOfflineFriendInvite("I Like Places", myemail.getObj());

                            htmlBody = htmlBody.replace(URL, ElementComposer.generateSimpleLinkTo(activationURL));
                            htmlBody = htmlBody.replace(Bate.PASSWORD_ADVICE, "");
                            htmlBody = htmlBody.replace(Bate.PASSWORD_DETAILS, "");

                            SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                    myemail.getObj(),
                                    "I Like Places prides you with an Exclusive Invite!",
                                    htmlBody);

                            //$$sendJSStmt(JSCodeToSend.redirectPageWithURL(Controller.Page.Activate.getURL()));
                        } else {
                            //$$(Controller.Page.DownTownHeatMapSignupNotifications).setTextContent("Email INVALID!");
                        }

                    } else {
                        //$$(Controller.Page.DownTownHeatMapSignupNotifications).setTextContent("This email is TAKEN!:(");
                    }

                    final Gson gson = new Gson();

                    resp.getWriter().flush();
                    resp.getWriter().append(gson.toJson(humanCreateReturn.valid() && humanCreateReturn.returnValue()));
                }


            } else {//Not a login request, Not a sign up Request, so the user either should be logged in or client programmer did a mistake


                final Object attribute__ = activeSession.getAttribute(ServletLogin.HumanUser);

                final SessionBoundBadRefWrapper<HumanUserLocal> sessionBoundBadRefWrapper = getEJBSession(attribute__);

                final HumanUserLocal humanUserAsValid = HumanUser.getHumanUserAsValid(sessionBoundBadRefWrapper);


                final HumanId humanId = humanUserAsValid.getHumanId();


                if (parameterMap.containsKey("add")) {

                    final HumanId email = new HumanId((((String[]) parameterMap.get("email"))[0]));
                    final VLong tribeId = new VLong(Long.parseLong(((String[]) parameterMap.get("tribe"))[0]));

                    final Return<Tribe> returnVal = DB.getHumanCRUDTribeLocal(false).addToTribe(email, tribeId);

                    final Gson gson = new Gson();

                    resp.getWriter().flush();
                    resp.getWriter().append(gson.toJson(returnVal.valid()));

                } else if (parameterMap.containsKey("tribe")) {
                    final Tribe tribe = DB.getHumanCRUDTribeLocal(false).getTribe(humanId,
                            new VLong(Long.parseLong(((String[]) parameterMap.get("tribe"))[0])));

                    final Gson gson = new Gson();

                    resp.getWriter().flush();
                    resp.getWriter().append(gson.toJson(tribe));
                } else if (parameterMap.containsKey("wall")) {
                    final VLong wallId = new VLong(Long.parseLong(((String[]) parameterMap.get("wall"))[0]));
                    final RefreshSpec refreshSpec = WallWidgetTribe.REFRESH_SPEC;

                    Loggers.INFO.info("" + wallId.getObjectAsValid());
                    Loggers.INFO.info("" + refreshSpec.toString());
                    Loggers.INFO.info("" + humanId);

                    final Wall wall = DB.getHumanCRUDTribeLocal(false).readWall(
                            humanId,
                            wallId,
                            refreshSpec
                    ).returnValueBadly();

                    final Gson gson = new Gson();

                    resp.getWriter().flush();
                    resp.getWriter().append(gson.toJson(wall));
                } else {
                    resp.getWriter().flush();
                    final Set<Tribe> tribes = DB.getHumanCRUDTribeLocal(false).getHumansTribesAsSet(humanId);
                    resp.getWriter().append((new Gson().toJson(tribes)));
                }
            }
        } catch (final Throwable t) {
            resp.getWriter().flush();
            resp.getWriter().append(t.getMessage());
        }

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


    private SessionBoundBadRefWrapper<HumanUserLocal> getEJBSession(Object attribute__) {
        return attribute__ == null ?
                null : (!((SessionBoundBadRefWrapper<HumanUserLocal>) attribute__).isAlive() ?
                null : ((SessionBoundBadRefWrapper<HumanUserLocal>) attribute__));
    }
}
