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
    final static protected String Click = "click";

    final private SessionBoundBadRefWrapper<HumanUserLocal> sessionBoundBadRefWrapper;
    private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new IllegalArgumentException("SORRY! I RECEIVED A NUMBER OF PARAMETERS WHICH I CANNOT HANDLE.");
    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("SORRY! THE CODE REFLECTING THIS CALL SHOULD ONLY WORK IF THE USER IS LOGGED IN, BUT ACTUALLY IS NOT.");


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
        this.sessionBoundBadRefWrapper =
                attribute__ == null ?
                null : (!((SessionBoundBadRefWrapper<HumanUserLocal>) attribute__).isAlive() ?
                null : ((SessionBoundBadRefWrapper<HumanUserLocal>) attribute__));

        init(itsNatHTMLDocument_, hTMLDocument_, itsNatDocument);

        registerEventListeners(itsNatHTMLDocument_, hTMLDocument_, itsNatDocument);

    }

    /**
     * Initialize your document here by appending fragments
     *
     * @param itsNatHTMLDocument__
     * @param hTMLDocument__
     * @param itsNatDocument__
     */
    protected abstract void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__);

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the constructor
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

    /**
     * Get a link element with given attributes
     *
     * @param displayText       displayText
     * @param url               URL
     * @param alt_title_classes alt attribute title of element, and space separated classes
     * @return Link Element
     */
    protected final Element $A(final String displayText, final String url, final String... alt_title_classes) {
        final Element link = $(MarkupTag.A);
        link.setTextContent(displayText);
        ElementComposer.$ElementSetHref(link, url);
        if (alt_title_classes != null) {
            switch (alt_title_classes.length) {
                case 1:
                    ElementComposer.compose(link)
                            .$ElementSetAlt(alt_title_classes[0]);
                    break;
                case 2:
                    ElementComposer.compose(link)
                            .$ElementSetAlt(alt_title_classes[0])
                            .$ElementSetTitle(alt_title_classes[1]);
                    break;
                case 3:
                    ElementComposer.compose(link)
                            .$ElementSetAlt(alt_title_classes[0])
                            .$ElementSetTitle(alt_title_classes[1])
                            .$ElementSetClasses(alt_title_classes[2]);
                    break;
                default:
                    throw ILLEGAL_ARGUMENT_EXCEPTION;
            }
        }
        return link;
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

    protected final void displayBlock(final Element element__) {
        element__.setAttribute("style", "display:block;");
    }

    protected final void displayNone(final Element element__) {
        element__.setAttribute("style", "display:none;");
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
        return sessionBoundBadRefWrapper != null ?
                (sessionBoundBadRefWrapper.isAlive() ?
                        sessionBoundBadRefWrapper.boundInstance.getHumanUserId() : null) : null;
    }

    /**
     * Get the Username of the Logged in user, or throw exception.
     * This is a call by prevention where the calls will be made to this method after one
     * validation that the user is logged in. This is the safe approach to code that assumes logged in.
     * When code gets bulky, at times calls to just getUserName might trigger null if not used in this form.
     * With this approach, we expect to throw an exception immediately instead of late discovery.
     *
     * @return The Username of the Logged in user, and null, if not logged in
     */
    final protected String getUsernameAsValid() {
        if (sessionBoundBadRefWrapper == null) {
            throw ILLEGAL_STATE_EXCEPTION;
        } else if (!sessionBoundBadRefWrapper.isAlive()) {//(Defensive)This is checked in the constructor of this class
            throw ILLEGAL_STATE_EXCEPTION;
        }
        return sessionBoundBadRefWrapper.boundInstance.getHumanUserId();
    }
}
