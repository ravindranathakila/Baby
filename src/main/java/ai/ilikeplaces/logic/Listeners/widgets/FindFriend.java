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
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {


        itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.friendFindSearchTextInput), EventType.blur.toString(), new EventListener() {

            private HumanId myhumanId = humanId;
            private RefObj<List<HumansIdentity>> mymatches = matches;

            @Override
            public void handleEvent(final Event evt_) {
                final String emailString = ((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value());
                logger.debug("{}", emailString);

                mymatches.setObj(DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentitiesByEmails(Email.emails(emailString)));
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.friendFindSearchButtonInput), EventType.click.toString(), new EventListener() {

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