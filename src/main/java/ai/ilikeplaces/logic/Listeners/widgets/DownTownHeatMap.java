package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.hotspots.Hotspot;
import ai.ilikeplaces.logic.hotspots.HotspotAnalyzer;
import ai.ilikeplaces.logic.hotspots.Rawspot;
import ai.ilikeplaces.logic.modules.Modules;
import ai.ilikeplaces.logic.validators.unit.BoundingBox;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Info;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import com.google.gdata.data.geo.impl.W3CPoint;
import net.sf.oval.Validator;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import twitter4j.org.json.JSONException;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Sep 10, 2010
 * Time: 5:07:59 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class DownTownHeatMap extends AbstractWidgetListener {

    private static final String WOEIDUPDATE_TOKEN = "WOEIDUPDATE_TOKEN";
    private static final String DownTownHeatMapWOEIDUpdate =
            "\nDownTownHeatMapWOEIDUpdate = function(lat,lng){document.getElementById('" + WOEIDUPDATE_TOKEN + "').value = '' + lat + ',' + lng; document.getElementById('" + WOEIDUPDATE_TOKEN + "').focus(); return document.getElementById('" + WOEIDUPDATE_TOKEN + "');}\n";

    private static final String BBUPDATE_TOKEN = "BBUPDATE_TOKEN";
    private static final String DownTownHeatMapBBUpdate =
            "\nDownTownHeatMapBBUpdate = function(swlat,swlng,nelat,nelng){document.getElementById('" + BBUPDATE_TOKEN + "').value = '' + swlat + ',' + swlng + ',' + nelat + ',' + nelng; document.getElementById('" + BBUPDATE_TOKEN + "').focus(); return document.getElementById('" + BBUPDATE_TOKEN + "');}\n";
    private static final String COMMA = ",";
    private static final String X1 = "), ";
    private static final String TITLE = "title:'";
    private static final String MAP_MAP = "map: map, ";
    private static final String ICON_GET_COLORED_MARKER_WITH_INTENSITY = "icon: getColoredMarkerWithIntensity(";
    private static final String X2 = ")  }));";

    private static final com.google.places.api.impl.ClientFactory GOOGLE_API_CLIENT_FACTORY = Modules.getModules().getGooglePlacesAPIFactory();
    private static final String LOCATION = "location";
    private static final String RADIUS = "radius";
    private static final String PLACE_TYPES = "types";
    private static final String COMMON_PLACE_TYPES = "airport%7Camusement_park%7Caquarium%7Cart_gallery%7Catm%7Cbakery%7Cbank%7Cbar%7Cbeauty_salon%7Cbook_store%7Cbowling_alley%7Cbus_station%7Ccafe%7Ccampground%7Ccar_rental%7Ccasino%7Cchurch%7Ccity_hall%7Cclothing_store%7Cembassy%7Cfire_station%7Cflorist%7Cfood%7Cgas_station%7Cgym%7Chair_care%7Chindu_temple%7Cjewelry_store%7Clibrary%7Clodging%7Cmeal_delivery%7Cmeal_takeaway%7Cmosque%7Cmovie_rental%7Cmovie_theater%7Cmuseum%7Cnight_club%7Cpark%7Cparking%7Cpet_store%7Cplace_of_worship%7Cpolice%7Crestaurant%7Crv_park%7Cschool%7Cshoe_store%7Cshopping_mall%7Cspa%7Cstadium%7Csubway_station%7Csynagogue%7Ctaxi_stand%7Ctrain_station%7Ctravel_agency%7Cuniversity%7Czoo";
    private static final String SENSOR = "sensor";
    private static final String TRUE = "true";
    private static final String LIST_OF_HOT_SPOTS_UNSHIFT_NEW_GOOGLE_MAPS_MARKER = "listOfHotSpots.unshift(new google.maps.Marker({ ";
    private static final String POSITION_NEW_GOOGLE_MAPS_LAT_LNG = "position: new google.maps.LatLng(";
    private static final String GOOGLE_MAPS_EVENT_ADD_LISTENER_LIST_OF_HOT_SPOTS_0_CLICK_FUNCTION = "google.maps.event.addListener(listOfHotSpots[0], 'click', function() {\n";
    private static final String X3 = "', ";
    private static final String X4 = "});";
    private static final String GOOGLE_PLACES_JSON_ENDPOINT = "https://maps.googleapis.com/maps/api/place/search/json";
    private static final String FAILED_TO_FETCH_GOOGLE_PLACES_DATA = "Failed to fetch Google Places Data";


    private Element elementToUpdateWithWOEID;
    private HumanId humanId;

    /**
     * @param request__                request__
     * @param appendToElement__        appendToElement__
     * @param elementToUpdateWithWOEID elementToUpdateWithWOEID
     * @param humanId__                humanId__
     */
    public DownTownHeatMap(final ItsNatServletRequest request__, final Element appendToElement__, final Element elementToUpdateWithWOEID, final String humanId__) {
        super(request__, Controller.Page.DownTownHeatMap, appendToElement__, elementToUpdateWithWOEID, humanId__);
    }

    @Override
    protected void init(final Object... initArgs) {

        elementToUpdateWithWOEID = (Element) initArgs[0];
        humanId = new HumanId((String) initArgs[1]);

        itsNatDocument_.addCodeToSend(
                DownTownHeatMapWOEIDUpdate.replace(
                        WOEIDUPDATE_TOKEN,
                        $$(Controller.Page.DownTownHeatMapWOEID).getAttribute(MarkupTag.GENERIC.id()))
        );

        itsNatDocument_.addCodeToSend(
                DownTownHeatMapBBUpdate.replace(
                        BBUPDATE_TOKEN,
                        $$(Controller.Page.DownTownHeatMapBB).getAttribute(MarkupTag.GENERIC.id()))
        );
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
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {

        itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.DownTownHeatMapWOEID), EventType.BLUR.toString(), new EventListener() {

            final Validator v = new Validator();
            RefObj<String> woeid;

            @Override
            public void handleEvent(final Event evt_) {
                woeid = new Info(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));

                if (woeid.validate(v) == 0) {
                    elementToUpdateWithWOEID.setAttribute(MarkupTag.INPUT.value(), woeid.getObjectAsValid());
                    //
                    // clear($$(PrivateLocationCreateCNotice));
                } else {
//                    $$(PrivateLocationCreateCNotice).setTextContent(woeid.getViolationAsString());
                }
            }

            @Override
            public void finalize() throws Throwable {
                Loggers.finalized(this.getClass().getName());
                super.finalize();
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));


        itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.DownTownHeatMapBB), EventType.BLUR.toString(), new EventListener() {

            final Validator v = new Validator();
            GeoCoord geoCoordSW = new GeoCoord();
            GeoCoord geoCoordNE = new GeoCoord();
            final ItsNatDocument ind = itsNatDocument_;
            final HumanId myhumanId = humanId;

            @Override
            public void handleEvent(final Event evt_) {

                GeoCoord[] geoCoord = GeoCoord.getGeoCoordsByBounds(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));

                if (geoCoord[0].validate(v) == 0 && geoCoord[1].validate(v) == 0) {
                    elementToUpdateWithWOEID.setAttribute(MarkupTag.INPUT.value(), geoCoord[0].toString() + COMMA + geoCoord[1]);


                    final List<PrivateEvent> privateEvents__ = DB.getHumanCrudPrivateEventLocal(true).doRPrivateEventsByBoundsAsSystem(
                            geoCoord[0].getObjectAsValid().getLatitude(),
                            geoCoord[1].getObjectAsValid().getLatitude(),
                            geoCoord[0].getObjectAsValid().getLongitude(),
                            geoCoord[1].getObjectAsValid().getLongitude()).returnValue();


                    /////////////////////////////
                    final Set<Rawspot> rs = new HashSet<Rawspot>() {{
                        for (final PrivateEvent privateEvent : privateEvents__) {
                            add(
                                    new Rawspot(
                                            new W3CPoint(privateEvent.getPrivateLocation().getPrivateLocationLatitude(), privateEvent.getPrivateLocation().getPrivateLocationLongitude()),
                                            privateEvent.getPrivateLocation().getPrivateLocationName()));
                        }
                    }};

                    final BoundingBox bb = new BoundingBox().setObj(
                            geoCoord[0].getObjectAsValid().getLatitude(),
                            geoCoord[0].getObjectAsValid().getLongitude(),
                            geoCoord[1].getObjectAsValid().getLatitude(),
                            geoCoord[1].getObjectAsValid().getLongitude());

                    final HotspotAnalyzer hsa = new HotspotAnalyzer(rs, (BoundingBox) bb.validateThrowAndGetThis());

//                    final Map<Integer, Map<Integer, Hotspot>> hotspots = hsa.getHotspots();
                    final Hotspot[][] hotspots = hsa.getHotspots();

                    for (final Hotspot[] hotspotspitch : hotspots) {
                        for (final Hotspot yaw : hotspotspitch) {
                            final W3CPoint coords = yaw.getCoordinates();

                            if (yaw.getCoordinates() != null) {

                                final Double latitude = coords.getLatitude();
                                final Double longitude = coords.getLongitude();

                                final String commonName = yaw.getCommonName();

                                final long hits = yaw.getHits();

                                generateMarker(latitude, longitude, commonName, hits);

                                generateMarkerEvent(coords);
                            }
                        }
                    }


//                    for (int i = 0; i < hotspots.size(); i++) {
//                        for (int j = 0; j < hotspots.get(i).size(); j++) {
//                            final W3CPoint coords = hotspots.get(i).get(j).getCoordinates();
//
//                            if (coords != null) {
//                                final Double latitude = coords.getLatitude();
//                                final Double longitude = coords.getLongitude();
//
//                                final String commonName = hotspots.get(i).get(j).getCommonName();
//
//                                final long hits = hotspots.get(i).get(j).getHits();
//
//                                generateMarker(latitude, longitude, commonName, hits);
//
//                                generateMarkerEvent(coords);
//
//                            }
//                        }
//                    }

                    UCGetGooglePlaces:
                    {
                        try {
                            final JSONObject placesJsonObject = getGooglePlaces(
                                    (geoCoord[0].getObjectAsValid().getLatitude() +
                                            geoCoord[1].getObjectAsValid().getLatitude()) / 2,
                                    (geoCoord[0].getObjectAsValid().getLongitude() +
                                            geoCoord[1].getObjectAsValid().getLongitude()) / 2,
                                    1000, COMMON_PLACE_TYPES);

                            final JSONArray placesJsonArray = placesJsonObject.getJSONArray("results");

                            JSONObject place;
                            for (int i = 0; i < placesJsonArray.length(); i++) {
                                place = placesJsonArray.getJSONObject(i);

                                final JSONObject lngLat = place.getJSONObject("geometry").getJSONObject("location");
                                final double lat = Double.parseDouble(lngLat.getString("lat"));
                                final double lng = Double.parseDouble(lngLat.getString("lng"));

                                generateMarker(
                                        lat,
                                        lng,
                                        place.getString("name"), 10);
                                generateMarkerEvent(new W3CPoint(lat, lng));
                            }

                        } catch (final Exception e) {
                            Loggers.ERROR.error(FAILED_TO_FETCH_GOOGLE_PLACES_DATA, e);
                        }


                    }

                } else {

                }
            }

            @Override
            public void finalize() throws Throwable {
                Loggers.finalized(this.getClass().getName());
                super.finalize();
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));


    }

    private void generateMarker(Double latitude, Double longitude, String commonName, long hits) {
        itsNatDocument_.addCodeToSend(LIST_OF_HOT_SPOTS_UNSHIFT_NEW_GOOGLE_MAPS_MARKER +
                POSITION_NEW_GOOGLE_MAPS_LAT_LNG
                + latitude
                + COMMA
                + longitude
                + X1 +
                TITLE + commonName + X3 +
                MAP_MAP +
                ICON_GET_COLORED_MARKER_WITH_INTENSITY + hits + X2);
    }

    private void generateMarkerEvent(W3CPoint coords) {
        $$sendJSStmt(GOOGLE_MAPS_EVENT_ADD_LISTENER_LIST_OF_HOT_SPOTS_0_CLICK_FUNCTION +
                JSCodeToSend.redirectPageWithURL(
                        new Parameter(Controller.Page.Organize.getURL())
                                .append(Controller.Page.DocOrganizeCategory, 143, true)
                                .append(WOEIDGrabber.WOEHINT, coords.getLatitude() + COMMA + coords.getLongitude())
                                .get()
                ) +
                X4);
    }


    public static JSONObject getGooglePlaces(final double latitude, final double longitude, final long radiusInMeters, final String placeTypes) throws JSONException {
        final Map<String, String> params = new HashMap<String, String>();
        params.put(LOCATION, latitude + "," + longitude);
        params.put(RADIUS, Double.toString(radiusInMeters));
        params.put(PLACE_TYPES, placeTypes);
        params.put(SENSOR, TRUE);
        return GOOGLE_API_CLIENT_FACTORY.getInstance(GOOGLE_PLACES_JSON_ENDPOINT).get("", params);
    }
}

//                            /*
//                            //Below is the working javascript code put here for reference. Do not delete.
//                            listOfHotSpots.unshift(
//                                new google.maps.Marker({
//                                      position: myLatlng,
//                                      map: map,
//                                      title:"2",
//                                      icon:"http://chart.apis.google.com/chart?chst=d_simple_text_icon_below&chld=Point%20this%20Marker|14|000|glyphish_map-marker|16|bb77ee|892e40"
//                                })
//                            );
//                            */