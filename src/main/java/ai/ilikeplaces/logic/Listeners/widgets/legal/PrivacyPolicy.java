package ai.ilikeplaces.logic.Listeners.widgets.legal;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.Loggers;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class PrivacyPolicy extends AbstractWidgetListener {

    /**
     *
     * @param request__
     * @param appendToElement__
     * @param initArgs
     */
    public PrivacyPolicy(final ItsNatServletRequest request__, final Element appendToElement__, final Object... initArgs) {
        super(request__, Page.PrivacyPolicy, appendToElement__, initArgs);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}
