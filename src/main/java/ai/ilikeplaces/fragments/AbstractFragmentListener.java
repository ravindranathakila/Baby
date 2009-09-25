package ai.ilikeplaces.fragments;

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
public abstract class AbstractFragmentListener {

    /**
     *
     */
    /**
     *
     */
    protected final static String Click = "click";

    /**
     *
     */
    /**
     *
     */
    public AbstractFragmentListener() {
        registerEventListeners();
    }

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the contructor
     */
    protected abstract void registerEventListeners();
}
