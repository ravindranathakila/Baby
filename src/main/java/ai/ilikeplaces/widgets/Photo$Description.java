package ai.ilikeplaces.widgets;

import ai.ilikeplaces.servlets.Controller.Page;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.Event;
import org.w3c.dom.Element;

/**
 *
 * @author Ravindranath Akila
 */
public abstract class Photo$Description extends AbstractWidgetListener {

    /**
     * As a widget may have many instances within a document, we register each
     * instance with unique ids. i.e. the existing id is appended with the
     * instance number
     */
    protected static int instance_ = 0;

    public Photo$Description(final ItsNatDocument itsNatDocument__, final Element appendToElement__) {
        super(itsNatDocument__, Page.Photo$Description.toString(), appendToElement__);
    }

    @Override
    protected void init() {
        instance_++;
        hTMLDocument_.getElementById("pd").setAttribute("id", "pd" + instance_);
        itsNatDocument_.getDocument().getElementById("pd" + instance_).setAttribute("style", "color:red");
    }

    @Override
    protected void registerEventListeners() {
        final int instance__ = instance_;
        itsNatHTMLDocument_.addEventListener((EventTarget) itsNatDocument_.getDocument().getElementById("pd" + instance__), "click", new EventListener() {

            public void handleEvent(final Event evt_) {
                hTMLDocument_.getElementById("pd" + instance__).getParentNode().appendChild(hTMLDocument_.createTextNode("Clicked!" + instance__));
            }
        }, false);
    }
}
