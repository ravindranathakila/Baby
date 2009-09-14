package ai.ilikeplaces.widgets;

import ai.ilikeplaces.servlets.Controller.Page;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.Event;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author Ravindranath Akila
 */
public abstract class Photo$Description extends AbstractWidgetListener {

    public Photo$Description(final ItsNatDocument itsNatDocument__, final Element appendToElement__) {
        super(itsNatDocument__, Page.Photo$Description, appendToElement__);
    }

    @Override
    protected void init() {
        getWidgetElementById("pd").setAttribute("style", "color:red");
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        itsNatHTMLDocument_.addEventListener((EventTarget) getWidgetElementById("pd"), "click", new EventListener() {

            public void handleEvent(final Event evt_) {
                getWidgetElementById("pd").getParentNode().appendChild(hTMLDocument_.createTextNode("Clicked BD!" + instanceId));
            }
        }, false);
    }
}
