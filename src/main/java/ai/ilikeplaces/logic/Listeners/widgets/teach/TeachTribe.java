package ai.ilikeplaces.logic.Listeners.widgets.teach;

import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.Bate;
import ai.ilikeplaces.logic.Listeners.widgets.UserProperty;
import ai.ilikeplaces.logic.contactimports.ImportedContact;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.ServletActivate;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 11/27/11
 * Time: 8:07 AM
 */
public class TeachTribe extends AbstractWidgetListener<TeachTribeCriteria> {
// ------------------------------ FIELDS ------------------------------


// ------------------------------ FIELDS STATIC --------------------------


    private static final String PASSWORD_DETAILS = "_passwordDetails";
    private static final String PASSWORD_ADVICE = "_passwordAdvice";
    private static final String URL = "_url";

// -------------------------- ENUMERATIONS --------------------------

    public static enum TeachTribeIds implements WidgetIds {
        teach_tribe_widget,
        teach_tribe_noti,
        teach_tribe_signed_in,
        teach_tribe_signed_out,
        teach_tribe_signup_section,
        teach_tribe_signup_input,
        teach_tribe_signup_click,
    }

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * @param request__
     * @param teachTribeCriteria
     * @param appendToElement__
     */
    public TeachTribe(final ItsNatServletRequest request__, final TeachTribeCriteria teachTribeCriteria, final Element appendToElement__) {

        super(request__, Controller.Page.TeachTribe, teachTribeCriteria, appendToElement__);
    }

// ------------------------ OVERRIDING METHODS ------------------------

    /**
     * Use this only in conjunction with {@link #AbstractWidgetListener(org.itsnat.core.ItsNatServletRequest, ai.ilikeplaces.servlets.Controller.Page, Object, org.w3c.dom.Element)}
     * GENERIC constructor.
     *
     * @param teachTribeCriteria
     */
    @Override
    protected void init(final TeachTribeCriteria teachTribeCriteria) {

        super.registerUserNotifier($$(TeachTribeIds.teach_tribe_noti));

        ShowHideWidgetAreaBasedOnLoggedInState:
        {
            if (teachTribeCriteria.getHumanId() == null) {
                $$displayBlock($$(TeachTribeIds.teach_tribe_signed_out));
            } else {
                $$displayBlock($$(TeachTribeIds.teach_tribe_signed_in));
            }
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

        super.registerForInputText($$(TeachTribeIds.teach_tribe_signup_input),
                new AIEventListener<TeachTribeCriteria>(criteria) {
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

        super.registerForClick($$(TeachTribeIds.teach_tribe_signup_click),
                new AIEventListener<TeachTribeCriteria>(criteria) {
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
                                try {
                                    final String randomPassword = Long.toHexString(Double.doubleToLongBits(Math.random()));

                                    final Return<Boolean> humanCreateReturn = DB.getHumanCRUDHumanLocal(true).doCHuman(
                                            new HumanId().setObjAsValid(myemail.getObj()),
                                            new Password(randomPassword),
                                            myemail);

                                    if (humanCreateReturn.valid() && humanCreateReturn.returnValue()) {
                                        UserIntroduction.createIntroData(new HumanId(myemail.getObj()));

                                        final String activationURL = new Parameter("http://www.ilikeplaces.com/" + "activate")
                                                .append(ServletLogin.Username, myemail.getObj(), true)
                                                .append(ServletLogin.Password,
                                                        DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(myemail.getObj()))
                                                                .returnValue()
                                                                .getHumanAuthenticationHash())
                                                .append(ServletActivate.NEXT, Controller.Page.Tribes.getURL())
                                                .get();


                                        String htmlBody = Bate.getHTMLStringForOfflineFriendInvite("I Like Places", myemail.getObj());

                                        htmlBody = htmlBody.replace(URL, ElementComposer.generateSimpleLinkTo(activationURL));
                                        htmlBody = htmlBody.replace(PASSWORD_DETAILS, "Your temporary password is " + randomPassword);
                                        htmlBody = htmlBody.replace(PASSWORD_ADVICE, "Make sure you change it.");

                                        SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                                myemail.getObj(),
                                                "I Like Places prides you with an Exclusive Invite!",
                                                htmlBody);

                                        $$displayNone($$(TeachTribeIds.teach_tribe_signup_section));

                                        TeachTribe.this.notifyUser("Great! Check your email now!");

                                        $$sendJSStmt(JSCodeToSend.redirectPageWithURL(Controller.Page.Activate.getURL()));
                                    } else {
                                        TeachTribe.this.notifyUser("Email INVALID!");
                                    }
                                } catch (DBDishonourCheckedException e) {
                                    TeachTribe.this.notifyUser("Email was taken meanwhile!:(");
                                }
                            } else {
                                TeachTribe.this.notifyUser("This email is TAKEN!:(");
                            }
                        } else {
                            TeachTribe.this.notifyUser("Email seems to be wrong :-(");
                        }
                    }
                }
        );
    }
}
