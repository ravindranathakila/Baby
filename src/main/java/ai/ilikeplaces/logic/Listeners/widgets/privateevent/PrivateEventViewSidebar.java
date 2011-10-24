package ai.ilikeplaces.logic.Listeners.widgets.privateevent;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.*;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.jpa.RefreshSpec;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.List;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
public class PrivateEventViewSidebar extends AbstractWidgetListener<PrivateEventViewSidebarCriteria> {


    public PrivateEventViewSidebar(final ItsNatServletRequest request__, final PrivateEventViewSidebarCriteria privateEventViewSidebarCriteria, final Element appendToElement__) {
        super(request__, Page.PrivateEventViewSidebar, privateEventViewSidebarCriteria, appendToElement__);
    }

    /**
     * @param privateEventViewSidebarCriteria
     *
     */
    @Override
    protected void init(final PrivateEventViewSidebarCriteria privateEventViewSidebarCriteria) {
        final HumanId humanId = new HumanId(privateEventViewSidebarCriteria.getHumanId__());
        final long privateEventId = privateEventViewSidebarCriteria.getPrivateEventId__();

        final Return<PrivateEvent> r = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventInfoAsAny(humanId.getObjectAsValid(), privateEventId);


        LoggerFactory.getLogger(PrivateEventViewSidebar.class.getName()).debug(r.toString());

        if (r.returnStatus() == 0) {

//            final Obj<Long> wallId = (Obj<Long>) new Obj<Long>().setObjAsValid(r.returnValue().getPrivateEventWall().getWallId()).getSelfAsValid();
//
//            final List<Msg> msgs = DB.getHumanCrudPrivateEventLocal(false).readWallLastEntries(
//                    humanId,
//                    wallId,
//                    1,
//                    new RefreshSpec()).returnValue();

            final Msg lastWallEntry = WallWidgetPrivateEvent.LAST_WALL_ENTRY.get(r.returnValue().getPrivateEventWall().getWallId(), humanId.getHumanId());

            final String href = new Parameter(Organize.getURL())
                    .append(DocOrganizeCategory, DocOrganizeModeEvent, true)
                    .append(DocOrganizeLocation, r.returnValue().getPrivateLocation().getPrivateLocationId())
                    .append(DocOrganizeEvent, r.returnValue().getPrivateEventId())
                    .get();


            final GeoCoord gc = new GeoCoord();
            gc.setObj(r.returnValue().getPrivateLocation().getPrivateLocationLatitude() + "," + r.returnValue().getPrivateLocation().getPrivateLocationLongitude());
            gc.validateThrow();
            $$(Page.private_event_view_sidebar_name).setTextContent(r.returnValue().getPrivateEventName());

            $$(Page.private_event_view_sidebar_profile_photo).setAttribute(MarkupTag.IMG.title(),
                    new Parameter("http://maps.google.com/maps/api/staticmap")
                            .append("sensor", "false", true)
                            .append("center", gc.toString())
                            .append("size", "122x200")
                            .append("format", "jpg")
                            .append("markers", "color:0x7fe2ff|label:S|path=fillcolor:0xAA000033|color:0xFFFFFF00|"
                                    + gc.toString())
                            .get());

            if (lastWallEntry != null) {
                new UserPropertySidebar(
                        request,
                        $$(Page.private_event_view_sidebar_content),
                        new HumanId(lastWallEntry.getMsgMetadata()),
                        lastWallEntry,
                        href) {

                    private Msg mylastWallEntry;
                    private String myhref;

                    protected void init(final Object... initArgs) {

                        mylastWallEntry = (Msg) ((Object[]) initArgs[1])[0];
                        myhref = (String) ((Object[]) initArgs[1])[1];

                        $$displayBlock($$(Controller.Page.user_property_sidebar_talk));
                        Element commentHref = ElementComposer.compose($$(MarkupTag.A)).$ElementSetText(mylastWallEntry.getMsgContent()).$ElementSetHref(myhref).get();
                        $$(Controller.Page.user_property_sidebar_content).appendChild(commentHref);
                    }

                    @Override
                    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

                        itsNatHTMLDocument_.addEventListener((EventTarget) $$(user_property_sidebar_talk), EventType.CLICK.toString(), new EventListener() {
                            @Override
                            public void handleEvent(final Event evt_) {
                                $$sendJS(JSCodeToSend.redirectPageWithURL(myhref));
                            }
                        }, false);
                    }
                };
            }

        }
    }

    @Override
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

    }
}