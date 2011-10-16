package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;
import ai.ilikeplaces.logic.Listeners.widgets.DownTownFlowCriteria;

import static ai.ilikeplaces.logic.Listeners.widgets.DownTownFlowCriteria.DownTownFlowDisplayComponent;
import static ai.ilikeplaces.logic.Listeners.widgets.DownTownFlowCriteria.DownTownFlowDisplayComponent.MOMENTS;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/16/11
 * Time: 2:28 PM
 */
public class DownTownFlow extends AbstractWidgetListener<DownTownFlowCriteria> {
// --------------------------- CONSTRUCTORS ---------------------------

    public DownTownFlow(final ItsNatServletRequest request__, final DownTownFlowCriteria downTownFlowCriteria__, final Element appendToElement__) {
        super(request__, Controller.Page.DownTownFlow, downTownFlowCriteria__, appendToElement__);

        switch (downTownFlowCriteria__.getDownTownFlowDisplayComponent()) {
            case TALKS: {
                $$displayBlock($$(Controller.Page.DownTownFlowTalks));
                break;
            }

            case MOMENTS: {
                $$displayBlock($$(Controller.Page.DownTownFlowMoments));
                break;
            }
            default: {
            }
        }

    }

    // ------------------------ OVERRIDING METHODS ------------------------
    @Override
    protected void init(final DownTownFlowCriteria downTownFlowCriteria) {

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {

    }
}
