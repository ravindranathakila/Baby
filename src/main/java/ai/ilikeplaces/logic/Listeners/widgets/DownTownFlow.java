package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventViewSidebar;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventViewSidebarCriteria;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.filters.ProfileRedirect;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.jpa.RefreshSpec;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ai.ilikeplaces.servlets.Controller.Page.user_property_sidebar_talk;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/16/11
 * Time: 2:28 PM
 */
public class DownTownFlow extends AbstractWidgetListener<DownTownFlowCriteria> {
// --------------------------- CONSTRUCTORS ---------------------------

    public DownTownFlow(final ItsNatServletRequest request__, final DownTownFlowCriteria downTownFlowCriteria__, final Element appendToElement__) {
        super(request__, Controller.Page.DownTownFlow, downTownFlowCriteria__, appendToElement__);

        switch (downTownFlowCriteria__.getDownTownFlowDisplayComponent()) {
            case TALKS: {
                $$displayBlock($$(Controller.Page.DownTownFlowTalks));
                break;
            }

            case MOMENTS: {
                $$displayBlock($$(Controller.Page.DownTownFlowMoments));
                break;
            }
            default: {
            }
        }

    }

    // ------------------------ OVERRIDING METHODS ------------------------
    @Override
    protected void init(final DownTownFlowCriteria downTownFlowCriteria) {

        UCDownTownFlowFriends:
        {

            final String currentUser = downTownFlowCriteria.getHumanId().getObj();
            final List<HumansNetPeople> beFriends = (List<HumansNetPeople>) downTownFlowCriteria.getHumanUserLocal().cache(currentUser);

            final Set<Wall> notifiedWalls = DB.getHumanCRUDHumansUnseenLocal(false).readEntries(downTownFlowCriteria.getHumanId().getHumanId());

            final Set<Long> notifiedWallLongs = new HashSet<Long>(notifiedWalls.size());
            for (final Wall wall : notifiedWalls) {
                notifiedWallLongs.add(wall.getWallId());
            }
            for (final HumansNetPeople friend : beFriends) {

                new UserPropertySidebar(request, $$(Controller.Page.DownTownFlowTalksFriends), new HumanId(friend.getHumanId())) {
                    protected void init(final Object... initArgs) {

                        final Long friendWallId = WallWidgetHumansWall.HUMANS_WALL_ID.get(new Pair<String, String>(new String(currentUser), new String(friend.getHumanId())));

                        final Msg lastWallEntry = DB.getHumanCrudWallLocal(false).readWallLastEntries(new HumanId(friend.getHumanId()), new Obj<HumanId>(new HumanId(currentUser)), 1, new RefreshSpec()).returnValue().get(0);

                        final Element appendToElement__ = $$(Controller.Page.user_property_sidebar_content);

                        new UserPropertySidebar(request, appendToElement__, new HumanId(lastWallEntry.getMsgMetadata())) {
                            final Msg mylastWallEntry = lastWallEntry;
                            private String href;

                            protected void init(final Object... initArgs) {
                                $$displayBlock($$(Controller.Page.user_property_sidebar_talk));
                                href = ProfileRedirect.PROFILE_URL + HUMANS_IDENTITY_SIDEBAR_CACHE.get(new String(friend.getHumanId())).getUrl().getUrl();
                                Element commentHref = ElementComposer.compose($$(MarkupTag.A)).$ElementSetText(lastWallEntry.getMsgContent()).$ElementSetHref(href).get();
                                $$(Controller.Page.user_property_sidebar_content).appendChild(commentHref);
                                if (notifiedWallLongs.contains(friendWallId)) {
                                    new Notification(request, new NotificationCriteria("!!!"), commentHref);
                                }
                            }

                            @Override
                            protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

                                itsNatHTMLDocument_.addEventListener((EventTarget) $$(user_property_sidebar_talk), EventType.CLICK.toString(), new EventListener() {
                                    @Override
                                    public void handleEvent(final Event evt_) {
                                        $$sendJS(JSCodeToSend.redirectPageWithURL(href));
                                    }
                                }, false);
                            }
                        };
                    }
                };
            }
        }

        UCDownTownFlowMoments:
        {
            for (final PrivateEvent privateEvent : DB.getHumanCrudPrivateEventLocal(false).doDirtyRPrivateEventsOfHuman(downTownFlowCriteria.getHumanId()).returnValue()) {
                new PrivateEventViewSidebar(
                        request,
                        new PrivateEventViewSidebarCriteria().setPrivateEventId__(privateEvent.getPrivateEventId()).setHumanId__(downTownFlowCriteria.getHumanId().getHumanId()),
                        $$(Controller.Page.DownTownFlowMoments));
            }
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {

    }
}
