package ai.ilikeplaces.widgets;

import ai.ilikeplaces.servlets.Controller.Page;
import java.math.BigInteger;
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
    protected BigInteger instanceId;

    public Photo$Description(final ItsNatDocument itsNatDocument__, final Element appendToElement__) {
        super(itsNatDocument__, Page.Photo$Description.toString(), appendToElement__);
    }

    @Override
    protected void setInstanceId(final BigInteger instance_) {
        instanceId = instance_;//We get a reference to a new instance on this call, as per implementation, so we directly point to it.
    }

    @Override
    protected BigInteger getInstanceId() {
        return instanceId;
    }

    @Override
    protected void init() {
        //hTMLDocument_.getElementById("pd").setAttribute("id", "pd" + instanceId);
        setWidgetElementIds("pd");
        getWidgetElementById("pd").setAttribute("style", "color:red");
    }

    @Override
    protected void registerEventListeners() {
        itsNatHTMLDocument_.addEventListener((EventTarget) getWidgetElementById("pd"), "click", new EventListener() {

            public void handleEvent(final Event evt_) {
                getWidgetElementById("pd").getParentNode().appendChild(hTMLDocument_.createTextNode("Clicked BD!" + instanceId));
            }
        }, false);
    }
}
