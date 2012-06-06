package ai.ilikeplaces.logic.Listeners.widgets.schema.thing;

import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.MarkupTag;
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
public class Place extends AbstractWidgetListener<PlaceCriteria> {

    public static enum PlaceIds implements WidgetIds {
        placeNamePre,
        placeName,
        placeNamePost,
        placeLng,
        placeLat
    }

    /**
     * @param request__
     * @param page__
     * @param t
     * @param appendToElement__
     */
    public Place(final ItsNatServletRequest request__, final PlaceCriteria placeCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.Place, placeCriteria, appendToElement__);

        $$(PlaceIds.placeNamePre).setTextContent(criteria.getPlaceNamePre());
        $$(PlaceIds.placeName).setTextContent(criteria.getPlaceName());
        $$(PlaceIds.placeNamePost).setTextContent(criteria.getPlaceNamePost());

        $$(PlaceIds.placeLat).setAttribute(MarkupTag.META.content(),placeCriteria.getPlaceLat());
        $$(PlaceIds.placeLng).setAttribute(MarkupTag.META.content(),placeCriteria.getPlaceLng());
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
