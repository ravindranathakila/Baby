package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.contactimports.ImportedContact;
import ai.ilikeplaces.logic.contactimports.google.GoogleContactImporter;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import net.sf.oval.Validator;
import net.sf.oval.exception.ConstraintsViolatedException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class Bate extends AbstractWidgetListener {
    private Email email;
    private Password password;

    private static final String ACCESS_TOKEN = "access_token";
    private static final String HASH = "#";
    private static final String DEFAULT = "default";
    final private Logger logger = LoggerFactory.getLogger(FindFriend.class.getName());

    HumanId humanId;

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
        email = new Email("");
        password = new Password("");

        matches = new Obj<List<HumansIdentity>>(new ArrayList<HumansIdentity>());
        emails = new HashSet<Email>();

        if (((HumanId) initArgs[0]).validate() == 0) {
            this.humanId = ((HumanId) initArgs[0]);
        } else {
            throw new ConstraintsViolatedException(((HumanId) initArgs[0]).getViolations());
        }

        final HumansNetPeople herExistingFriends = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(humanId);//First important variable to notice

        UCAttemptToRecognizeGoogleContactImportRedirect:
        {

            final String authToken = request.getServletRequest().getParameter(ACCESS_TOKEN);

            if (authToken != null) {

                final List<ImportedContact> whoppingImportedContacts;//Second important variable to notice
                Import_Google_Contacts__Add_To_Emails_List:
                {
                    whoppingImportedContacts = GoogleContactImporter.fetchContacts(DEFAULT, authToken);

                    for (final ImportedContact importedContact : whoppingImportedContacts) {//LOOPING A WHOPPING 1000
                        emails.add(new Email(importedContact.getHumanId()));
                    }
                }

                final List<HumansIdentity> existingUsersFromDB;//Third important variable to notice
                final List<String> existingUsers = new ArrayList<String>();//LOOPING 300 Approx
                Get_Users_Existing_Friends__Add_To_Humans_Network:
                {
                    existingUsersFromDB = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentitiesByEmails(new ArrayList<Email>(emails));
                    for (final HumansNetPeople h : herExistingFriends.getHumansNetPeoples()) {
                        existingUsers.add(h.getHumanId());
                    }
                }

                clear($$(Controller.Page.BateImportResults));

                for (final HumansIdentity humansIdentity : existingUsersFromDB) {
                    if (!existingUsers.contains(humansIdentity.getHumanId())) {
                        generateFriendAddWidgetFor(new HumanId(humansIdentity.getHumanId()), humanId);
                    } else {
                        generateFriendDeleteWidgetFor(new HumanId(humansIdentity.getHumanId()), humanId);
                    }
                }

                for (final ImportedContact importedContact : whoppingImportedContacts) {
                    if (!herExistingFriends.getHumansNetPeoples().contains(importedContact)) {
                        new UserProperty(
                                request,
                                $$(Page.BateImportResults),
                                ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText("").get(),
                                new UserProperty.InviteCriteria(importedContact.getFullName(), HASH, HASH, humanId, importedContact.getAsHumanId()) ) {
                        };
                    }
                }
            }
        }

        UCDisplayYouHaventAddedFriendsYet:
        {
            if (herExistingFriends.getHumansNetPeoples().size() == 0) {
                $$(Controller.Page.friendFindSearchNotice).setTextContent("Oops! You haven't added any friends yet. Add your friends to get going!");
            }
        }

        List<String> emails = new ArrayList<String>();
        for (final HumansNetPeople hnp : herExistingFriends.getHumansNetPeoples()) {
            emails.add(hnp.getHumanId());
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

                                    final String activationURL = new Parameter("http://www.ilikeplaces.com/" + "activate")
                                            .append(ServletLogin.Username, myemail.getObj(), true)
                                            .append(ServletLogin.Password,
                                                    DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(myemail.getObj()))
                                                            .returnValue()
                                                            .getHumanAuthenticationHash())
                                            .get();

                                    final String mail = MessageFormat.format(RBGet.gui().getString("SIGNUP_BODY"), RBGet.globalConfig.getString("noti_mail"))
                                            .replace("activationURL", "<a href='" +
                                                    activationURL + "' >" + activationURL + "</a>");
                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                            myemail.getObj(),
                                            RBGet.gui().getString("SIGNUP_HEADER"),
                                            mail);
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

}
