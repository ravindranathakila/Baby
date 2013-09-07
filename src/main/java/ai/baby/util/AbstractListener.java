package ai.baby.util;

import ai.ilikeplaces.entities.etc.HumanId;
import ai.baby.logic.role.HumanUser;
import ai.baby.logic.role.HumanUserLocal;
import ai.baby.rbs.RBGet;
import ai.baby.servlets.Controller;
import ai.scribble.License;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpSession;
import org.itsnat.core.tmpl.ItsNatHTMLDocFragmentTemplate;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLDocument;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public abstract class
        AbstractListener {

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
    final protected ItsNatHttpSession itsNatHttpSession;

    /**
     *
     */
    final static protected String Click = "click";

    final private SessionBoundBadRefWrapper<HumanUserLocal> sessionBoundBadRefWrapper;
    private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new IllegalArgumentException("SORRY! I RECEIVED A NUMBER OF PARAMETERS WHICH I CANNOT HANDLE.");

    final protected SmartLogger sl;
    private static final String INITIALIZING_LISTENER = "Initializing page";

    /**
     * @param request_
     */
    @SuppressWarnings("unchecked")
    public AbstractListener(final ItsNatServletRequest request_, final ItsNatServletResponse response_, final Object... initArgs) {
        //sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, INITIALIZING_LISTENER, 10000, null, true);
        sl = SmartLogger.g();
        this.itsNatDocument = request_.getItsNatDocument();

        itsNatDocument.addEventListener(new EventListener() {
            @Override
            public void handleEvent(Event evt) {
                /*itsNatDocument_.addCodeToSend("\n" + "alert("
                        + "'"
                        + itsNatDocument_.getItsNatDocumentTemplate().getName()
                        + ","
                        + ((Element) evt.getCurrentTarget()).getAttribute(MarkupTag.GENERIC.id()).replaceAll("\\d*$", "")
                        + ","
                        + evt.getType()
                        + "'"
                        + ");" + "\n");*/

                if (evt.getCurrentTarget() instanceof Element) {
                    itsNatDocument.addCodeToSend("_gaq.push(['_trackEvent', '" + ((ItsNatEvent) evt).getItsNatDocument().getItsNatDocumentTemplate().getName() + "', '" + evt.getType() + "', '" + ((Element) evt.getCurrentTarget()).getAttribute(MarkupTag.GENERIC.id()).replaceAll("\\d*$", "") + "']);");
                }
            }
        });

        this.itsNatHTMLDocument_ = (ItsNatHTMLDocument) itsNatDocument;
        this.hTMLDocument_ = itsNatHTMLDocument_.getHTMLDocument();
        this.itsNatServlet_ = itsNatDocument.getItsNatDocumentTemplate().getItsNatServlet();
        this.itsNatHttpSession = (ItsNatHttpSession) request_.getItsNatSession();
        final Object attribute__ = itsNatHttpSession.getAttribute(Controller.HumanUser);
        this.sessionBoundBadRefWrapper =
                attribute__ == null ?
                        null : (!((SessionBoundBadRefWrapper<HumanUserLocal>) attribute__).isAlive() ?
                        null : ((SessionBoundBadRefWrapper<HumanUserLocal>) attribute__));

        try {
            init(itsNatHTMLDocument_, hTMLDocument_, itsNatDocument, initArgs);

            registerEventListeners(itsNatHTMLDocument_, hTMLDocument_, itsNatDocument);
        } catch (final Throwable re) {
            try {
                if (Loggers.DEBUG.isDebugEnabled()) {
                    Loggers.error("SORRY! ERROR FOUND IN " + this.getClass().getName() + " IMPLEMENTATION. " +
                            "YOU SHOULD DO YOUR OWN LOGGING. " +
                            "SINCE DEBUG IS ENABLED, LOGGING ERROR DETAILS TO ERROR LOGGER AS FOLLOWS. ",
                            re);
                }
                ((HttpServletResponse) response_.getServletResponse()).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (final IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

    }

    /**
     * Initialize your document here by appending fragments
     *
     * @param itsNatHTMLDocument__
     * @param hTMLDocument__
     * @param itsNatDocument__
     * @param initArgs
     */
    protected abstract void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs);

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


    private Element getElementById(final String key__) {
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


    protected final List<Node> $getElementsByName(final String name) {
        final NodeList nodes = (NodeList) LogNull.logThrow(hTMLDocument_.getElementsByName(name));
        final List<Node> nodeList = new ArrayList<Node>(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            nodeList.add(nodes.item(i));
        }
        return nodeList;
    }

    /**
     * Supports "i18n" selector.
     *
     * @param name              name as defined in the tag(s). e.g. ' div name="example" ' would require "example".
     * @param textToReplaceWith Text To Replace With
     * @return The Modified Node List
     */
    protected final List<Node> $i18nize(final String name, final String textToReplaceWith) {
        final List<Node> nodeList = $getElementsByName(name);
        for (final Node node : nodeList) {
            if (node.getTextContent().contains("#i18n")) {
                node.setTextContent(node.getTextContent().replaceAll("#i18n", textToReplaceWith));
            } else {
                node.setTextContent(textToReplaceWith);
            }
        }
        return nodeList;
    }

    /**
     * @param name           name as defined in the tag(s). e.g. ' div name="example" ' would require "example".
     * @param resourceBundle resourceBundle from which to fetch the value of key, key being the text content of the node
     * @return The Modified Node List
     */
    protected final List<Node> $i18nize(final String name, final ResourceBundle resourceBundle) {
        final List<Node> nodeList = $getElementsByName(name);
        for (final Node node : nodeList) {
            node.setTextContent(resourceBundle.getString(node.getTextContent()));
        }
        return nodeList;
    }

    /**
     * @param names          names as defined in the tag(s). e.g. ' div name="example" ' would require "example".
     * @param resourceBundle resourceBundle from which to fetch the value of key, key being the text content of the node
     * @return The Modified Node List
     */
    protected final List<Node> $i18nize(final List<String> names, final ResourceBundle resourceBundle) {
        final List<Node> allNodes = new ArrayList<Node>();
        for (final String name : names) {
            final List<Node> nodeList = $getElementsByName(name);
            for (final Node node : nodeList) {
                node.setTextContent(resourceBundle.getString(node.getTextContent()));
            }
            allNodes.addAll(nodeList);
        }
        return allNodes;
    }

    /**
     * @param name         name as defined in the tag(s). e.g. ' div name="example" ' would require "example".
     * @param i18nValueMap map from which to fetch the value of key, key being the text content of the node
     * @return The Modified Node List
     */
    protected final List<Node> $i18nize(final String name, final Map<String, Object> i18nValueMap) {
        final List<Node> nodeList = $getElementsByName(name);
        for (final Node node : nodeList) {
            node.setTextContent(i18nValueMap.get(node.getTextContent()).toString());
        }
        return nodeList;
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
                        sessionBoundBadRefWrapper.getBoundInstance().getHumanUserId() : null) : null;
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
            throw HumanUser.ILLEGAL_STATE_EXCEPTION;
        } else if (!sessionBoundBadRefWrapper.isAlive()) {//(Defensive)This is checked in the constructor of this class
            throw HumanUser.ILLEGAL_STATE_EXCEPTION;
        }
        return sessionBoundBadRefWrapper.getBoundInstance().getHumanUserId();
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
    final protected HumanId geHumanIdAsValid() {
        if (sessionBoundBadRefWrapper == null) {
            throw HumanUser.ILLEGAL_STATE_EXCEPTION;
        } else if (!sessionBoundBadRefWrapper.isAlive()) {//(Defensive)This is checked in the constructor of this class
            throw HumanUser.ILLEGAL_STATE_EXCEPTION;
        }
        return new HumanId(sessionBoundBadRefWrapper.getBoundInstance().getHumanUserId());
    }

    /**
     * Non-static wrapper for {@link ai.baby.logic.role.HumanUser#getHumanUserAsValid(SessionBoundBadRefWrapper)}
     * <p/>
     * The contents of this method were moved to the static method to make the static method avaiable globally.
     * This should later be shifted to most relevantly to {@link ai.baby.logic.role.HumanUser}
     * <p/>
     * Get the Username of the Logged in user, or throw exception.
     * This is a call by prevention where the calls will be made to this method after one
     * validation that the user is logged in. This is the safe approach to code that assumes logged in.
     * When code gets bulky, at times calls to just getUserName might trigger null if not used in this form.
     * With this approach, we expect to throw an exception immediately instead of late discovery.
     *
     * @return The HumanUserLocal of the Logged in user or throws an exception if null or not logged in
     */
    final protected HumanUserLocal getHumanUserAsValid() {
        return HumanUser.getHumanUserAsValid(sessionBoundBadRefWrapper);
    }

}
