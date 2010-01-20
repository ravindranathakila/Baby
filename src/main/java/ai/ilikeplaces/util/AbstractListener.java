package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.ServletLogin;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpSession;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import java.util.Map;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public abstract class AbstractListener {

    /**
     * Use this ItsNatDocument in your code for handling the page
     */
    final protected ItsNatDocument itsNatDocument;
    /**
     *
     */
    final private ItsNatHTMLDocument itsNatHTMLDocument_;
    /**
     *
     */
    final private HTMLDocument hTMLDocument_;
    /**
     *
     */
    final private ItsNatServlet itsNatServlet_;
    /**
     *
     */
    final private ItsNatHttpSession itsNatHttpSession;
    /**
     *
     */
    final protected String location;
    /**
     *
     */
    final static protected String Click = "click";

    final private SessionBoundBadRefWrapper<HumanUserLocal> sessionBoundBadRefWrapper;


    /**
     * @param request_
     */
    @SuppressWarnings("unchecked")
    public AbstractListener(final ItsNatServletRequest request_) {
        this.itsNatDocument = request_.getItsNatDocument();
        this.itsNatHTMLDocument_ = (ItsNatHTMLDocument) itsNatDocument;
        this.hTMLDocument_ = itsNatHTMLDocument_.getHTMLDocument();
        this.itsNatServlet_ = itsNatDocument.getItsNatDocumentTemplate().getItsNatServlet();
        this.itsNatHttpSession = (ItsNatHttpSession) request_.getItsNatSession();
        final Object attribute__ = itsNatHttpSession.getAttribute(ServletLogin.HumanUser);
        this.sessionBoundBadRefWrapper = attribute__ == null ? null : (SessionBoundBadRefWrapper<HumanUserLocal>) attribute__;
        this.location = (String) request_.getServletRequest().getAttribute(RBGet.config.getString("HttpSessionAttr.location"));

        init(itsNatHTMLDocument_, hTMLDocument_, itsNatDocument);

        registerEventListeners(itsNatHTMLDocument_, hTMLDocument_, itsNatDocument);

    }

    /**
     * Intialize your document here by appending fragments
     */
    protected abstract void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__);

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the contructor
     *
     * @param itsNatHTMLDocument_
     * @param itsNatDocument__
     * @param hTMLDocument_
     */
    protected abstract void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_, final ItsNatDocument itsNatDocument__);

    /**
     * In general, avoid registering the same template again to avoid element
     * Id conflicts
     *
     * @param page_
     * @return DocumentFragment
     */
    protected DocumentFragment registerFragmentHead(final String page_) {
        return ((ItsNatHTMLDocFragmentTemplate) itsNatServlet_.getItsNatDocFragmentTemplate(page_)).loadDocumentFragmentHead(itsNatDocument);
    }

    /**
     * In general, avoid registering the same template again to avoid element
     * Id conflicts
     *
     * @param page_
     * @return DocumentFragment
     */
    protected DocumentFragment registerFragmentBody(final String page_) {
        return ((ItsNatHTMLDocFragmentTemplate) itsNatServlet_.getItsNatDocFragmentTemplate(page_)).loadDocumentFragmentBody(itsNatDocument);
    }

    /**
     * This Map is static as Id's in html documents should be universally identical, i.e. as htmldocname_elementId
     */
    final static protected Map<String, String> GlobalHTMLIdRegistry_ = Controller.GlobalHTMLIdRegistry;

    /**
     * Id registry should be globally visible to callers
     * Wrapper to getElementById
     *
     * @param iDOfElementRequired__
     * @return Element
     */
    protected final Element $(final String iDOfElementRequired__) {
        return getElementById(iDOfElementRequired__);
    }

    private final Element getElementById(final String key__) {
        final String elementId__ = GlobalHTMLIdRegistry_.get(key__);
        if (elementId__ == null) {
            throw new NullPointerException(
                    RBGet.expMsgs.getString("ai.ilikeplaces.util.AbstractListener.0001.1")
                            + key__
                            + RBGet.expMsgs.getString("ai.ilikeplaces.util.AbstractListener.0001.2"));
        }
        final Element element__ = hTMLDocument_.getElementById(elementId__);
        return element__ != null ? element__ : (Element) LogNull.logThrow();
    }

    protected final void displayBlock(final Element element__){
         element__.setAttribute("style","display:block;");
    }

    protected final void displayNone(final Element element__){
         element__.setAttribute("style","display:none;");
    }

    /**
     * @param tagNameInAllCaps
     * @return Element
     */
    final protected Element $(MarkupTagFace tagNameInAllCaps) {
        return hTMLDocument_.createElement(tagNameInAllCaps.toString());
    }

    /**
     * Get the Username of the Logged in user, and null, if not logged in
     *
     * @return The Username of the Logged in user, and null, if not logged in
     */
    final protected String getUsername() {
        return sessionBoundBadRefWrapper != null ? sessionBoundBadRefWrapper.boundInstance.getHumanUserId():null;
    }
}
