package ai.ilikeplaces.logic.Listeners.widgets.schema.thing;

import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 6/3/12
 * Time: 12:05 PM
 */
public class Event extends AbstractWidgetListener<EventCriteria> {

    public enum EventIds implements WidgetIds {

    }

    /**
     * @param request__
     * @param page__
     * @param t
     * @param appendToElement__
     */
    public Event(final ItsNatServletRequest request__, final Controller.Page page__, final EventCriteria eventCriteria, final Element appendToElement__) {
        super(request__, page__, eventCriteria, appendToElement__);
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
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
