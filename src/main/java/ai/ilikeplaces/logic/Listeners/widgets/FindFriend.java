package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
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

import java.util.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class FindFriend extends AbstractWidgetListener {

    final private Logger logger = LoggerFactory.getLogger(FindFriend.class.getName());

    HumanId humanId;

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

        List<String> emails = new ArrayList<String>();
        for (final HumansNetPeople hnp : DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(humanId).getHumansNetPeoples()) {
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
            clear($$(Controller.Page.friendFindSearchResults));

            final HumansNetPeople humansNetPeople = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(humanId);

            for (final HumansNetPeople friend : humansNetPeople.getHumansNetPeoples()) {

                new UserProperty(request, $$(Controller.Page.friendFindSearchResults), new HumanId(friend.getHumanId())) {
                    protected void init(final Object... initArgs) {
                        new FriendDelete(request, $$(Controller.Page.user_property_content), (HumanId) initArgs[0], humanId) {
                        };
                    }
                };

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

                @Override
                public void finalize() throws Throwable {
                    Loggers.finalized(this.getClass().getName());
                    super.finalize();
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

                            new UserProperty(request, $$(Controller.Page.friendFindSearchResults), new HumanId(humansIdentity.getHumanId())) {
                                protected void init(final Object... initArgs) {
                                    new FriendAdd(request, $$(Controller.Page.user_property_content), (HumanId) initArgs[0], myhumanId) {
                                    };
                                }
                            };

                        } else {

                            new UserProperty(request, $$(Controller.Page.friendFindSearchResults), new HumanId(humansIdentity.getHumanId())) {
                                protected void init(final Object... initArgs) {
                                    new FriendDelete(request, $$(Controller.Page.user_property_content), (HumanId) initArgs[0], myhumanId) {
                                    };
                                }
                            };

                        }
                    }


                    clear($$(Controller.Page.friendFindSearchInvites));
                    for (final Email email : myemails) {
                        if (!matchedEmailList.contains(email)) {

                            new Button(request, $$(Controller.Page.friendFindSearchInvites), "Invite " + email, false) {
                                private HumanId mymyhumanId = myhumanId;
                                private Email mymyemail = email;

                                @Override
                                protected void init(final Object... initArgs) {
                                    setBluePrintCSSSpan(14, 0, 0, true);
                                }

                                @Override
                                protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
                                    itsNatDocument_.addEventListener((EventTarget) $$(Controller.Page.GenericButtonLink), EventType.CLICK.toString(), new EventListener() {
                                        @Override
                                        public void handleEvent(final Event evt_) {
                                            final SmartLogger inviteSL = SmartLogger.start(Loggers.LEVEL.USER,
                                                                                           mymyhumanId.getObj() + " sending join social network invitation to " + mymyemail.getObj(),
                                                                                           60000,
                                                                                           null,
                                                                                           true);
                                            SendMail.getSendMailLocal().sendAsSimpleText(
                                                    mymyemail.getObj(),
                                                    "On behalf of " + mymyhumanId.getObj(),
                                                    "Hey, try out I Like Places(http://www.ilikeplaces.com). I've joined too.");
                                            $$(Controller.Page.GenericButtonLink).setTextContent("Okay, Invitation Sent.");
                                            inviteSL.complete(Loggers.LEVEL.USER, Loggers.DONE);
                                            remove(evt_.getTarget(), EventType.CLICK, this);
                                        }

                                        @Override
                                        public void finalize() throws Throwable {
                                            Loggers.finalized(this.getClass().getName());
                                            super.finalize();
                                        }
                                    }, false);
                                }
                            };
                        }
                    }
                    sl.complete(Loggers.LEVEL.USER, Loggers.DONE);
                }

                @Override
                public void finalize() throws Throwable {
                    Loggers.finalized(this.getClass().getName());
                    super.finalize();
                }
            }, false);
        }
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}