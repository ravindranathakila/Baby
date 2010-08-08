package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.servlets.Controller;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
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
    private final Document document_;
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
     * Per request refresh needs to be verified.
     */
    final protected ItsNatServletRequest request;


    final static private String NPE_1 = "SORRY! THIS DOCUMENT APPEARS NOT TO HAVE BEEN REGISTERED. PLEASE VERIFY THIS FIRST.(CAUSE OF NPE COULD BE DIFFERENT)";
    private static final String SORRY_I_FIND_THAT_ELEMENT = "SORRY! I FIND THAT ELEMENT \"";
    private static final String CONTAINS_NULL_OR_NO_REFERENCE_IN_REGISTRY = "\" CONTAINS NULL OR NO REFERENCE IN REGISTRY!";
    private static final String SORRY_COULD_NOT_FETCH_THE_ELEMENT_NAMED = "SORRY! COULD NOT FETCH THE ELEMENT NAMED ";
    private static final String FROM_FILE = " FROM FILE.";
    private static final String THIS_COULD_HAPPEN_IF_THE_WRONG_PAGE_IS_REGISTERED_IN_THE_WIDGET_CONSTRUCTOR_WHEN_CALLING_SUPER = "\nTHIS COULD HAPPEN IF THE WRONG PAGE IS REGISTERED IN THE WIDGET CONSTRUCTOR WHEN CALLING SUPER OR IF THE ELEMENT WAS REGISTERED UNDER A WRONG ENTITY.";
    private static final String STYLE = "style";
    private static final String DISPLAY_BLOCK = "display:block;";
    private static final String DISPLAY_NONE = "display:none;";
    /**
     * Check for null before use. Null if not initialized
     */
    public String fetchToEmail = null;


    /**
     * @param request__
     * @param page__
     * @param appendToElement__
     */
    @WARNING(warning = "If you want your variables initialized by the time you reach registereventlisteners, do the asignmen in init, NOT the implemented subclass constructer" +
            "which runs as super, init, registereventlisteners and THEN the remainder of the constructer.")
    public AbstractWidgetListener(final ItsNatServletRequest request__, final Page page__, final Element appendToElement__, final Object... initArgs) {

        request = request__;
        instanceId = InstanceCounter_++;
        page = page__;
        this.itsNatDocument_ = request__.getItsNatDocument();
        final ItsNatHTMLDocument itsNatHTMLDocument_ = (ItsNatHTMLDocument) itsNatDocument_;
        this.hTMLDocument_ = itsNatHTMLDocument_.getHTMLDocument();
        this.document_ = itsNatDocument_.getDocument();
        final ItsNatServlet itsNatServlet_ = itsNatDocument_.getItsNatDocumentTemplate().getItsNatServlet();
        final ItsNatHTMLDocFragmentTemplate inhdft_ = (ItsNatHTMLDocFragmentTemplate) itsNatServlet_.getItsNatDocFragmentTemplate(page__.toString());
        LogNull.logThrow(inhdft_, NPE_1);//Do not remove unless performance degrade is evident.
        appendToElement__.appendChild(inhdft_.loadDocumentFragmentBody(itsNatDocument_));

        setWidgetElementIds(Controller.GlobalPageIdRegistry.get(page));
        init(initArgs);
        registerEventListeners(itsNatHTMLDocument_, hTMLDocument_);
    }

    protected abstract void init(final Object... initArgs);

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the constructor
     *
     * @param itsNatHTMLDocument_
     * @param hTMLDocument_
     */
    protected abstract void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_);

    protected void fetchToEmail(final Object... args) {
        throw ExceptionCache.METHOD_NOT_IMPLEMENTED;
    }

    /**
     * Id registry should be globally visible to callers
     *
     * @param key__
     * @return Element
     */
    @WARNING(warning = "You cannot actually use this method as it will definitely throw a NPE. Check if fetching a main xhtml template element works. Should." +
            "Nevertheless, having this is bug friendly as $ instead of $$ is an easy miss for widgets.")
    @Deprecated
    protected final Element $(final String key__) {
        final String elementId__ = Controller.GlobalHTMLIdRegistry.get(key__);
        if (elementId__ == null) {
            throw new java.lang.NullPointerException("SORRY! I FIND THAT ELEMENT \"" + key__ + "\" CONTAINS NULL OR NO REFERENCE IN REGISTRY!");
        }
        final Element returnVal = document_.getElementById(elementId__);
        return returnVal != null ? returnVal : (Element) LogNull.logThrow("SORRY! CANNOT FETCH THE ELEMENT " + key__ + ". YOU PROBABLY WERE SUPPOSED TO USE $$ INSTEAD OF $");
    }

    /**
     * Id list of this widget
     *
     * @param ids__
     */
    private final void setWidgetElementIds(final HashSet<String> ids__) {
        for (final String id_ : ids__) {
            document_.getElementById(id_).setAttribute(Id, id_ + instanceId);
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

    /**
     * Fetches the element disregarding widget behavior. i.e. ignores dynamic nature of ID's
     *
     * @param key__
     * @param document__
     * @return
     */
    final protected Element $$(final String key__, final Document document__) {
        return document__.getElementById(key__);
    }

    final private Element getWidgetElementById(final String key__) {
        final String elementId__ = Controller.GlobalHTMLIdRegistry.get(key__);
        if (elementId__ == null) {
            throw new java.lang.NullPointerException("SORRY! I FIND THAT ELEMENT \"" + key__ + "\" CONTAINS NULL OR NO REFERENCE IN REGISTRY!");
        }
        final Element returnVal = document_.getElementById(elementId__ + instanceId);
        return returnVal != null ? returnVal : (Element) LogNull.logThrow("SORRY! COULD NOT FETCH THE ELEMENT NAMED " + key__ + " FROM PAGE:" + page.toString() +
                "\nTHIS COULD HAPPEN IF THE WRONG PAGE IS REGISTERED IN THE WIDGET CONSTRUCTOR WHEN CALLING SUPER OR IF THE ELEMENT WAS REGISTERED UNDER A WRONG ENTITY..");
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
                    final String existingVal = $$(elementId__).getAttribute(STYLE);
                    if (existingVal.contains("display:block")) {
                        $$(elementId__).setAttribute(STYLE, existingVal.replace("display:block", "display:none"));
                    } else {
                        $$(elementId__).setAttribute(STYLE, DISPLAY_NONE + existingVal);
                    }
                }
            }
            visible = false;
        } else {
            for (String elementId__ : widgetElements) {
                if (!elementId__.equals(toggleLink)) {
                    @FIXME(issue = "this is wrong. css can have necessary spaces. e.g. backgroound-image:0% 0% url(/path/image.png);")
                    final String existingVal = $$(elementId__).getAttribute(STYLE).replace(" ", "");

                    if (existingVal.contains("display:none")) {
                        $$(elementId__).setAttribute(STYLE, existingVal.replace("display:none", "display:block"));
                    } else {
                        $$(elementId__).setAttribute(STYLE, DISPLAY_BLOCK + existingVal);
                    }
                }
            }
            visible = true;
        }
    }

    protected final void displayBlock(final Element element__) {
        element__.setAttribute(STYLE, DISPLAY_BLOCK);
    }

    protected final void displayNone(final Element element__) {
        element__.setAttribute(STYLE, DISPLAY_NONE);
    }

    final protected Element $$(MarkupTagFace tagNameInAllCaps) {
        return document_.createElement(tagNameInAllCaps.toString());
    }

    final protected Element $$(MarkupTagFace tagNameInAllCaps__,final Document document__) {
        return document__.createElement(tagNameInAllCaps__.toString());
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

    /**
     * @param whoseChildrenRemoved
     * @return whoseChildrenRemoved
     */
    final protected Element clear(final Element whoseChildrenRemoved) {
        try {
            //whoseChildrenRemoved.normalize();//Clearing the air for text nodes
            while (whoseChildrenRemoved.hasChildNodes()) {
                whoseChildrenRemoved.removeChild(whoseChildrenRemoved.getFirstChild());
            }
        } catch (final Exception e) {
            Loggers.EXCEPTION.error("SORRY! AN ERROR OCCURRED WHILE CLEARING CONTENT.", e);
        }
        return whoseChildrenRemoved;
//        if (owner.hasChildNodes()) {
//            final NodeList childNodes = element__.getChildNodes();
//            for (int i = 0; i < childNodes.getLength(); i++) {
//                element__.removeChild(childNodes.item(i));
//            }
//        }
    }

    /**
     * Note: suppresses NPE occured due to element not having any children.
     * Advice: use &nbsp;
     *
     * @param elementToBeSetTextOf
     * @param textToBeSet
     * @return elementToBeSetTextOf
     */
    static protected Element $$setText(final Element elementToBeSetTextOf, final String textToBeSet) {
        try {
            ElementComposer.$ElementSetText(elementToBeSetTextOf, textToBeSet);
        } catch (final NullPointerException npe) {
            Loggers.EXCEPTION.error("SORRY! THIS ELEMENT DOES NOT HAVE ANY CHILDREN", npe);
        }
        return elementToBeSetTextOf;
    }

    protected Element $$(final Event event) {
        return (Element) event.getCurrentTarget();
    }

    final static protected Logger logger = LoggerFactory.getLogger(AbstractWidgetListener.class);

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}
