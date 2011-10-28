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
 * Date: 10/20/11
 * Time: 8:50 PM
 */
public class TribeCreateWidget extends AbstractWidgetListener<TribeCreateWidgetCriteria>{

    public TribeCreateWidget(final ItsNatServletRequest request__, final TribeCreateWidgetCriteria tribeCreateWidgetCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.TribeCreateHome, tribeCreateWidgetCriteria, appendToElement__);
    }

    @Override
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

    }
}
