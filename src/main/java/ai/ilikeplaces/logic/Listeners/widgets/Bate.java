package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.contactimports.ImportedContact;
import ai.ilikeplaces.logic.contactimports.google.GoogleContactImporter;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.logic.validators.unit.SimpleName;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import com.google.gdata.data.Person;
import net.sf.oval.Validator;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import javax.xml.transform.TransformerException;
import java.text.MessageFormat;
import java.util.*;

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
    private static final String PASSWORD_DETAILS = "_passwordDetails";
    private static final String PASSWORD_ADVICE = "_passwordAdvice";
    private static final String URL = "_url";
    private Email email;
    private Password password;

    private static final String ACCESS_TOKEN = "access_token";
    private static final String HASH = "#";
    private static final String DEFAULT = "default";
    final private Logger logger = LoggerFactory.getLogger(FindFriend.class.getName());

    HumanId humanId;
    SimpleName humansName;

    RefObj<List<HumansIdentity>> matches = null;

    Set<Email> emails;


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
                        generateFriendInviteWidgetFor(importedContact, humanId, humansName);
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
                            try {

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

                                    htmlBody = htmlBody.replace(URL, activationURL);
                                    htmlBody = htmlBody.replace(PASSWORD_ADVICE, "");
                                    htmlBody = htmlBody.replace(PASSWORD_DETAILS, "");

                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                            myemail.getObj(),
                                            "I Like Places prides you with an Exclusive Invite!",
                                            htmlBody);

                                    $$sendJSStmt(JSCodeToSend.redirectPageWithURL(Controller.Page.Activate.getURL()));
                                }

                            } catch (DBDishonourCheckedException e) {
                                $$(Controller.Page.BateSignupNotifications).setTextContent("Email was taken meanwhile!:(");
                            }
                        } else {
                            $$(Controller.Page.BateSignupNotifications).setTextContent("This email is TAKEN!:(");
                        }
                    } else {
                        //Ignored as the individual validators would've reported the error by now
                    }

                }
            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
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
                new UserProperty.InviteCriteria(
                        importedContact.getFullName(),//This name is of the person being invited(invitee), not the inviter
                        "#",
                        "#",
                        currentUser,
                        importedContact)) {

            protected void init(final Object... initArgs) {

                new Button(
                        request,
                        $$(Page.user_property_content),
                        (new ButtonCriteria(false, "Invite", "#", "padding-left:0%; width:20%; padding-right:80%;")
                                .setMetadata(
                                        ((InviteCriteria) initArgs[1]).getInviter(),
                                        ((InviteCriteria) initArgs[1]).getInvitee(),
                                        ((InviteCriteria) initArgs[1]).getDisplayName()))) {

                    private HumanId inviter;
                    private ImportedContact invitee;
                    private String invitersName;

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

                            @Override
                            public void handleEvent(final Event evt) {

                                try {

                                    final String randomPassword = Long.toHexString(Double.doubleToLongBits(Math.random()));

                                    final Return<Boolean> humanCreateReturn = DB.getHumanCRUDHumanLocal(true).doCHuman(
                                            new HumanId().setObjAsValid(myinvitee.getEmail()),
                                            new Password(randomPassword),
                                            new Email(myinvitee.getEmail()));

                                    UserIntroduction.createIntroData(new HumanId(myinvitee.getEmail()));

                                    final String activationURL = new Parameter("http://www.ilikeplaces.com/" + "activate")
                                            .append(ServletLogin.Username, myinvitee.getEmail(), true)
                                            .append(ServletLogin.Password,
                                                    DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(myinvitee.getEmail()))
                                                            .returnValue()
                                                            .getHumanAuthenticationHash())
                                            .get();


                                    String htmlBody = getHTMLStringForOfflineFriendInvite(myinvitersName, myinvitee.getFullName());

                                    htmlBody = htmlBody.replace(URL, activationURL);
                                    htmlBody = htmlBody.replace(PASSWORD_DETAILS, "Your temporary password is " + randomPassword);
                                    htmlBody = htmlBody.replace(PASSWORD_ADVICE, "Make sure you change it. ");

                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                            myinvitee.getHumanId(),
                                            "Invitation from " + myinvitersName,
                                            htmlBody);
                                } catch (final Throwable t) {
                                    Loggers.error("Error sending email", t);
                                }

                                $$(Page.GenericButtonText).setTextContent("Invited!");

                                $$remove(evt, EventType.CLICK, this);
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
    final String getHTMLStringForOfflineFriendInvite(final String inviter, final String invitee) {
        try {

            final Document document = HTMLDocParser.getDocument(Controller.REAL_PATH + Controller.WEB_INF_PAGES + Controller.USER_PROPERTY_EMAIL_XHTML);

            $$(Controller.Page.user_property_name, document).setTextContent(inviter);
            $$(Controller.Page.user_property_name, document).setAttribute(MarkupTag.A.href(), "http://www.ilikeplaces.com");
            $$(Controller.Page.user_property_profile_photo, document).setAttribute(MarkupTag.IMG.src(), RBGet.getGlobalConfigKey("PROFILE_PHOTO_DEFAULT"));
            $$(Controller.Page.user_property_content, document).appendChild(
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


            return HTMLDocParser.convertNodeToHtml($$(Page.user_property_widget, document));
        } catch (final Throwable e) {
            throw LogNull.getRuntimeException(e);
        }

    }

}
