package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.hotspots.Hotspot;
import ai.ilikeplaces.logic.hotspots.HotspotAnalyzer;
import ai.ilikeplaces.logic.hotspots.Rawspot;
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
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                    elementToUpdateWithWOEID.setAttribute(MarkupTag.INPUT.value(), geoCoord[0].toString() + "," + geoCoord[1]);


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

//                  final Map<Integer, Map<Integer, Hotspot>> hotspots = hsa.getHotspots();
                    final Hotspot[][] hotspots = hsa.getHotspots();
                    
                    for (final Hotspot[] hotspotspitch : hotspots) {
                        for (final Hotspot yaw : hotspotspitch) {
                            final W3CPoint coords = yaw.getCoordinates();

                            if (yaw.getCoordinates() != null) {
                                itsNatDocument_.addCodeToSend("listOfHotSpots.unshift(new google.maps.Marker({ " +
                                        "position: new google.maps.LatLng("
                                        + coords.getLatitude()
                                        + ","
                                        + coords.getLongitude()
                                        + "), " +
                                        "title:'" + yaw.getCommonName() + "', " +
                                        "map: map, " +
                                        "icon: getColoredMarkerWithIntensity(" + yaw.getHits() + ")  }));");

                                $$sendJSStmt("google.maps.event.addListener(listOfHotSpots[0], 'click', function() {\n" +
                                        JSCodeToSend.redirectPageWithURL(
                                                new Parameter(Controller.Page.Organize.getURL())
                                                        .append(Controller.Page.DocOrganizeCategory, 143, true)
                                                        .append(WOEIDGrabber.WOEHINT, coords.getLatitude() + "," + coords.getLongitude())
                                                        .get()
                                        ) +
                                        "});");
                            }
                        }
                    }


//                        for (int i = 0; i < hotspots.size(); i++) {
//                            for (int j = 0; j < hotspots.get(i).size(); j++) {
//                                final W3CPoint coords = hotspots.get(i).get(j).getCoordinates();
//
//                                if (coords != null) {
//                                    itsNatDocument_.addCodeToSend("listOfHotSpots.unshift(new google.maps.Marker({ " +
//                                            "position: new google.maps.LatLng("
//                                            + coords.getLatitude()
//                                            + ","
//                                            + coords.getLongitude()
//                                            + "), " +
//                                            "title:'" + hotspots.get(i).get(j).getCommonName() + "', " +
//                                            "map: map, " +
//                                            "icon: 'http://chart.apis.google.com/chart?chst=d_simple_text_icon_below&chld=" + hotspots.get(i).get(j).getHits() + "|14|000|glyphish_map-marker|16|4bf202|892e40'  }));");
//
//                                    $$sendJSStmt("google.maps.event.addListener(listOfHotSpots[0], 'click', function() {\n" +
//                                            JSCodeToSend.redirectPageWithURL(
//                                                    new Parameter(Controller.Page.Organize.getURL())
//                                                            .append(Controller.Page.DocOrganizeCategory, 143, true)
//                                                            .append(WOEIDGrabber.WOEHINT, coords.getLatitude() + "," + coords.getLongitude())
//                                                            .get()
//                                            ) +
//                                            "});");
//                                }
//                            }
//                        }

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