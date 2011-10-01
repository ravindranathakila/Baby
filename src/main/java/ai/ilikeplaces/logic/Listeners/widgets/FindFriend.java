package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.contactimports.ImportedContact;
import ai.ilikeplaces.logic.contactimports.google.GoogleContactImporter;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import net.sf.oval.exception.ConstraintsViolatedException;
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

import java.util.*;


/**
 * Why the fuck I used {@link ai.ilikeplaces.entities.HumansNet} on this class I have no idea.
 * It fucking uses up a lot of memory.
 * Judging by my previous rants, I'm sure there was a reason. But I still don't get it.
 * I write this at the time I'm trying to hook up contact imports with existing users on ilikeplaces.com .
 * <p/>  <br/>
 * <b>Okay, lets clear this out.  <br/>
 * There are 3 kinds. Basically,    <br/>
 * Existing on ilikeplaces and her friend <br/>
 * Existing on ilikeplaces not her friend   <br/>
 * Not existing on ilikeplaces  <br/> <br/>
 * </b>
 * <p/><br/>
 * It seems we are fetching her existing friends each time she does a search. This is OK, but can be cached on the widget.
 * I'm sure at the time of writing this code I was so poor I decided reading from the DB to RAM each time is better than
 * caching it. I never checked Memory $ vs CPU cycles $. It's too late for either. Let's fix the code as we go.
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class FindFriend extends AbstractWidgetListener {

    private static final String PASSWORD_DETAILS = "_passwordDetails";
    private static final String PASSWORD_ADVICE = "_passwordAdvice";
    private static final String URL = "_url";

    private static final String ACCESS_TOKEN = "access_token";
    private static final String HASH = "#";
    private static final String DEFAULT = "default";
    final private Logger logger = LoggerFactory.getLogger(FindFriend.class.getName());

    HumanId humanId;

    HumansIdentity humansIdentity;

    RefObj<List<HumansIdentity>> matches = null;

    Set<Email> emails;


    public FindFriend(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanId) {
        super(request__, Page.FindFriend, appendToElement__, humanId);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        matches = new Obj<List<HumansIdentity>>(new ArrayList<HumansIdentity>());
        emails = new HashSet<Email>();

        if (((HumanId) initArgs[0]).validate() == 0) {
            this.humanId = ((HumanId) initArgs[0]);
        } else {
            throw new ConstraintsViolatedException(((HumanId) initArgs[0]).getViolations());
        }

        humansIdentity = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(humanId).returnValueBadly();

        final HumansNetPeople herExistingFriends = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(humanId);//First important variable to notice

        UCAttemptToRecognizeGoogleContactImportRedirect:
        {

            final String authToken = request.getServletRequest().getParameter(ACCESS_TOKEN);

            if (authToken != null) {

                final List<ImportedContact> whoppingImportedContacts;//Second important variable to notice
                Import_Google_Contacts__Add_To_Emails_List:
                {
                    whoppingImportedContacts = GoogleContactImporter.fetchContacts(DEFAULT, authToken).getValue();

                    for (final ImportedContact importedContact : whoppingImportedContacts) {//LOOPING A WHOPPING 1000
                        emails.add(new Email(importedContact.getHumanId()));
                    }
                }

                final List<HumansIdentity> existingUsersFromDB;//Third important variable to notice
                final List<String> existingUsers = new ArrayList<String>();//LOOPING 300
                Get_Users_Existing_Friends__Add_To_Humans_Network:
                {
                    existingUsersFromDB = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentitiesByEmails(new ArrayList<Email>(emails));
                    for (final HumansNetPeople h : herExistingFriends.getHumansNetPeoples()) {
                        existingUsers.add(h.getHumanId());
                    }
                }

                clear($$(Controller.Page.friendFindSearchResults));

                for (final HumansIdentity humansIdentity : existingUsersFromDB) {
                    if (!existingUsers.contains(humansIdentity.getHumanId())) {
                        generateFriendAddWidgetFor(new HumanId(humansIdentity.getHumanId()), humanId);
                    } else {
                        generateFriendDeleteWidgetFor(new HumanId(humansIdentity.getHumanId()), humanId);
                    }
                }

                for (final ImportedContact importedContact : whoppingImportedContacts) {
                    if (!herExistingFriends.getHumansNetPeoples().contains(importedContact)) {
                        generateFriendInviteWidgetFor(importedContact, humanId);
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

        UCDisplayExistingUserMails:
        {
            /**
             * Normal approach
             */
            $$(Controller.Page.friendFindSearchTextInput).setAttribute(MarkupTag.TEXTAREA.value(), Arrays.toString(emails.toArray()));

            /**
             * For chrome!
             */
            $$(Controller.Page.friendFindSearchTextInput).setTextContent(Arrays.toString(emails.toArray()));
        }

        UCDisplayExistingUsers:
        {
            //clear($$(Controller.Page.friendFindSearchResults));


            for (final HumansNetPeople friend : herExistingFriends.getHumansNetPeoples()) {

                generateFriendDeleteWidgetFor(new HumanId(friend.getHumanId()), humanId);

            }

        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {

        UCProcessUserEmailInput:
        {

            itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.friendFindSearchTextInput), EventType.BLUR.toString(), new EventListener() {

                private HumanId myhumanId = humanId;
                private RefObj<List<HumansIdentity>> mymatches = matches;
                private Set<Email> myemails = emails;

                @Override
                public void handleEvent(final Event evt_) {
                    final String emailString = ((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value());
                    final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.DEBUG, "Processing email string " + emailString, 60000, null, true);
                    myemails.clear();
                    myemails.addAll(Email.emails(emailString));
                    final List<HumansIdentity> existingUsers = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentitiesByEmails(new ArrayList<Email>(myemails));
                    UCExistingFriendAdd:
                    {
                        mymatches.setObj(existingUsers);
                    }

                    /**
                     * remember, multiple entries in browser mailto "to" parameter separated by commas,
                     * doesn't work in some browser though they appear to support it.
                     * They in fact don't send the mail.
                     */
                    UCNonExistingFriendInvite:
                    {
                        /**
                         * Just include subject, and body.
                         */
                        UCSimpleMailToLink:
                        {
                            //This is there in the template
                        }

                        /**
                         * Invite each friend individually link.
                         */
                        UCIndividualMailToLink:
                        {
                            //postponed, may not be necessary. kind of a headache to the user too.
                        }
                    }
                    sl.complete(Loggers.DONE);

                }

            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
        }

        UCAddRemoveFriends:
        {

            itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.friendFindSearchButtonInput), EventType.CLICK.toString(), new EventListener() {

                private HumanId myhumanId = humanId;
                private RefObj<List<HumansIdentity>> mymatches = matches;
                private Set<Email> myemails = emails;

                @Override
                public void handleEvent(final Event evt_) {
                    final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.USER,
                            myhumanId.getObj() + " finding friends",
                            60000,
                            null,
                            true
                    );

                    final HumansNetPeople humansNetPeople = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(myhumanId);

                    final List<String> humansNetPeoples = new ArrayList<String>();
                    for (final HumansNetPeople h : humansNetPeople.getHumansNetPeoples()) {
                        humansNetPeoples.add(h.getHumanId());
                    }

                    clear($$(Controller.Page.friendFindSearchResults));
                    final List<Email> matchedEmailList = new ArrayList<Email>();

                    for (final HumansIdentity humansIdentity : mymatches.getObj()) {
                        matchedEmailList.add(new Email(humansIdentity.getHumanId()));
                        if (!humansNetPeoples.contains(humansIdentity.getHumanId())) {
                            generateFriendAddWidgetFor(new HumanId(humansIdentity.getHumanId()), myhumanId);
                        } else {
                            generateFriendDeleteWidgetFor(new HumanId(humansIdentity.getHumanId()), myhumanId);
                        }
                    }


                    clear($$(Controller.Page.friendFindSearchInvites));

                    for (final Email email : myemails) {
                        if (!matchedEmailList.contains(email)) {

                            final Element userPropertyContent = ElementComposer.compose($$(MarkupTag.A))
                                    .$ElementSetHref("#")
                                    .$ElementSetText("Invite").get();

                            new UserProperty(request, $$(Controller.Page.friendFindSearchInvites), userPropertyContent,
                                    new InviteCriteria(
                                            email.getObjectAsValid(), "http://www.ilikeplaces.com", "#", myhumanId,
                                            new ImportedContact().setEmailR(email.getObjectAsValid()).setFullNameR(email.getObjectAsValid())
                                    )) {
                                final HumanId mymyhumanId = myhumanId;
                                final Email myemail = email;

                                @Override
                                protected void init(Object... initArgs) {
                                }

                                @Override
                                protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {
                                    itsNatDocument_.addEventListener((EventTarget) $$(Page.user_property_content), EventType.CLICK.toString(), new EventListener() {
                                        @Override
                                        public void handleEvent(final Event evt_) {
                                            final SmartLogger inviteSL = SmartLogger.start(Loggers.LEVEL.USER,
                                                    mymyhumanId.getObj() + " sending join social network invitation to " + myemail.getObj(),
                                                    60000,
                                                    null,
                                                    true);
                                            SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                                    myemail.getObjectAsValid(),
                                                    humansIdentity.getHuman().getDisplayName(),
                                                    UserProperty.getUserPropertyHtmlFor(mymyhumanId, myemail.getObjectAsValid(),
                                                            "I think you should get on-board DOWN TOWN. It's for offline socializing. Invite your friends and get invited back in! http://www.ilikeplaces.com"
                                                    )
                                            );
                                            inviteSL.complete(Loggers.LEVEL.USER, Loggers.DONE);
                                            $$clear($$(Page.user_property_content));
                                            $$(Page.user_property_content).setTextContent("Invited!");
                                            remove(evt_.getTarget(), EventType.CLICK, this);
                                        }
                                    }, false);
                                }
                            };


//                            new Button(request, $$(Controller.Page.friendFindSearchInvites), "Invite " + email, false) {
//                                private HumanId mymyhumanId = myhumanId;
//                                private Email mymyemail = email;
//
//                                @Override
//                                protected void init(final Object... initArgs) {
//                                    setBluePrintCSSSpan(14, 0, 0, true);
//                                }
//
//                                @Override
//                                protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
//                                    itsNatDocument_.addEventListener((EventTarget) $$(Controller.Page.GenericButtonLink), EventType.CLICK.toString(), new EventListener() {
//                                        @Override
//                                        public void handleEvent(final Event evt_) {
//                                            final SmartLogger inviteSL = SmartLogger.start(Loggers.LEVEL.USER,
//                                                    mymyhumanId.getObj() + " sending join social network invitation to " + mymyemail.getObj(),
//                                                    60000,
//                                                    null,
//                                                    true);
//                                            SendMail.getSendMailLocal().sendAsSimpleText(
//                                                    mymyemail.getObj(),
//                                                    "On behalf of " + mymyhumanId.getObj(),
//                                                    "Hey, try out I Like Places(http://www.ilikeplaces.com). I've joined too.");
//                                            $$(Controller.Page.GenericButtonLink).setTextContent("Okay, Invitation Sent.");
//                                            inviteSL.complete(Loggers.LEVEL.USER, Loggers.DONE);
//                                            remove(evt_.getTarget(), EventType.CLICK, this);
//                                        }
//                                    }, false);
//                                }
//                            };
                        }
                    }
                    sl.complete(Loggers.LEVEL.USER, Loggers.DONE);
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


    private void generateFriendInviteWidgetFor(final ImportedContact importedContact, final HumanId currentUser) {
        new UserProperty(
                request,
                $$(Page.friendFindSearchInvites),
                ElementComposer.compose($$(MarkupTag.BR)).get(),
                new InviteCriteria(
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
                                        ((InviteCriteria) initArgs[1]).getInviterDisplayName()))) {

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

                                    if (humanCreateReturn.returnValue()) {

                                        UserIntroduction.createIntroData(new HumanId(myinvitee.getEmail()));

                                        final String activationURL = new Parameter("http://www.ilikeplaces.com/" + "activate")
                                                .append(ServletLogin.Username, myinvitee.getEmail(), true)
                                                .append(ServletLogin.Password,
                                                        DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(myinvitee.getEmail()))
                                                                .returnValue()
                                                                .getHumanAuthenticationHash())
                                                .get();

                                        String htmlBody = getHTMLStringForOnlineFriendInvite(myinviter, myinvitersName, myinvitee.getFullName());
                                        htmlBody = htmlBody.replace(URL, ElementComposer.generateSimpleLinkTo(activationURL));
                                        htmlBody = htmlBody.replace(PASSWORD_ADVICE, "Your temporary password is " + randomPassword);
                                        htmlBody = htmlBody.replace(PASSWORD_DETAILS, "Make sure you change it.");

                                        SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                                myinvitee.getHumanId(),
                                                "Invitation from " + myinvitersName,
                                                htmlBody);
                                    }
                                } catch (final Throwable t) {
                                    Loggers.EXCEPTION.error("Error creating user", t);
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
     * Online, implies that the inviter is online
     *
     * @param inviter
     * @param invitee
     * @return
     */
    final String getHTMLStringForOnlineFriendInvite(final HumanId inviteEmail, final String inviter, final String invitee) {
        try {

            final Document document = HTMLDocParser.getDocument(Controller.REAL_PATH + Controller.WEB_INF_PAGES + Controller.USER_PROPERTY_EMAIL_XHTML);

            $$(Controller.Page.user_property_name, document).setTextContent(inviter);
            $$(Controller.Page.user_property_name, document).setAttribute(MarkupTag.A.href(), "http://www.ilikeplaces.com");


            final String profilePhotoUrl = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansProfilePhoto(inviteEmail).returnValueBadly();

            $$(Controller.Page.user_property_profile_photo, document).setAttribute(MarkupTag.IMG.src(),
                    UserProperty.formatProfilePhotoUrlStatic(profilePhotoUrl));


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