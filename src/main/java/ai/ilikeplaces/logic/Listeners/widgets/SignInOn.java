package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.Loggers;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import javax.servlet.ServletRequest;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class SignInOn extends AbstractWidgetListener {
    private static final String TRUE = "true";

    private static final String ISSIGNUP = "issignup";

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public SignInOn(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final HumanId humanId, final ServletRequest request) {
        super(itsNatDocument__, Page.SignInOn, appendToElement__, humanId, isSignUp(request));
    }

    @WARNING(warning = "During work ont this class, from time to time you'll want to make it a WEB 2.0 login." +
            "However, remember that having a login on a separate servlet is better for security.  HTTPS support." +
            "Also, note that we have a policy of redirecting the user to the refer. " +
            "You will also have to make sure both online and offline modes are available.")
    @Override
    protected void init(final Object... initArgs) {
        final HumanId humanId = (HumanId) initArgs[0];
        @WARNING(warning = "Check for humanid first, as upon successful signup, the user is redirected to the calling address. " +
                "This means, the signup parameter might still be present.")
        final boolean signup = (Boolean) initArgs[1];
        if (humanId.validate() == 0) {
            displayBlock($$(Controller.Page.signinon_logon));
            displayNone($$(Controller.Page.signinon_login));
            displayNone($$(Controller.Page.signinon_signup));
        } else {
            if (!signup) {
                displayBlock($$(Controller.Page.signinon_login));
                displayNone($$(Controller.Page.signinon_logon));
                displayNone($$(Controller.Page.signinon_signup));
            } else {
                displayBlock($$(Controller.Page.signinon_signup));
                displayNone($$(Controller.Page.signinon_logon));
                displayNone($$(Controller.Page.signinon_login));
            }
        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
    }

    static public boolean isSignUp(final ServletRequest request) {
        final String issignup = request.getParameter(ISSIGNUP);
        return issignup != null && issignup.equals(TRUE);
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}