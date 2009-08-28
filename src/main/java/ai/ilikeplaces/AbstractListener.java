package ai.ilikeplaces;

import ai.ilikeplaces.servlets.Controller;
import java.util.Map;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.Element;

/**
 *
 * @author Ravindranath Akila
 */
public abstract class AbstractListener {

    /**
     * Use this ItsNatDocument in your code for handling the page
     */
    protected final ItsNatDocument itsNatDocument_;
    protected final ItsNatHTMLDocument itsNatHTMLDocument_;
    protected final HTMLDocument hTMLDocument_;
    protected final ItsNatServlet itsNatServlet_;
    protected final String location_;
    protected final static String Click = "click";

    public AbstractListener(final ItsNatServletRequest request_) {
        this.itsNatDocument_ = request_.getItsNatDocument();
        this.itsNatHTMLDocument_ = (ItsNatHTMLDocument) itsNatDocument_;
        this.hTMLDocument_ = itsNatHTMLDocument_.getHTMLDocument();
        this.itsNatServlet_ = itsNatDocument_.getItsNatDocumentTemplate().getItsNatServlet();
        this.location_ = (String) request_.getServletRequest().getAttribute("location");
        System.out.println("LOCATION:3" + location_);
        init();
        registerEventListeners();
    }

    /**
     * Intialize your document here by appending fragments
     */
    protected abstract void init();

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the contructor
     */
    protected abstract void registerEventListeners();

    /**
     * In general, avoid registering the same template again to avoid element
     * Id conflicts
     * @param page_
     * @return DocumentFragment
     */
    protected DocumentFragment registerFragmentHead(final String page_) {
        final ItsNatHTMLDocFragmentTemplate inhdft_ = (ItsNatHTMLDocFragmentTemplate) itsNatServlet_.getItsNatDocFragmentTemplate(page_);
        return inhdft_.loadDocumentFragmentHead(itsNatDocument_);
    }

    /**
     * In general, avoid registering the same template again to avoid element
     * Id conflicts
     * @param page_
     * @return DocumentFragment
     */
    protected DocumentFragment registerFragmentBody(final String page_) {
        final ItsNatHTMLDocFragmentTemplate inhdft_ = (ItsNatHTMLDocFragmentTemplate) itsNatServlet_.getItsNatDocFragmentTemplate(page_);
        return inhdft_.loadDocumentFragmentBody(itsNatDocument_);
    }
    /**
     * This Map is static as Id's in html documents should be universally identical, i.e. as htmldocname_elementId
     */
    protected final static Map<String, String> GlobalHTMLIdRegistry_ = Controller.GlobalHTMLIdRegistry_;//new IdentityHashMap<String, String>();

    /**
     * Id registry should be globally visible to callers
     *
     * @param key__ 
     * @return Element
     */
    protected final Element getElementById(final String key__) {
        final String elementId__ = GlobalHTMLIdRegistry_.get(key__);
        if (elementId__ == null) {
            throw new java.lang.NullPointerException("ELEMENT \"" + key__ + "\" CONTAINS NULL OR NO REFERENCE IN REGISTRY!");
        }
        return hTMLDocument_.getElementById(elementId__);
    }
}
