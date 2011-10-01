package ai.ilikeplaces.logic.Listeners.widgets.privateevent;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.HumansPrivateEvent;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.Listeners.widgets.AlbumManager;
import ai.ilikeplaces.logic.Listeners.widgets.Button;
import ai.ilikeplaces.logic.Listeners.widgets.UserProperty;
import ai.ilikeplaces.logic.Listeners.widgets.WallWidgetPrivateEvent;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.filters.ProfileRedirect;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateEventView extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(PrivateEventView.class.getName());
    private static final String ARROW_RIGHT_GIF = "arrow-right.gif";
    private static final String OWNER = "Owner";
    private static final String VISITOR = "Visitor";
    private static final String INVITEE = "Invitee";
    private static final String TALK = "Get in touch";

    /**
     * @param request__
     * @param appendToElement__
     * @param humanId__
     * @param privateEventId__
     * @param detailedMode__
     */
    public PrivateEventView(final ItsNatServletRequest request__, final Element appendToElement__, final String humanId__, final long privateEventId__, final boolean detailedMode__) {
        super(request__, Page.PrivateEventView, appendToElement__, humanId__, privateEventId__, detailedMode__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        final HumanId humanId = new HumanId((String) initArgs[0]);
        final long privateEventId = (Long) initArgs[1];

        @WARNING("Underlying widgets expect a fully refreshed PrivateEvent with respect to owners, visitors and viewers")
        final Return<PrivateEvent> r = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventAsAny(humanId.getObjectAsValid(), privateEventId);


        LoggerFactory.getLogger(PrivateEventView.class.getName()).debug(r.toString());

        if (r.returnStatus() == 0) {
            $$(privateEventViewName).setTextContent(r.returnValue().getPrivateEventName());
            $$(privateEventViewInfo).setTextContent(r.returnValue().getPrivateEventInfo());
            new Button(request, $$(privateEventViewLink), "Link to " + r.returnValue().getPrivateEventName(), false, r.returnValue()) {
                PrivateEvent privateEvent = null;

                @Override
                protected void init(final Object... initArgs) {
                    privateEvent = (PrivateEvent) (((Object[]) initArgs[2])[0]);
                    SetLocationLink:
                    {
                        setLink:
                        {
                            $$(GenericButtonLink).setAttribute(MarkupTag.A.href(),
                                    new Parameter(Organize.getURL())
                                            .append(DocOrganizeCategory, DocOrganizeModeEvent, true)
                                            .append(DocOrganizeLocation, r.returnValue().getPrivateLocation().getPrivateLocationId())
                                            .append(DocOrganizeEvent, privateEvent.getPrivateEventId())
                                            .get()
                            );
                        }
                        setImage:
                        {
                            $$(GenericButtonImage).setAttribute(MarkupTag.IMG.src(), RBGet.globalConfig.getString(RBGet.url_CDN_STATIC) + ARROW_RIGHT_GIF);
                        }
                    }
                }
            };
            if ((Boolean) initArgs[2]) {
                new WallWidgetPrivateEvent(request, $$(Page.privateEventViewWall), humanId, r.returnValue().getPrivateEventId());
                new AlbumManager(request, $$(Controller.Page.privateEventViewAlbum), humanId, r.returnValue());

                final GeoCoord gc = new GeoCoord();
                gc.setObj(r.returnValue().getPrivateLocation().getPrivateLocationLatitude() + "," + r.returnValue().getPrivateLocation().getPrivateLocationLongitude());
                gc.validateThrow();

                $$(Controller.Page.privateEventViewLocationMap).setAttribute(MarkupTag.IMG.src(),
                        new Parameter("http://maps.google.com/maps/api/staticmap")
                                .append("sensor", "false", true)
                                .append("center", gc.toString())
                                .append("size", "230x250")
                                .append("format", "jpg")
                                .append("markers", "color:0x7fe2ff|label:S|path=fillcolor:0xAA000033|color:0xFFFFFF00|"
                                        + gc.toString())
                                .get());
            }

            UCShowOwners:
            {
                for (final HumansPrivateEvent hpe : r.returnValue().getPrivateEventOwners()) {
                    new UserProperty(request, $$(Controller.Page.privateEventViewOwners), new HumanId(hpe.getHumanId())) {
                        protected void init(final Object... initArgs) {
                            $$(Controller.Page.user_property_sidebar_content).appendChild(
                                    ElementComposer.compose($$(MarkupTag.A)).$ElementSetText(TALK).$ElementSetHref(ProfileRedirect.PROFILE_URL + hpe.getHumanId()).get()
                            );
                        }
                    };
                }
            }

            UCShowViewers:
            {
                for (final HumansPrivateEvent hpe : r.returnValue().getPrivateEventViewers()) {
                    new UserProperty(request, $$(Controller.Page.privateEventViewVisitors), new HumanId(hpe.getHumanId())) {
                        protected void init(final Object... initArgs) {
                            $$(Controller.Page.user_property_sidebar_content).appendChild(
                                    ElementComposer.compose($$(MarkupTag.A)).$ElementSetText(TALK).$ElementSetHref(ProfileRedirect.PROFILE_URL + hpe.getHumanId()).get()
                            );
                        }
                    };
                }
            }

            UCShowInvites:
            {
                for (final HumansPrivateEvent hpe : r.returnValue().getPrivateEventInvites()) {
                    new UserProperty(request, $$(Controller.Page.privateEventViewInvites), new HumanId(hpe.getHumanId())) {
                        protected void init(final Object... initArgs) {
                            $$(Controller.Page.user_property_content).setTextContent(INVITEE);
                        }
                    };
                }
            }

        } else {
            $$(privateEventViewNotice).setTextContent(r.returnMsg());
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
    }

}