/*
 * 
 * Each widget itself knows its functionality.
 * Hence, each widget should register its own event listeners.
 * But there will be several copies of the same widget in the same page.
 * Therefore, the widget ids should be different.
 * Each widget has to have its own class
 *
 *
 */
package ai.ilikeplaces.widgets;

import ai.ilikeplaces.servlets.Controller;
import java.math.BigInteger;
import java.util.HashSet;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.html.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.Element;
import static ai.ilikeplaces.servlets.Controller.*;

/**
 *
 * @author Ravindranath Akila
 */
public abstract class AbstractWidgetListener {

    /**
     *
     */
    /**
     *
     */
    protected final ItsNatDocument itsNatDocument_;
    private final HTMLDocument hTMLDocument_;
    /**
     * As a widget may have many instances within a document, we register each
     * instance with unique ids. i.e. the existing id is appended with the
     * instance number. Remember, each instance has to have its own instance id,
     * and should not have a reference to THIS copy which will lead to bugs.
     */
    private static long Instance_ = 0;
    /**
     * As a widget may have many instances within a document, we register each
     * instance with unique ids. i.e. the existing id is appended with the
     * instance number
     */
    final protected long instanceId;
    /**
     *
     */
    /**
     *
     */
    final protected Page page;

    final private static String Id = "id";

    /**
     *
     * @param itsNatDocument__
     * @param page__
     * @param appendToElement__
     */
    /**
     *
     * @param itsNatDocument__
     * @param page__
     * @param appendToElement__
     */
    public AbstractWidgetListener(final ItsNatDocument itsNatDocument__, final Page page__, final Element appendToElement__) {

        instanceId = Instance_++;
        page = page__;
        this.itsNatDocument_ = itsNatDocument__;
        final ItsNatHTMLDocument itsNatHTMLDocument_ = (ItsNatHTMLDocument) itsNatDocument_;
        this.hTMLDocument_ = itsNatHTMLDocument_.getHTMLDocument();
        final ItsNatServlet itsNatServlet_ = itsNatDocument_.getItsNatDocumentTemplate().getItsNatServlet();
        final ItsNatHTMLDocFragmentTemplate inhdft_ = (ItsNatHTMLDocFragmentTemplate) itsNatServlet_.getItsNatDocFragmentTemplate(page__.toString());
        appendToElement__.appendChild(inhdft_.loadDocumentFragmentBody(itsNatDocument_));

        setWidgetElementIds(Controller.GlobalPageIdRegistry.get(page));
        init();
        registerEventListeners(itsNatHTMLDocument_, hTMLDocument_);
    }

    /**
     *
     */
    /**
     *
     */
    protected abstract void init();

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the contructor
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
    protected final Element getElementById(final String key__) {
        final String elementId__ = Controller.GlobalHTMLIdRegistry.get(key__);
        if (elementId__ == null) {
            throw new java.lang.NullPointerException("ELEMENT \"" + key__ + "\" CONTAINS NULL OR NO REFERENCE IN REGISTRY!");
        }
        return hTMLDocument_.getElementById(elementId__);
    }

    /**
     * Id list of this widget
     *
     * @param keys__
     */
    private final void setWidgetElementIds(final HashSet<String> ids__) {
        for (final String id_ : ids__) {
            hTMLDocument_.getElementById(id_).setAttribute(Id, id_ + instanceId);
        }
    }

    /**
     *
     * @param key__
     * @return Element
     */
    protected final Element getWidgetElementById(final String key__) {
        final String elementId__ = Controller.GlobalHTMLIdRegistry.get(key__);
        if (elementId__ == null) {
            throw new java.lang.NullPointerException("ELEMENT \"" + key__ + "\" CONTAINS NULL OR NO REFERENCE IN REGISTRY!");
        }
        return hTMLDocument_.getElementById(elementId__ + instanceId);
    }
}
