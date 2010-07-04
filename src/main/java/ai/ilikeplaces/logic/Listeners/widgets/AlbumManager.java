package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MarkupTag;
import ai.ilikeplaces.util.Return;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;


/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
public class AlbumManager extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(AlbumManager.class.getName());

    public AlbumManager(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final HumanId humanId__, final long privateEventId) {
        super(itsNatDocument__, Page.Album, appendToElement__, humanId__, privateEventId);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        final HumanId humanId = ((HumanId) initArgs[0]).getSelfAsValid();
        final Long privateEventId = (Long) initArgs[1];
        final Return<Boolean> r = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventIsOwner(humanId, privateEventId);
        if (r.returnStatus() == 0 && r.returnValue()) {
            $$(Controller.Page.AlbumPivateEventId).setAttribute(MarkupTag.INPUT.value(), privateEventId.toString());
        } else {
            $$(Controller.Page.AlbumNotice).setTextContent(r.returnMsg());
        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {

    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}