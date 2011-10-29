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
public class TribeWidget extends AbstractWidgetListener<TribeWidgetCriteria> {

    public TribeWidget(final ItsNatServletRequest request__, final TribeWidgetCriteria tribeWidgetCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.TribeHome, tribeWidgetCriteria, appendToElement__);
    }


    /**
     * Use this only in conjunction with {@link #AbstractWidgetListener(org.itsnat.core.ItsNatServletRequest, ai.ilikeplaces.servlets.Controller.Page, Object, org.w3c.dom.Element)}
     * GENERIC constructor.
     *
     * @param tribeWidgetCriteria
     */
    @Override
    protected void init(final TribeWidgetCriteria tribeWidgetCriteria) {
        new WallWidgetTribe(request, new WallWidgetTribeCriteria().setHumanId(criteria.getHumanId()).setTribeId(criteria.getTribeId().getObj()), $$(Controller.Page.tribeHomeWall));
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
    }
}
