package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.*;
import ai.ilikeplaces.exception.ExceptionConstructorInvokation;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.ServletLogin;
import java.util.Map;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpSession;
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
    final protected ItsNatDocument itsNatDocument_;
    /**
     *
     */
    final protected ItsNatHTMLDocument itsNatHTMLDocument_;
    /**
     *
     */
    final protected HTMLDocument hTMLDocument_;
    /**
     *
     */
    final protected ItsNatServlet itsNatServlet_;
    /**
     *
     */
    final protected  ItsNatHttpSession itsNatHttpSession;
    /**
     *
     */
    final protected String location_;
    /**
     *
     */
    final static protected String Click = "click";
    /**
     *
     */
    final protected SBLoggedOnUserFace sBLoggedOnUserFace;

    /**
     *
     * @param request_
     */
    public AbstractListener(final ItsNatServletRequest request_) {
        boolean initializeFailed = true;
        final StringBuilder log = new StringBuilder();
        init:
        {

            this.itsNatDocument_ = request_.getItsNatDocument();
            this.itsNatHTMLDocument_ = (ItsNatHTMLDocument) itsNatDocument_;
            this.hTMLDocument_ = itsNatHTMLDocument_.getHTMLDocument();
            this.itsNatServlet_ = itsNatDocument_.getItsNatDocumentTemplate().getItsNatServlet();
            this.itsNatHttpSession = (ItsNatHttpSession) request_.getItsNatSession();
            this.sBLoggedOnUserFace = (SBLoggedOnUserFace) itsNatHttpSession.getAttribute(ServletLogin.SBLoggedOnUser);
            this.location_ = (String) request_.getServletRequest().getAttribute("location");
            init();
            registerEventListeners(itsNatHTMLDocument_, hTMLDocument_, itsNatDocument_);

            /**
             * break. Do not let this statement be reachable if initialization
             * failed. Instead, break immediately where initialization failed.
             * At this point, we set the initializeFailed to false and thereby,
             * allow initialization of an instance
             */
            initializeFailed = false;
        }
        if (initializeFailed) {
            throw new ExceptionConstructorInvokation(log.toString());
        }
    }

    /**
     * Intialize your document here by appending fragments
     */
    protected abstract void init();

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the contructor
     * @param itsNatHTMLDocument_
     * @param itsNatDocument__
     * @param hTMLDocument_
     */
    protected abstract void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_, final ItsNatDocument itsNatDocument__);

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
    protected final static Map<String, String> GlobalHTMLIdRegistry_ = Controller.GlobalHTMLIdRegistry;

    /**
     * Id registry should be globally visible to callers
     *
     * @param key__ 
     * @return Element
     */
    protected final Element getElementById(final String key__) {
        final String elementId__ = GlobalHTMLIdRegistry_.get(key__);
        if (elementId__ == null) {
            throw new java.lang.NullPointerException("THE ELEMENT \"" + key__ + "\" YOU REQUIRE CONTAINS NULL OR NO REFERENCE IN REGISTRY!");
        }
        return hTMLDocument_.getElementById(elementId__);
    }
}
