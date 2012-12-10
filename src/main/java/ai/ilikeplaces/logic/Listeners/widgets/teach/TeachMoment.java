package ai.ilikeplaces.logic.Listeners.widgets.teach;

import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.Bate;
import ai.ilikeplaces.logic.Listeners.widgets.PrivateLocationCreate;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.ServletActivate;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import ai.reaver.HumanId;
import ai.reaver.Return;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLDocument;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 11/27/11
 * Time: 8:07 AM
 */
public class TeachMoment extends AbstractWidgetListener<TeachMomentCriteria> {
// ------------------------------ FIELDS ------------------------------


// ------------------------------ FIELDS STATIC --------------------------


    private static final String PASSWORD_DETAILS = "_passwordDetails";
    private static final String PASSWORD_ADVICE = "_passwordAdvice";
    private static final String URL = "_url";
    private String woehint;
    private String placeName;
    private String placeDetails;

// -------------------------- ENUMERATIONS --------------------------

    public static enum TeachMomentIds implements WidgetIds {
        teach_moment_widget,
        teach_moment_noti,
        teach_moment_signed_in,
        teach_moment_signed_out,
        teach_moment_signup_section,
        teach_moment_signup_input,
        teach_moment_signup_click,
    }

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * @param request__
     * @param teachMomentCriteria
     * @param appendToElement__
     */
    public TeachMoment(final ItsNatServletRequest request__, final TeachMomentCriteria teachMomentCriteria, final Element appendToElement__) {

        super(request__, Controller.Page.TeachMoment, teachMomentCriteria, appendToElement__);
    }

// ------------------------ OVERRIDING METHODS ------------------------

    /**
     * Use this only in conjunction with {@link #AbstractWidgetListener(org.itsnat.core.ItsNatServletRequest, ai.ilikeplaces.servlets.Controller.Page, Object, org.w3c.dom.Element)}
     * GENERIC constructor.
     *
     * @param teachMomentCriteria
     */
    @Override
    protected void init(final TeachMomentCriteria teachMomentCriteria) {

        super.registerUserNotifier($$(TeachMomentIds.teach_moment_noti));

        ShowHideWidgetAreaBasedOnLoggedInState:
        {
            if (teachMomentCriteria.getHumanId() == null) {
                $$displayBlock($$(TeachMomentIds.teach_moment_signed_out));
            } else {
                $$displayBlock($$(TeachMomentIds.teach_moment_signed_in));
            }
        }

        GetUrlParametersIfPresent:
        {
            woehint = TeachMoment.this.$$(request, PrivateLocationCreate.WOEHINT);
            placeName = TeachMoment.this.$$(request, PrivateLocationCreate.PLACENAME);
            placeDetails = TeachMoment.this.$$(request, PrivateLocationCreate.PLACEDETAILS);
        }
    }

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the constructor
     *
     * @param itsNatHTMLDocument_
     * @param hTMLDocument_
     */
    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {

        super.registerForInputText($$(TeachMomentIds.teach_moment_signup_input),
                new AIEventListener<TeachMomentCriteria>(criteria) {
                    /**
                     * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
                     *
                     * @param evt fired from client
                     */
                    @Override
                    protected void onFire(Event evt) {

                        criteria.getSignupCriteria().setEmail(new Email(getTargetInputText(evt)));
                    }
                }
        );

        super.registerForClick($$(TeachMomentIds.teach_moment_signup_click),
                new AIEventListener<TeachMomentCriteria>(criteria) {
                    /**
                     * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
                     *
                     * @param evt fired from client
                     */
                    @Override
                    protected void onFire(Event evt) {

                        final Email myemail = criteria.getSignupCriteria().getEmail();

                        if (myemail.valid()) {
                            if (!DB.getHumanCRUDHumanLocal(true).doDirtyCheckHuman(myemail.getObj()).returnValue()) {
                                final String randomPassword = Long.toHexString(Double.doubleToLongBits(Math.random()));

                                final Return<Boolean> humanCreateReturn = DB.getHumanCRUDHumanLocal(true).doCHuman(
                                        new HumanId().setObjAsValid(myemail.getObj()),
                                        new Password(randomPassword),
                                        myemail);

                                if (humanCreateReturn.valid() && humanCreateReturn.returnValue()) {
                                    UserIntroduction.createIntroData(new HumanId(myemail.getObj()));

                                    final Parameter parameter;
                                    if (woehint != null && placeName != null && placeDetails != null) {
                                        parameter = new Parameter("http://www.ilikeplaces.com/page/_org?category=143")
                                                .append(PrivateLocationCreate.WOEHINT,
                                                        woehint)
                                                .append(PrivateLocationCreate.PLACENAME,
                                                        placeName)
                                                .append(PrivateLocationCreate.PLACEDETAILS,
                                                        placeDetails);
                                    } else {
                                        parameter = new Parameter("http://www.ilikeplaces.com/page/_org?category=143");
                                    }

                                    final String activationURL;
                                    try {
                                        activationURL = new Parameter("http://www.ilikeplaces.com/" + "activate")
                                                .append(ServletLogin.Username, myemail.getObj(), true)
                                                .append(ServletLogin.Password,
                                                        DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(myemail.getObj()))
                                                                .returnValue()
                                                                .getHumanAuthenticationHash())
                                                .append(ServletActivate.NEXT, URLEncoder.encode(parameter.toURL(), "UTF8"))
                                                .get();
                                    } catch (UnsupportedEncodingException e) {
                                        throw new RuntimeException(e);
                                    }


                                    String htmlBody = Bate.getHTMLStringForOfflineFriendInvite("I Like Places", myemail.getObj());

                                    htmlBody = htmlBody.replace(URL, ElementComposer.generateSimpleLinkTo(activationURL));
                                    htmlBody = htmlBody.replace(PASSWORD_DETAILS, "Your temporary password is " + randomPassword);
                                    htmlBody = htmlBody.replace(PASSWORD_ADVICE, "Make sure you change it.");

                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                            myemail.getObj(),
                                            "I Like Places prides you with an Exclusive Invite!",
                                            htmlBody);

                                    $$displayNone($$(TeachMomentIds.teach_moment_signup_section));

                                    TeachMoment.this.notifyUser("Great! Check your email now!");

                                    $$sendJSStmt(JSCodeToSend.redirectPageWithURL(Controller.Page.Activate.getURL()));
                                } else {
                                    TeachMoment.this.notifyUser("Email INVALID!");
                                }
                            } else {
                                TeachMoment.this.notifyUser("This email is TAKEN!:(");
                            }
                        } else {
                            TeachMoment.this.notifyUser("Email seems to be wrong :-(");
                        }
                    }
                }
        );
    }

    public static String getCurrentUrl(final HttpServletRequest req) {
        final String contextPath = req.getContextPath();   // /mywebapp
        final String servletPath = req.getServletPath();   // /servlet/MyServlet
        final String pathInfo = req.getPathInfo();         // /a/b;c=123
        final String queryString = req.getQueryString();          // d=789

        // Reconstruct original requesting URL
        String url = contextPath + servletPath;
        if (pathInfo != null) {
            url += pathInfo;
        }
        if (queryString != null) {
            url += "?" + queryString;
        }
        return url;
    }
}
