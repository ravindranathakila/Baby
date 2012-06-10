package ai.ilikeplaces.logic.Listeners.widgets.schema.thing;

import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.MarkupTag;
import ai.ilikeplaces.util.Parameter;
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

    public static final String PAGE = "/page/";

    public static enum PlaceIds implements WidgetIds {
        //Place
        placeNamePre,
        placeName,
        placeNamePost,
        placeLng,
        placeLat,
        //Parent Place
        placeSuperNamePre,
        placeSuperName,
        placeSuperNamePost,
        placeSuperLng,
        placeSuperLat,
        //Parent Place Link
        placeSuperLink
    }

    /**
     * @param request__
     * @param page__
     * @param t
     * @param appendToElement__
     */
    public Place(final ItsNatServletRequest request__, final PlaceCriteria placeCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.Place, placeCriteria, appendToElement__);

        UC_LOCATION:
        {
            $$(PlaceIds.placeNamePre).setTextContent(criteria.getPlaceNamePre());
            $$(PlaceIds.placeName).setTextContent(criteria.getPlaceName());
            $$(PlaceIds.placeNamePost).setTextContent(criteria.getPlaceNamePost());

            $$(PlaceIds.placeLat).setAttribute(MarkupTag.META.content(), placeCriteria.getPlaceLat());
            $$(PlaceIds.placeLng).setAttribute(MarkupTag.META.content(), placeCriteria.getPlaceLng());
        }

        UC_SUPER_LOCATION:
        {
            $$(PlaceIds.placeSuperNamePre).setTextContent(criteria.getPlaceSuperNamePre());
            $$(PlaceIds.placeSuperName).setTextContent(criteria.getPlaceSuperName());
            $$(PlaceIds.placeSuperNamePost).setTextContent(criteria.getPlaceSuperNamePost());

            $$(PlaceIds.placeSuperLat).setAttribute(MarkupTag.META.content(), placeCriteria.getPlaceSuperLat());
            $$(PlaceIds.placeSuperLng).setAttribute(MarkupTag.META.content(), placeCriteria.getPlaceSuperLng());
        }

        UC_SUPER_LOCATION_LINK:
        {
            $$(PlaceIds.placeSuperLink).setAttribute(MarkupTag.A.href(),
                    PAGE
                            + placeCriteria.getPlaceSuperName()
                            + Parameter.get(Location.WOEID, placeCriteria.getPlaceSuperWOEID(), true));
        }
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
