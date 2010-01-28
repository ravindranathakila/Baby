package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.Return;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 21, 2010
 * Time: 10:04:15 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class FriendDelete extends AbstractWidgetListener {

    HumanId humanId = null;
    HumanId caller = null;

    /**
     * @param itsNatDocument__
     * @param page__
     * @param appendToElement__
     */
    public FriendDelete(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final HumanId humanId, final HumanId caller) {
        super(itsNatDocument__, Controller.Page.FriendDelete, appendToElement__, humanId, caller);
    }

    @Override
    protected void init(final Object... initArgs) {
        this.humanId = ((HumanId) initArgs[0]);
        this.caller = ((HumanId) initArgs[1]);
        final HumansIdentity hid = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(humanId).getHumansIdentity();

        $$(Controller.Page.friendDeleteFirstNameLabel).setTextContent(humanId.getObj());
        $$(Controller.Page.friendDeleteLastNameLabel).setTextContent("TODO");
        $$(Controller.Page.friendDeleteBirthYearLabel).setTextContent(Integer.toString(hid.getHumansIdentityDateOfBirth().getYear()));
    }

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the contructor
     *
     * @param itsNatHTMLDocument_
     * @param hTMLDocument_
     */
    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.friendDeleteAddButton), EventType.click.toString(), new EventListener() {

            private HumanId myhumanId = humanId;
            private HumanId mycaller = caller;

            @Override
            public void handleEvent(final Event evt_) {
                logger.debug("{}", "Clicked Delete");
                Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doNTxRemoveHumansNetPeople(mycaller, myhumanId);
                logger.debug("{}", r.toString());
                $$(Controller.Page.friendDeleteAddButton).setTextContent("DONE");
                remove(evt_.getTarget(), EventType.click, this);
            }
        }, false);

    }
}
