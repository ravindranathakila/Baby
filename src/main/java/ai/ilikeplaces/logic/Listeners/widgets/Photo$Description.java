package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.Loggers;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.close;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class Photo$Description extends AbstractWidgetListener {

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public Photo$Description(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final Object... initArgs) {
        super(itsNatDocument__, Page.Photo$Description, appendToElement__, initArgs);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        itsNatHTMLDocument_.addEventListener((EventTarget) $$(close), "click", new EventListener() {

            @Override
            public void handleEvent(final Event evt_) {
                displayNone($$(close));
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
