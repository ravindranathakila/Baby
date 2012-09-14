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
        eventName,
        eventImage,
        eventPlace,
        /**
         * This should be in  ISO 8601 date format http://en.wikipedia.org/wiki/ISO_8601 .
         * e.g. 2007-04-05T14:30 .
         * Do not screw this up, or search engines will screw us :-/ .
         */
        eventStartTime,
    }

    /**
     * @param request__
     * @param page__
     * @param t
     * @param appendToElement__
     */
    public Event(final ItsNatServletRequest request__, final EventCriteria eventCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.Event, eventCriteria, appendToElement__);
    }

    /**
     * Override this mehod at your own risk.
     * This method initializes all the fireworks of this widget.
     *
     * @param eventCriteria
     */
    @Override
    protected void init(final EventCriteria eventCriteria) {
        $$(Event.EventIds.eventName).setTextContent(criteria.getEventName());
        $$(Event.EventIds.eventStartTime).setTextContent(criteria.eventStartTime());
        new Place(request, criteria.getPlaceCriteria(), $$(Event.EventIds.eventPlace));
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

    }
}
