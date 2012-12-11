package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.contactimports.ImportedContact;
import ai.ilikeplaces.logic.contactimports.google.GoogleContactImporter;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.logic.validators.unit.SimpleName;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import ai.reaver.Return;
import ai.reaver.ReturnImpl;
import ai.scribble.License;
import com.google.gdata.data.Person;
import net.sf.oval.Validator;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * You know, having everything on one place promotes readability. This class is full of inner classes because of that.
 * However, if you see it as a mess, you are probably not the "get the overall picture" type. So do some refactoring.
 * Use a good IDE and move the abstract classes to separate ones. I'd go with IntellJ. Good luck. God speed!
 * <p/>
 * Oh one more thing, varargs contructurs and alike were a design decision made at the very begining of ilikeplaces to
 * promote agility. It has helped a lot since. Still helps. So learn to deal with it.
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class Bate extends AbstractWidgetListener {
    public static final String PASSWORD_DETAILS = "_passwordDetails";
    public static final String PASSWORD_ADVICE = "_passwordAdvice";
    private static final String URL = "_url";
    private static final String OMG_GOOGLE_INVITE = "omg_gi";
    private Email email;
    private Password password;

    private static final String ACCESS_TOKEN = "access_token";
    private static final String HASH = "#";
    public static final String DEFAULT = "default";
    final private Logger logger = LoggerFactory.getLogger(FindFriend.class.getName());

    HumanId humanId;
    SimpleName humansName;

    RefObj<List<HumansIdentity>> matches = null;

    Set<Email> emails;

    /**
     * Thank you for not refactoring to "inviteCount" and not confusing the coder that it is the number of invites
     * that a user has available instead of the number of invites he sent.
     */
    private Integer invitedCount = 0;

    private Element bate = null;
    private ItsNatHttpSession userSession;


    /**
     * @param request__
     * @param appendToElement__
     * @param initArgs
     */
    public Bate(final ItsNatServletRequest request__, final Element appendToElement__, final Object... initArgs) {
        super(request__, Page.Bate, appendToElement__, initArgs);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        $$displayNone($(Controller.Page.CPageNotice));
        matches = new Obj<List<HumansIdentity>>(new ArrayList<HumansIdentity>());
        emails = new HashSet<Email>();
        email = new Email("");
        password = new Password("");
        bate = $$(Controller.Page.BateSignup);

        userSession = ((ItsNatHttpSession) request.getItsNatSession());

        final boolean omged = userSession.getAttribute(OMG_GOOGLE_INVITE) != null && (Boolean) userSession.getAttribute(OMG_GOOGLE_INVITE);


        UCAttemptToRecognizeGoogleContactImportRedirect:
        {

            final String authToken = request.getServletRequest().getParameter(ACCESS_TOKEN);

            if (authToken != null) {

                final List<ImportedContact> whoppingImportedContacts;//Second important variable to notice
                Import_Google_Contacts__Add_To_Emails_List:
                {
                    Pair<Person, List<ImportedContact>> emailListPair = GoogleContactImporter.fetchContactsWithAuthor(DEFAULT, authToken);
                    this.humanId = new HumanId(emailListPair.getKey().getEmail());
                    this.humansName = new SimpleName(emailListPair.getKey().getName());
                    whoppingImportedContacts = emailListPair.getValue();

                    for (final ImportedContact importedContact : whoppingImportedContacts) {//LOOPING A WHOPPING 1000
                        emails.add(new Email(importedContact.getHumanId()));
                    }
                }

                final List<HumansIdentity> existingUsersFromDB;//Third important variable to notice
                final List<String> existingUsers = new ArrayList<String>();//LOOPING 300
                Get_Existing_Users:
                {
                    existingUsersFromDB = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentitiesByEmails(new ArrayList<Email>(emails));
                }

                clear($$(Page.BateImportResults));
                $$displayNone($$(Controller.Page.BateIntroduction));

                for (final ImportedContact importedContact : whoppingImportedContacts) {
                    if (!existingUsersFromDB.contains(importedContact)) {
                        if (!omged) {
                            generateFriendInviteWidgetFor(importedContact, humanId, humansName);
                        } else {
                            UCDisplayOmgSuccessMessage:
                            {
                                $$displayBlock($$(Controller.Page.BateOmgSuccessMsg));
                            }
                            UCSendInviteMails:
                            {
                                final Return<Boolean> booleanReturn = sendInviteToOfflineInvite(humansName.getObjectAsValid(), importedContact);
                            }
                            UCCleaningUpForNextTimeIfEver:
                            {
                                userSession.setAttribute(OMG_GOOGLE_INVITE, false);
                            }
                            UCNotifyAndDisplaySignupWidget:
                            {
                                $$displayBlock(bate);
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        UCSignup:
        {
            itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.BateSignupEmail), EventType.BLUR.toString(), new EventListener() {

                final Validator v = new Validator();
                final Email myemail = email;

                @Override
                public void handleEvent(final Event evt_) {
                    myemail.setObj(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.INPUT.value()));
                    if (myemail.validate(v) == 0) {
                        $$(Controller.Page.BateSignupNotifications).setTextContent("Email Valid!");
                    } else {
                        $$(Controller.Page.BateSignupNotifications).setTextContent("Email INVALID!");
                    }
                }
            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

            itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.BateSignupPassword), EventType.BLUR.toString(), new EventListener() {

                final Validator v = new Validator();
                final Password mypassword = password;

                @Override
                public void handleEvent(final Event evt_) {
                    mypassword.setObj(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.INPUT.value()));
                    if (mypassword.validate(v) == 0) {
                        $$(Controller.Page.BateSignupNotifications).setTextContent("Password Valid!");
                    } else {
                        $$(Controller.Page.BateSignupNotifications).setTextContent("Password INVALID!");
                    }
                }
            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

            itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.BateSignupButton), EventType.CLICK.toString(), new EventListener() {

                final Validator v = new Validator();
                final Email myemail = email;
                final Password mypassword = password;

                @Override
                public void handleEvent(final Event evt_) {
                    if (myemail.validate(v) == 0 && mypassword.validate(v) == 0) {
                        if (!DB.getHumanCRUDHumanLocal(true).doDirtyCheckHuman(myemail.getObj()).returnValue()) {

                            final Return<Boolean> humanCreateReturn = DB.getHumanCRUDHumanLocal(true).doCHuman(
                                    new HumanId().setObjAsValid(email.getObj()),
                                    mypassword,
                                    myemail);

                            if (humanCreateReturn.returnValue()) {

                                UserIntroduction.createIntroData(new HumanId(email.getObj()));

                                final String activationURL = new Parameter("http://www.ilikeplaces.com/" + "activate")
                                        .append(ServletLogin.Username, myemail.getObj(), true)
                                        .append(ServletLogin.Password,
                                                DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(myemail.getObj()))
                                                        .returnValue()
                                                        .getHumanAuthenticationHash())
                                        .get();


                                String htmlBody = getHTMLStringForOfflineFriendInvite("I Like Places", email.getObj());

                                htmlBody = htmlBody.replace(URL, ElementComposer.generateSimpleLinkTo(activationURL));
                                htmlBody = htmlBody.replace(PASSWORD_ADVICE, "");
                                htmlBody = htmlBody.replace(PASSWORD_DETAILS, "");

                                SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                        myemail.getObj(),
                                        "I Like Places prides you with an Exclusive Invite!",
                                        htmlBody);

                                $$sendJSStmt(JSCodeToSend.redirectPageWithURL(Controller.Page.Activate.getURL()));
                            }

                        } else {
                            $$(Controller.Page.BateSignupNotifications).setTextContent("This email is TAKEN!:(");
                        }
                    } else {
                        //Ignored as the individual validators would've reported the error by now
                    }

                }
            }, false);
        }

        UCOmg:
        {
            itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.BateOmg), EventType.CLICK.toString(), new EventListener() {

                final Validator v = new Validator();
                final Password mypassword = password;
                final ItsNatHttpSession myuserSession = userSession;

                @Override
                public void handleEvent(final Event evt_) {
                    myuserSession.setAttribute(OMG_GOOGLE_INVITE, Boolean.TRUE);
                }
            }, false);
        }
    }


    private void generateFriendDeleteWidgetFor(final HumanId humanIdWhosProfileToShow, final HumanId currentUser) {
        new UserProperty(request, $$(Page.friendFindSearchResults), humanIdWhosProfileToShow, currentUser) {
            protected void init(final Object... initArgs) {
                new FriendDelete(request, $$(Page.user_property_content), (HumanId) initArgs[0], (HumanId) ((Object[]) initArgs[1])[0]) {//This var args thingy is an awesome way to flexibility, except when not careful!
                };
            }
        };
    }

    private void generateFriendAddWidgetFor(final HumanId humanIdWhosProfileToShow, final HumanId currentUser) {
        new UserProperty(request, $$(Page.friendFindSearchResults), humanIdWhosProfileToShow, currentUser) {
            protected void init(final Object... initArgs) {
                new FriendAdd(request, $$(Page.user_property_content), (HumanId) initArgs[0], (HumanId) ((Object[]) initArgs[1])[0]) {//This var args thingy is an awesome way to flexibility, except when not careful!
                };
            }
        };
    }


    private void generateFriendInviteWidgetFor(final ImportedContact importedContact, final HumanId currentUser, final SimpleName humansName) {
        new UserProperty(
                request,
                $$(Page.BateImportResults),
                ElementComposer.compose($$(MarkupTag.BR)).get(),
                new InviteCriteria(
                        humansName.getObjectAsValid(),//This name is of the person being invited(invitee), not the inviter
                        "#",
                        "#",
                        currentUser,
                        importedContact)) {

            private Integer myinvitedCount = invitedCount;
            private Element mybate = bate;

            protected void init(final Object... initArgs) {

                final InviteCriteria myInviteCritria = (InviteCriteria) initArgs[1];
                new Button(
                        request,
                        $$(Page.user_property_content),
                        (new ButtonCriteria(false, "Invite", "#", "padding-left:0%; width:20%; padding-right:80%;")
                                .setMetadata(
                                        myInviteCritria.getInviter(),
                                        myInviteCritria.getInvitee(),
                                        myInviteCritria.getInviterDisplayName()))) {

                    private HumanId inviter;
                    private ImportedContact invitee;
                    private String invitersName;
                    private Integer mymyinvitedCount = myinvitedCount;
                    private Element mymybate = mybate;


                    @Override
                    protected void init(final ButtonCriteria buttonCriteria) {
                        inviter = (HumanId) buttonCriteria.getMetadata()[0];
                        invitee = (ImportedContact) buttonCriteria.getMetadata()[1];
                        invitersName = (String) buttonCriteria.getMetadata()[2];
                    }

                    @Override
                    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {

                        itsNatHTMLDocument_.addEventListener((EventTarget) $$(Page.GenericButtonLink), EventType.CLICK.toString(), new EventListener() {
                            private HumanId myinviter = inviter;
                            private ImportedContact myinvitee = invitee;
                            private String myinvitersName = invitersName;
                            private Integer mymymyinvitedCount = mymyinvitedCount;
                            private Element mymymybate = mymybate;


                            @Override
                            public void handleEvent(final Event evt) {

                                final Return<Boolean> booleanReturn = sendInviteToOfflineInvite(myinvitersName, myinvitee);

                                if (booleanReturn.returnStatus() == 0 && booleanReturn.returnValue()) {
                                    /**
                                     * What happens if I do mymymyinvitedCount++ ? Will the referenc be updated? or do I get a new int? No time to check that :)
                                     */
                                    mymymyinvitedCount = mymymyinvitedCount + 1;

                                    if (mymymyinvitedCount > 1) {
                                        $$displayBlock(mymymybate);
                                    }

                                    $$(Page.GenericButtonText).setTextContent("Invited!");

                                    $$remove(evt, EventType.CLICK, this);
                                } else {
                                    $$(Page.GenericButtonText).setTextContent("Failed! Retry..");
                                }
                            }
                        }, false);
                    }
                };
            }

            @Override
            protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

                itsNatHTMLDocument_.addEventListener((EventTarget) $$(Page.user_property_content), EventType.CLICK.toString(), new EventListener() {

                    @Override
                    public void handleEvent(final Event evt) {
                        //$$displayNone($$(Page.user_property_widget));
                        $$sendJS(JSCodeToSend.jqueryHide($$getId(Page.user_property_widget)));
                    }
                }, false);

            }
        };
    }

    /**
     * Offline, implies the inviter is offline
     *
     * @param inviter
     * @param invitee
     * @return
     */
    final static public String getHTMLStringForOfflineFriendInvite(final String inviter, final String invitee) {
        try {

            final Document document = HTMLDocParser.getDocument(Controller.REAL_PATH + Controller.WEB_INF_PAGES + Controller.USER_PROPERTY_EMAIL_XHTML);

            $$static(Controller.Page.user_property_name, document).setTextContent(inviter);
            $$static(Controller.Page.user_property_name, document).setAttribute(MarkupTag.A.href(), "http://www.ilikeplaces.com");
            $$static(Controller.Page.user_property_profile_photo, document).setAttribute(MarkupTag.IMG.src(), RBGet.getGlobalConfigKey("PROFILE_PHOTO_DEFAULT"));
            $$static(Controller.Page.user_property_content, document).appendChild(
                    document.importNode(
                            ElementComposer.compose(
                                    document.createElement(MarkupTag.DIV.toString())
                            ).$ElementSetText(
                                    "Hey! " + inviter + " has just invited you to I LIKE PLACES! " +
                                            "The website is for meeting people you care at interesting places. " +
                                            "In it, you can find interesting places and organize moments with your friends and family. " +
                                            "You can join I Like Places only through an invite. " +
                                            "Now that " + inviter + " has gotten you in, use the following link to access I Like Places. " +
                                            URL + " . " +
                                            PASSWORD_DETAILS + " " +
                                            PASSWORD_ADVICE + " " +
                                            "All the best and Have Fun! "
                            ).getAsNode(),
                            true)
            );


            return HTMLDocParser.convertNodeToHtml($$static(Page.user_property_widget, document));
        } catch (final Throwable e) {
            throw LogNull.getRuntimeException(e);
        }

    }

    final static public Return<Boolean> sendInviteToOfflineInvite(final String invitersName, final ImportedContact inviteee) {
        Return<Boolean> returnVal;
        try {
            final String randomPassword = getRandomPassword();

            final Return<Boolean> humanCreateReturn = DB.getHumanCRUDHumanLocal(true).doCHuman(
                    new HumanId().setObjAsValid(inviteee.getEmail()),
                    new Password(randomPassword),
                    new Email(inviteee.getEmail()));

            if (humanCreateReturn.valid() && humanCreateReturn.returnValue()) {

                UserIntroduction.createIntroData(new HumanId(inviteee.getEmail()));

                final String activationURL = new Parameter("http://www.ilikeplaces.com/" + "activate")
                        .append(ServletLogin.Username, inviteee.getEmail(), true)
                        .append(ServletLogin.Password,
                                DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(inviteee.getEmail()))
                                        .returnValue()
                                        .getHumanAuthenticationHash())
                        .get();


                String htmlBody = getHTMLStringForOfflineFriendInvite(invitersName, inviteee.getFullName());

                htmlBody = htmlBody.replace(URL, ElementComposer.generateSimpleLinkTo(activationURL));
                htmlBody = htmlBody.replace(PASSWORD_DETAILS, "Your temporary password is " + "\"" + randomPassword + "\"" + "(without quotes)");
                htmlBody = htmlBody.replace(PASSWORD_ADVICE, "Make sure you change it. ");

                final Return<Boolean> mailReturn = SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                        inviteee.getHumanId(),
                        "Invitation from " + invitersName,
                        htmlBody);
                returnVal = new ReturnImpl<Boolean>(true, "User Creation and Email Send Successful!");
            } else {
                returnVal = new ReturnImpl<Boolean>(humanCreateReturn.returnError(), "User Creation and Email Send FAILED!", false);
            }
        } catch (final Throwable t) {
            returnVal = new ReturnImpl<Boolean>(t, "User Creation and Email Send FAILED!", true);
        }
        return returnVal;

    }

    public static final String getRandomPassword() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

}
