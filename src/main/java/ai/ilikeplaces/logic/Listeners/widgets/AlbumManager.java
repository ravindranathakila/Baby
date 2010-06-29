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
abstract public class AlbumManager extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(AlbumManager.class.getName());

    private HumanId humanId = null;

    public AlbumManager(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final String humanId__) {
        super(itsNatDocument__, Page.Album, appendToElement__, humanId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = new HumanId((String) initArgs[0]);

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