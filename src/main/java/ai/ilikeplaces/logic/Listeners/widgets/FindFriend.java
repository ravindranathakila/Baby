package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import net.sf.oval.exception.ConstraintsViolatedException;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class FindFriend extends AbstractWidgetListener {

    final private Logger logger = LoggerFactory.getLogger(FindFriend.class.getName());

    HumanId humanId;

    RefObj<List<HumansIdentity>> matches = null;


    public FindFriend(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final HumanId humanId) {
        super(itsNatDocument__, Page.FindFriend, appendToElement__, humanId);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        matches = new Obj<List<HumansIdentity>>(new ArrayList<HumansIdentity>());

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
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {

        UCProcessUserEmailInput:
        {

            itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.friendFindSearchTextInput), EventType.BLUR.toString(), new EventListener() {

                private HumanId myhumanId = humanId;
                private RefObj<List<HumansIdentity>> mymatches = matches;

                @Override
                public void handleEvent(final Event evt_) {
                    final String emailString = ((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value());
                    logger.debug("{}", emailString);
                    final List<Email> mails = Email.emails(emailString);
                    final List<HumansIdentity> existingUsers = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentitiesByEmails(mails);
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

                }
            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
        }

        UCAddRemoveFriends:
        {

            itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.friendFindSearchButtonInput), EventType.CLICK.toString(), new EventListener() {

                private HumanId myhumanId = humanId;
                private RefObj<List<HumansIdentity>> mymatches = matches;

                @Override
                public void handleEvent(final Event evt_) {
                    logger.debug("{}", "Clicked Find");
                    final HumansNetPeople humansNetPeople = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(myhumanId);

                    List<String> humansNetPeoples = new ArrayList<String>();
                    for (final HumansNetPeople h : humansNetPeople.getHumansNetPeoples()) {
                        humansNetPeoples.add(h.getHumanId());
                    }

                    clear($$(Controller.Page.friendFindSearchResults));

                    for (final HumansIdentity humansIdentity : mymatches.getObj()) {
                        if (!humansNetPeoples.contains(humansIdentity.getHumanId())) {
                            new FriendAdd(itsNatDocument_, $$(Controller.Page.friendFindSearchResults), new HumanId(humansIdentity.getHumanId()), myhumanId) {
                            };
                        } else {
                            new FriendDelete(itsNatDocument_, $$(Controller.Page.friendFindSearchResults), new HumanId(humansIdentity.getHumanId()), myhumanId) {
                            };
                        }
                    }
                }
            }, false);
        }
    }
}