package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Info;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
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

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
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
     * @param request__
     * @param appendToElement__
     * @param elementToUpdateWithWOEID
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


                    if (myhumanId.validate() == 0) {
                        final List<PrivateEvent> privateEvents__ = DB.getHumanCrudPrivateEventLocal(true).doRPrivateEventsByBounds(
                                myhumanId,
                                geoCoord[0].getObjectAsValid().getLatitude(),
                                geoCoord[1].getObjectAsValid().getLatitude(),
                                geoCoord[0].getObjectAsValid().getLongitude(),
                                geoCoord[1].getObjectAsValid().getLongitude()).returnValue();

                        for (final PrivateEvent privateEvent : privateEvents__) {
                            itsNatDocument_.addCodeToSend(" new google.maps.Marker({ position: new google.maps.LatLng("
                                    + privateEvent.getPrivateLocation().getPrivateLocationLatitude()
                                    + ","
                                    + privateEvent.getPrivateLocation().getPrivateLocationLongitude()
                                    + "),map: map, icon: markerIcon});");
                        }
                    }

                    // markerShadow,
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


    }
}
