package ai.ilikeplaces.logic.Listeners.widgets.autoplay;

import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AIEventListener;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/13/12
 * Time: 7:16 PM
 */
public class AutoplayControls extends AbstractWidgetListener<AutoplayControlsCriteria> {


    public static enum AutoplayControlsIds implements WidgetIds {
        AutoplayControlsStop,
        AutoplayControlsPause,
        AutoplayControlsPlay,
    }

    /**
     * @param request__
     * @param appendToElement__
     */
    public AutoplayControls(final ItsNatServletRequest request__, final AutoplayControlsCriteria autoplayControlsCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.AutoplayControls, autoplayControlsCriteria, appendToElement__);
    }

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the constructor
     *
     * @param itsNatHTMLDocument_
     * @param hTMLDocument_
     */
    @Override
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {
        super.registerForClick(
                AutoplayControlsIds.AutoplayControlsStop,
                new AIEventListener<AutoplayControlsCriteria>(criteria) {

                });

        super.registerForClick(
                AutoplayControlsIds.AutoplayControlsStop,
                new AIEventListener<AutoplayControlsCriteria>(criteria) {
                });

        super.registerForClick(
                AutoplayControlsIds.AutoplayControlsStop,
                new AIEventListener<AutoplayControlsCriteria>(criteria) {
                });
    }
}
