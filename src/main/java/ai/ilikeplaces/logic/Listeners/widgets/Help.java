package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/22/11
 * Time: 11:03 AM
 */
public class Help extends AbstractWidgetListener<HelpCriteria> {
    public Help(final ItsNatServletRequest request__, final HelpCriteria helpCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.Help, helpCriteria, appendToElement__);
    }

    @Override
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {
    }
}
