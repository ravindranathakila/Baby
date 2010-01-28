package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.servlets.Controller;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.html.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.HashSet;
import java.util.Set;

import static ai.ilikeplaces.servlets.Controller.Page;

/**
 * Each widget itself knows its functionality.
 * Hence, each widget should register its own event listeners.
 * But there will be several copies of the same widget in the same page.
 * Therefore, the widget ids should be different.
 * Each widget has to have its own class
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public abstract class AbstractWidgetListener {

    protected final ItsNatDocument itsNatDocument_;
    private final HTMLDocument hTMLDocument_;
    /**
     * As a widget may have many instances within a document, we register each
     * instance with unique ids. i.e. the existing id is appended with the
     * instance number. Remember, each instance has to have its own instance id,
     * and should not have a reference to THIS copy which will lead to bugs.
     */
    private static long InstanceCounter_ = 0;
    /**
     * As a widget may have many instances within a document, we register each
     * instance with unique ids. i.e. the existing id is appended with the
     * instance number
     */
    final protected long instanceId;
    final protected Page page;
    final private static String Id = "id";
    private boolean visible = true;


    /**
     * @param itsNatDocument__
     * @param page__
     * @param appendToElement__
     */
    @WARNING(warning = "If you want your variables initialized by the time you reach registereventlisteners, do the asignmen in init, NOT the implemented subclass constructer" +
            "which runs as super, init, registereventlisteners and THEN the remainder of the constructer.")
    public AbstractWidgetListener(final ItsNatDocument itsNatDocument__, final Page page__, final Element appendToElement__, final Object... initArgs) {

        instanceId = InstanceCounter_++;
        page = page__;
        this.itsNatDocument_ = itsNatDocument__;
        final ItsNatHTMLDocument itsNatHTMLDocument_ = (ItsNatHTMLDocument) itsNatDocument_;
        this.hTMLDocument_ = itsNatHTMLDocument_.getHTMLDocument();
        final ItsNatServlet itsNatServlet_ = itsNatDocument_.getItsNatDocumentTemplate().getItsNatServlet();
        final ItsNatHTMLDocFragmentTemplate inhdft_ = (ItsNatHTMLDocFragmentTemplate) itsNatServlet_.getItsNatDocFragmentTemplate(page__.toString());
        appendToElement__.appendChild(inhdft_.loadDocumentFragmentBody(itsNatDocument_));

        setWidgetElementIds(Controller.GlobalPageIdRegistry.get(page));
        init(initArgs);
        registerEventListeners(itsNatHTMLDocument_, hTMLDocument_);
    }

    protected abstract void init(final Object... initArgs);

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the contructor
     *
     * @param itsNatHTMLDocument_
     * @param hTMLDocument_
     */
    protected abstract void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_);

    /**
     * Id registry should be globally visible to callers
     *
     * @param key__
     * @return Element
     */
    protected final Element $(final String key__) {
        final String elementId__ = Controller.GlobalHTMLIdRegistry.get(key__);
        if (elementId__ == null) {
            throw new java.lang.NullPointerException("SORRY! I FIND THAT ELEMENT \"" + key__ + "\" CONTAINS NULL OR NO REFERENCE IN REGISTRY!");
        }
        return hTMLDocument_.getElementById(elementId__);
    }

    /**
     * Id list of this widget
     *
     * @param ids__
     */
    private final void setWidgetElementIds(final HashSet<String> ids__) {
        for (final String id_ : ids__) {
            hTMLDocument_.getElementById(id_).setAttribute(Id, id_ + instanceId);
        }
    }

    /**
     * Wrapper to getWidgetElementById
     *
     * @param key__
     * @return Element
     */
    final protected Element $$(final String key__) {
        return getWidgetElementById(key__);
    }

    final private Element getWidgetElementById(final String key__) {
        final String elementId__ = Controller.GlobalHTMLIdRegistry.get(key__);
        if (elementId__ == null) {
            throw new java.lang.NullPointerException("SORRY! I FIND THAT ELEMENT \"" + key__ + "\" CONTAINS NULL OR NO REFERENCE IN REGISTRY!");
        }
        final Element returnVal = hTMLDocument_.getElementById(elementId__ + instanceId);
        return returnVal != null ? returnVal : (Element) LogNull.logThrow("SORRY! COULD NOT FETCH THE ELEMENT NAMED " + key__ + " FROM FILE." +
                "\nTHIS COULD HAPPEN IF THE WRONG PAGE IS REGISTERED IN THE WIDGET CONSTRUCTOR WHEN CALLING SUPER.");
    }

    /**
     * @return HashSet<String> widgetElements
     */
    final protected Set<String> getWidgetElements() {
        return Controller.GlobalPageIdRegistry.get(page);
    }

    @Deprecated
    protected void toggleVisible(final String toggleLink) {
        final Set<String> widgetElements = getWidgetElements();
        if (visible) {
            for (String elementId__ : widgetElements) {
                if (!elementId__.equals(toggleLink)) {
                    final String existingVal = $$(elementId__).getAttribute("style");
                    if (existingVal.contains("display:block")) {
                        $$(elementId__).setAttribute("style", existingVal.replace("display:block", "display:none"));
                    } else {
                        $$(elementId__).setAttribute("style", "display:none;" + existingVal);
                    }
                }
            }
            visible = false;
        } else {
            for (String elementId__ : widgetElements) {
                if (!elementId__.equals(toggleLink)) {
                    @FIXME(issue = "this is wrong. css can have necessary spaces. e.g. backgroound-image:0% 0% url(/path/image.png);")
                    final String existingVal = $$(elementId__).getAttribute("style").replace(" ", "");

                    if (existingVal.contains("display:none")) {
                        $$(elementId__).setAttribute("style", existingVal.replace("display:none", "display:block"));
                    } else {
                        $$(elementId__).setAttribute("style", "display:block;" + existingVal);
                    }
                }
            }
            visible = true;
        }
    }

    @NOTE(note = "toogleVisitble CANNOT BE CALLED IF ALL ELEMENTS ARE HIDDEN. SO WE JUST HIDE OVERRIDING STYLES AS HIDE WOULD BE PERMANENT.")
    protected void hide(final String toggleLink) {
        final Set<String> widgetElements = getWidgetElements();
        for (String elementId__ : widgetElements) {
            $$(elementId__).setAttribute("style", "display:none;");
        }
    }

    final protected Element $$(MarkupTagFace tagNameInAllCaps) {
        return hTMLDocument_.createElement(tagNameInAllCaps.toString());
    }

    /**
     * @param eventTarget_
     * @param eventType_
     * @param eventListener_
     * @param useCapture     ... default false
     */
    final protected void remove(final EventTarget eventTarget_, final EventType eventType_, final EventListener eventListener_, final Boolean... useCapture) {
        logger.debug("HELLO, REMOVING WIDGET LISTENER");
        itsNatDocument_.removeEventListener(eventTarget_, eventType_.toString(), eventListener_, useCapture.length == 0 ? false : useCapture[0]);
    }

    final protected void clear(final Element element__) {
        final Node owner = (Node) element__;
        int i = 1;
        while (owner.hasChildNodes()) {
            owner.removeChild(owner.getFirstChild());
            logger.debug("LOOP:" + i++);
        }
//        if (owner.hasChildNodes()) {
//            final NodeList childNodes = element__.getChildNodes();
//            for (int i = 0; i < childNodes.getLength(); i++) {
//                element__.removeChild(childNodes.item(i));
//            }
//        }
    }

    final static protected Logger logger = LoggerFactory.getLogger(AbstractWidgetListener.class);
}
