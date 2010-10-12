package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.Return;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
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
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 21, 2010
 * Time: 10:03:54 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class FriendAdd extends AbstractWidgetListener {
    final private Logger logger = LoggerFactory.getLogger(FriendAdd.class.getName());

    HumanId addee = null;
    HumanId caller = null;

    /**
     *
     * @param request__
     * @param appendToElement__
     * @param addee
     * @param caller
     */
    public FriendAdd(final ItsNatServletRequest request__,  final Element appendToElement__, final HumanId addee, final HumanId caller) {
        super(request__, Controller.Page.FriendAdd, appendToElement__, addee, caller);
    }

    @Override
    protected void init(final Object... initArgs) {
        this.addee = (HumanId) initArgs[0];
        this.caller = (HumanId) initArgs[1];

        $$(Controller.Page.friendAddDisplayNameLabel).setTextContent(DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(addee).getDisplayName());
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
        itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.friendAddAddButton), EventType.CLICK.toString(), new EventListener() {

            private HumanId myaddee = addee;
            private HumanId mycaller = caller;

            @Override
            public void handleEvent(final Event evt_) {
                logger.debug("{}", "Clicked Find");
                Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doNTxAddHumansNetPeople(mycaller, myaddee);
                if (r.returnStatus() == 0) {
                    logger.debug("{}", r.toString());
                    $$(Controller.Page.friendAddAddButton).setTextContent("Added!");
                    final Human adder = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(mycaller.getObj());
                    final Human addee = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(mycaller.getObj());
                    if (!addee.isFriend(adder.getHumanId())) {
                        SendMail.getSendMailLocal().sendAsSimpleText(myaddee.getObj(),
                                adder.getDisplayName(),
                                adder.getDisplayName() + " has added you as a friend. You can add " + adder.getDisplayName() + " back as your friend at "
                                        + "http://www.ilikeplaces.com/i/" + DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPublicURL(mycaller).returnValue());
                    }
                    remove(evt_.getTarget(), EventType.CLICK, this);
                } else {
                    //DO something!
                }
            }


            @Override
            public void finalize() throws Throwable {
                Loggers.finalized(this.getClass().getName());
                super.finalize();
            }
        }, false);
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}
