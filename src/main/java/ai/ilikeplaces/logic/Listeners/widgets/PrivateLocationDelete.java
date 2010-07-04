package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.List;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateLocationDelete extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(PrivateLocationDelete.class.getName());

    private HumanId humanId = null;
    private Long privateLocationId = null;
    Return<PrivateLocation> r;
    List<HumansNetPeople> possibilities;

    public PrivateLocationDelete(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final String humanId__, final long privateLocationId__) {
        super(itsNatDocument__, Page.PrivateLocationDelete, appendToElement__, humanId__, privateLocationId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = new HumanId();
        humanId.setObjAsValid((String) initArgs[0]);

        this.privateLocationId = (Long) initArgs[1];

        r = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationAsOwner(humanId, privateLocationId);
        if (r.returnStatus() == 0) {
            $$(privateLocationDeleteName).setTextContent(r.returnValue().getPrivateLocationName());
            $$(privateLocationDeleteInfo).setTextContent(r.returnValue().getPrivateLocationInfo());
        } else {
            $$(privateLocationDeleteNotice).setTextContent(r.returnMsg());
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationDelete), EventType.CLICK.toString(), new EventListener() {

            final HumanId myhumanId = humanId;
            final Long myprivateLocationId = privateLocationId;

            @Override
            public void handleEvent(final Event evt_) {
                logger.debug("{}", "HELLO! CLICKED DELETE.");

                final Return<Boolean> r = DB.getHumanCrudPrivateLocationLocal(true).dPrivateLocation(myhumanId, myprivateLocationId);
                if (r.returnStatus() == 0) {
                    logger.debug("{}", "HELLO! DELETED. DB REPLY:" + r.returnValue());
                    remove(evt_.getTarget(), EventType.CLICK, this);
                    logger.debug("{}", "HELLO! REMOVED CLICK.");
                    clear($$(privateLocationDeleteNotice));
                } else {
                    $$(privateLocationDelete).setTextContent(r.returnMsg());
                }


            }

            @Override
            public void finalize() throws Throwable {
                Loggers.finalized(this.getClass().getName());
                super.finalize();
            }
        }, false, JSCodeToSend.RefreshPage);

        final HumansNetPeople user = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(humanId);
        this.possibilities = user.getHumansNetPeoples();

        AddRemoveOwners:
        {
            new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateLocation>>(
                    itsNatDocument_,
                    $$(privateLocationDeleteOwners),
                    user.getHumansNet(),
                    possibilities,
                    r.returnValue().getPrivateLocationOwners(),
                    new Save<Return<PrivateLocation>>() {

                        final long myprivateLocationId = r.returnValue().getPrivateLocationId();

                        @Override
                        public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                            return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationAddOwner(humanId, myprivateLocationId, humansFriend);
                        }
                    },
                    new Save<Return<PrivateLocation>>() {

                        final long myprivateLocationId = r.returnValue().getPrivateLocationId();

                        @Override
                        public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                            return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationRemoveOwner(humanId, myprivateLocationId, humansFriend);
                        }
                    }) {
            };
        }
        AddRemoveVisitors:
        {
            new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateLocation>>(
                    itsNatDocument_,
                    $$(privateLocationDeleteVisitors),
                    user.getHumansNet(),
                    possibilities,
                    r.returnValue().getPrivateLocationViewers(),
                    new Save<Return<PrivateLocation>>() {

                        final long myprivateLocationId = r.returnValue().getPrivateLocationId();

                        @Override
                        public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                            return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationAddVisitor(humanId, myprivateLocationId, humansFriend);
                        }
                    },
                    new Save<Return<PrivateLocation>>() {

                        final long myprivateLocationId = r.returnValue().getPrivateLocationId();

                        @Override
                        public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                            return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationRemoveVisitor(humanId, myprivateLocationId, humansFriend);
                        }
                    }) {
            };
        }
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}