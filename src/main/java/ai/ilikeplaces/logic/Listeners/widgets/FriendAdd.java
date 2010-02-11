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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 21, 2010
 * Time: 10:03:54 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class FriendAdd extends AbstractWidgetListener {
    final private Logger logger = LoggerFactory.getLogger(FriendAdd.class.getName());

    HumanId humanId = null;
    HumanId caller = null;

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     * @param humanId
     */
    public FriendAdd(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final HumanId humanId, final HumanId caller) {
        super(itsNatDocument__, Controller.Page.FriendAdd, appendToElement__, humanId, caller);
    }

    @Override
    protected void init(final Object... initArgs) {
        this.humanId = (HumanId) initArgs[0];
        this.caller = (HumanId) initArgs[1];
        final HumansIdentity hid = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(humanId).getHumansIdentity();

        $$(Controller.Page.friendAddFirstNameLabel).setTextContent(humanId.getObj());
        $$(Controller.Page.friendAddLastNameLabel).setTextContent("TODO");
        //$$(Controller.Page.friendAddBirthYearLabel).setTextContent(Integer.toString(hid.getHumansIdentityDateOfBirth().getYear()));
        $$(Controller.Page.friendAddLocationLabel).setTextContent("TODO");
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
        itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.friendAddAddButton), EventType.click.toString(), new EventListener() {

            private HumanId myhumanId = humanId;
            private HumanId mycaller = caller;

            @Override
            public void handleEvent(final Event evt_) {
                logger.debug("{}", "Clicked Find");
                Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doNTxAddHumansNetPeople(mycaller, myhumanId);
                logger.debug("{}", r.toString());
                $$(Controller.Page.friendAddAddButton).setTextContent("DONE");
                remove(evt_.getTarget(), EventType.click, this);
            }
        }, false);
    }
}
