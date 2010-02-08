package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class SignInOn extends AbstractWidgetListener {

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public SignInOn(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final HumanId humanId) {
        super(itsNatDocument__, Page.SignInOn, appendToElement__, humanId);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        final HumanId humanId = (HumanId) initArgs[0];
        if (humanId.validate() == 0) {
            displayBlock($$(Controller.Page.signinon_logon));
            displayNone($$(Controller.Page.signinon_login));
        } else {
            displayBlock($$(Controller.Page.signinon_login));
            displayNone($$(Controller.Page.signinon_logon));
        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
    }
}