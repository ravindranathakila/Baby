package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateLocationView extends AbstractWidgetListener {


    private static final String PLV_LIST_ITEM = "PLVListItem";
    private static final String NO_MOMENTS_STARTED_HERE_YET = "No moments started here yet!";
    private static final String START_A_MOMENT = "START A MOMENT!";
    private static final String START_A_MOMENT_LINK = "start_a_moment_link";
    private static final String PRIVATE_EVENT_CREATE_ANCHOR = "#PrivateEventCreateAnchor";
    private static final String TEXT_DECORATION_UNDERLINE = "text-decoration:underline;";
    final private Logger logger = LoggerFactory.getLogger(PrivateLocationView.class.getName());

    /**
     * @param privateLocationId__
     * @param appendToElement__
     */
    public PrivateLocationView(final ItsNatServletRequest request__, final Element appendToElement__, final String humanId__, final long privateLocationId__) {
        super(request__, Page.PrivateLocationView, appendToElement__, humanId__, privateLocationId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        final HumanId humanId = new HumanId((String) initArgs[0]).getSelfAsValid();
        final long privateLocationId = (Long) initArgs[1];

        final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationAsViewer(humanId, privateLocationId);


        LoggerFactory.getLogger(PrivateLocationView.class.getName()).debug(r.toString());

        if (r.returnStatus() == 0) {
            $$(privateLocationViewName).setTextContent(r.returnValue().getPrivateLocationName());
            $$(privateLocationViewInfo).setTextContent(r.returnValue().getPrivateLocationInfo());
            final String privateLocationLink = new Parameter(Organize.getURL())
                    .append(DocOrganizeCategory, 2, true)
                    .append(DocOrganizeLocation, r.returnValue().getPrivateLocationId())
                    .get();
            setLink:
            {
                $$(privateLocationViewLink).setAttribute(MarkupTag.A.href(),
                        privateLocationLink);
            }

            setTitleLink:
            {
                $$(privateLocationViewName).setAttribute(MarkupTag.GENERIC.onclick(), JSCodeToSend.redirectPageWithURL(privateLocationLink));
            }


            final Set<Long> notifyingWalls;
            GetHumansWallNotificationSubscriptions:
            {
                final List<Wall> notifyingWallEntities = DB.getHumanCRUDHumansUnseenLocal(false).readEntries(humanId.getObjectAsValid());

                notifyingWalls = new HashSet<Long>(notifyingWallEntities.size());
                for (final Wall wall : notifyingWallEntities) {
                    notifyingWalls.add(wall.getWallId());
                }
            }


            SetEventList:
            {
                for (final PrivateEvent pe : r.returnValue().getPrivateEvents()) {
                    if (DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventIsViewer(humanId, pe.getPrivateEventId()).returnValue()) {
                        final Long wallId = pe.getPrivateEventWall().getWallId();

                        final Element eventHref = ElementComposer.compose($$(MarkupTag.DIV))
                                .$ElementSetClasses(PLV_LIST_ITEM)
                                .wrapThis(
                                        ElementComposer.compose($$(MarkupTag.A))
                                                .$ElementSetText(pe.getPrivateEventName())
                                                .$ElementSetHref(
                                                        new Parameter(Organize.getURL())
                                                                .append(DocOrganizeCategory, DocOrganizeModeEvent, true)
                                                                .append(DocOrganizeLocation, r.returnValue().getPrivateLocationId())
                                                                .append(DocOrganizeEvent, pe.getPrivateEventId())
                                                                .get()
                                                )
                                                .get()
                                )
                                .get();


                        $$(privateLocationViewEventList).appendChild(eventHref);

                        new Notification(request, new NotificationCriteria(new NotificationActionJS(EventType.CLICK, "alert('Notified!');"), notifyingWalls.contains(pe.getPrivateEventWall().getWallId()) ? "!!!" : ""), eventHref);

                    }
                }
                if (r.returnValue().getPrivateEvents().size() == 0) {
                    if (r.returnValue().getPrivateLocationOwners().contains(humanId)) {
                        $$(privateLocationViewEventList).appendChild(
                                ElementComposer.compose($$(MarkupTag.A))
                                        .$ElementSetHref(privateLocationLink + PRIVATE_EVENT_CREATE_ANCHOR)
                                        .$ElementSetText(START_A_MOMENT)
                                        .$ElementSetClasses(START_A_MOMENT_LINK)
                                        .$ElementSetAttribute(MarkupTag.A.style(), TEXT_DECORATION_UNDERLINE)
                                        .get());
                    } else {
                        $$(privateLocationViewEventList).appendChild(
                                ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText(NO_MOMENTS_STARTED_HERE_YET)
                                        .get());
                    }
                }
            }


            SetLocationMap:
            {
                final GeoCoord gc = new GeoCoord();
                Loggers.info(r.returnValue().toString());
                gc.setObj(r.returnValue().getPrivateLocationLatitude(), r.returnValue().getPrivateLocationLongitude());
                gc.validateThrow();

                $$(privateLocationViewLocationMap).setAttribute(MarkupTag.IMG.src(),
                        new Parameter("http://maps.google.com/maps/api/staticmap")
                                .append("sensor", "false", true)
                                .append("center", gc.toString())
                                .append("size", "230x250")
                                .append("format", "jpg")
                                .append("markers", "color:0x7fe2ff|label:S|path=fillcolor:0xAA000033|color:0xFFFFFF00|"
                                        + gc.toString())
                                .get());
            }
        } else {
            LoggerFactory.getLogger(PrivateLocationView.class.getName()).debug("Error");
            $$(privateLocationViewNotice).setTextContent("Alert: " + r.returnMsg());
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        //No events as this is just a view widget/ Edit button???
    }
}