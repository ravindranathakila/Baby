package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.Validator;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AIEventListener;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.RefObj;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.NotNull;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLDocument;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/20/11
 * Time: 8:50 PM
 */
public class TribeCreateWidget extends AbstractWidgetListener<TribeCreateWidgetCriteria> {
// --------------------------- CONSTRUCTORS ---------------------------


    public TribeCreateWidget(final ItsNatServletRequest request__, final TribeCreateWidgetCriteria tribeCreateWidgetCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.TribeCreateHome, tribeCreateWidgetCriteria, appendToElement__);
    }

    /**
     * Use this only in conjunction with {@link #AbstractWidgetListener(org.itsnat.core.ItsNatServletRequest, ai.ilikeplaces.servlets.Controller.Page, Object, org.w3c.dom.Element)}
     * GENERIC constructor.
     *
     * @param tribeCreateWidgetCriteria
     */
    @Override
    protected void init(final TribeCreateWidgetCriteria tribeCreateWidgetCriteria) {

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        super.registerForInputText($$(Controller.Page.tribeHomeCreateName),
                new AIEventListener<TribeCreateWidgetCriteria>(criteria) {

                    @Override
                    public void onFire(final Event evt) {
                        this.criteria.getvTribeName().setObj(TribeCreateWidget.this.getTargetInputText(evt));
                    }
                }
        );

        super.registerForInputText($$(Controller.Page.tribeHomeCreateStory),
                new AIEventListener<TribeCreateWidgetCriteria>(criteria) {

                    @Override
                    public void onFire(final Event evt) {
                        this.criteria.getvTribeStory().setObj(TribeCreateWidget.this.getTargetInputText(evt));
                    }
                });

        super.registerForClick($$(Controller.Page.tribeHomeCreateSave),
                new AIEventListener<TribeCreateWidgetCriteria>(criteria) {

                    @Override
                    public void onFire(final Event evt) {
                        Loggers.DEBUG.debug(this.criteria.getvTribeName().getObj());
                        Loggers.DEBUG.debug(this.criteria.getvTribeStory().getObj());

                        if (this.criteria.getvTribeName().valid() && this.criteria.getvTribeStory().valid()) {
                            DB.getHumanCRUDTribeLocal(false).createTribe(criteria.getHumanId(), this.criteria.getvTribeName(), this.criteria.getvTribeStory());
                        }

                    }
                });
    }
}
