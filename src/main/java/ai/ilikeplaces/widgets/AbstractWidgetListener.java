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

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import ai.ilikeplaces.servlets.Controller;
import java.util.Map;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.core.html.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.Element;

/**
 *
 * @author Ravindranath Akila
 */
public abstract class AbstractWidgetListener {

    protected final ItsNatDocument itsNatDocument_;
    protected final ItsNatHTMLDocument itsNatHTMLDocument_;
    protected final HTMLDocument hTMLDocument_;
    protected final ItsNatServlet itsNatServlet_;

    public AbstractWidgetListener(final ItsNatDocument itsNatDocument__, final String page__, final Element appendToElement__) {
        this.itsNatDocument_ = itsNatDocument__;
        this.itsNatHTMLDocument_ = (ItsNatHTMLDocument) itsNatDocument_;
        this.hTMLDocument_ = itsNatHTMLDocument_.getHTMLDocument();
        this.itsNatServlet_ = itsNatDocument_.getItsNatDocumentTemplate().getItsNatServlet();
        final ItsNatHTMLDocFragmentTemplate inhdft_ = (ItsNatHTMLDocFragmentTemplate) itsNatServlet_.getItsNatDocFragmentTemplate(page__);
        appendToElement__.appendChild(inhdft_.loadDocumentFragmentBody(itsNatDocument_));

        init();
        registerEventListeners();
    }

    protected abstract void init();

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the contructor
     */
    protected abstract void registerEventListeners();

    /**
     * Id registry should be globally visible to callers
     *
     * @param key__
     * @return Element
     */
    protected final Element getElementById(final String key__) {
        final String elementId__ = Controller.GlobalHTMLIdRegistry_.get(key__);
        if (elementId__ == null) {
            throw new java.lang.NullPointerException("ELEMENT \"" + key__ + "\" CONTAINS NULL OR NO REFERENCE IN REGISTRY!");
        }
        return hTMLDocument_.getElementById(elementId__);
    }
}
