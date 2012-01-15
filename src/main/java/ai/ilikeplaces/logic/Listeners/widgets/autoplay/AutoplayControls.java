package ai.ilikeplaces.logic.Listeners.widgets.autoplay;

import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.UserProperty;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventViewSidebar;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.filters.ProfileRedirect;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLDocument;

import java.util.List;
import java.util.Set;

import static ai.ilikeplaces.logic.Listeners.widgets.autoplay.AutoplayControlsCriteria.AUTOPLAY_STATE.PAUSED;
import static ai.ilikeplaces.logic.Listeners.widgets.autoplay.AutoplayControlsCriteria.AUTOPLAY_STATE.PLAYING;
import static ai.ilikeplaces.servlets.Controller.Page.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/13/12
 * Time: 7:16 PM
 */
public class AutoplayControls extends AbstractWidgetListener<AutoplayControlsCriteria> {

    public static enum AutoplayControlsIds implements WidgetIds {
        AutoplayControlsPlay,
        AutoplayControlsPause,
        AutoplayControlsPlaySection,
        AutoplayControlsPauseSection,
        AutoplayControlsStop,
        AutoplayControlsTimeout,
        AutoplayControlsPlayState,
    }

    /**
     * @param request__
     * @param appendToElement__
     */
    public AutoplayControls(final ItsNatServletRequest request__, final AutoplayControlsCriteria autoplayControlsCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.AutoplayControls, autoplayControlsCriteria, appendToElement__);
    }


    @Override
    protected void init(final AutoplayControlsCriteria autoplayControlsCriteria) {
        AutoplayControlsCriteria.AUTOPLAY_STATE autoplayState = (AutoplayControlsCriteria.AUTOPLAY_STATE) criteria.getHumanUserLocal().cacheAndUpdateWith(HumanUserLocal.CACHE_KEY.AUTOPLAY_STATE, null);
        autoplayState = (autoplayState != null ? autoplayState :
                PLAYING);
        switch (autoplayState) {
            case PLAYING: {
                $$displayBlock(AutoplayControlsIds.AutoplayControlsPauseSection);
                criteria.getHumanUserLocal().cacheAndUpdateWith(HumanUserLocal.CACHE_KEY.AUTOPLAY_STATE, PLAYING);
                $$(AutoplayControlsIds.AutoplayControlsPlayState).setAttribute(MarkupTag.INPUT.value(), Boolean.TRUE.toString());
                break;
            }
            case PAUSED: {
                $$displayBlock(AutoplayControlsIds.AutoplayControlsPlaySection);
                criteria.getHumanUserLocal().cacheAndUpdateWith(HumanUserLocal.CACHE_KEY.AUTOPLAY_STATE, PAUSED);
                $$(AutoplayControlsIds.AutoplayControlsPlayState).setAttribute(MarkupTag.INPUT.value(), Boolean.FALSE.toString());
                break;
            }
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

        super.registerForClick(
                AutoplayControlsIds.AutoplayControlsPlay,
                new AIEventListener<AutoplayControlsCriteria>(criteria) {
                    /**
                     * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
                     *
                     * @param evt fired from client
                     */
                    @Override
                    protected void onFire(Event evt) {

                        criteria.getHumanUserLocal().cacheAndUpdateWith(HumanUserLocal.CACHE_KEY.AUTOPLAY_STATE, PLAYING);
                        $$displayNone(AutoplayControlsIds.AutoplayControlsPlaySection);
                        $$displayBlock(AutoplayControlsIds.AutoplayControlsPauseSection);


                    }
                });

        super.registerForClick(
                AutoplayControlsIds.AutoplayControlsPause,
                new AIEventListener<AutoplayControlsCriteria>(criteria) {
                    /**
                     * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
                     *
                     * @param evt fired from client
                     */
                    @Override
                    protected void onFire(Event evt) {
                        criteria.getHumanUserLocal().cacheAndUpdateWith(HumanUserLocal.CACHE_KEY.AUTOPLAY_STATE, PAUSED);
                        $$displayNone(AutoplayControlsIds.AutoplayControlsPauseSection);
                        $$displayBlock(AutoplayControlsIds.AutoplayControlsPlaySection);
                    }
                });

        super.registerForClick(
                AutoplayControlsIds.AutoplayControlsStop,
                new AIEventListener<AutoplayControlsCriteria>(criteria) {

                });

        super.registerForClick(
                AutoplayControlsIds.AutoplayControlsTimeout,
                new AIEventListener<AutoplayControlsCriteria>(criteria) {
                    /**
                     * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
                     *
                     * @param evt fired from client
                     */
                    @Override
                    protected void onFire(Event evt) {

                        AutoplayControlsCriteria.AUTOPLAY_STATE autplayState = (AutoplayControlsCriteria.AUTOPLAY_STATE) criteria.getHumanUserLocal().cacheAndUpdateWith(HumanUserLocal.CACHE_KEY.AUTOPLAY_STATE, null);
                        autplayState = (autplayState != null ? autplayState :
                                PLAYING);

                        switch (autplayState) {
                            case PLAYING: {

                                final List<Wall> updatedWalls = DB.getHumanCRUDHumansUnseenLocal(false).readEntries(criteria.getHumanId().getHumanId());

                                if (!updatedWalls.isEmpty()) {
                                    final Wall hopefullyLastWall = updatedWalls.get(updatedWalls.size() - 1);

                                    if (hopefullyLastWall.getWallType() == null) {
                                        hopefullyLastWall.setWallType(Wall.wallTypePrivateEvent);
                                    }

                                    switch (hopefullyLastWall.getWallType()) {
                                        case Wall.wallTypeHuman: {
                                            final String otherHumansId = hopefullyLastWall.getMetadataValueFor(Wall.WallMetadataKey.HUMAN);
                                            final HumansIdentity humansIdentity = ai.ilikeplaces.logic.Listeners.widgets.UserProperty.HUMANS_IDENTITY_CACHE.get(otherHumansId, "");

                                            $$sendJS(JSCodeToSend.redirectPageWithURL(
                                                    ProfileRedirect.PROFILE_URL + humansIdentity.getUrl().getUrl()
                                            ));

                                            break;
                                        }
                                        case Wall.wallTypeTribe: {
                                            final String tribeIdAsString = hopefullyLastWall.getMetadataValueFor(Wall.WallMetadataKey.TRIBE);
                                            final long tribeId = Long.parseLong(tribeIdAsString);

                                            final String href = new Parameter(Tribes.getURL())
                                                    .append(DocTribesMode, DocTribesModeView, true)
                                                    .append(DocTribesWhich, tribeId)
                                                    .get();

                                            $$sendJS(JSCodeToSend.redirectPageWithURL(
                                                    href)
                                            );

                                            break;
                                        }
                                        default: {//Private Event is default. Humans set metadata
                                            final String privateEventString = hopefullyLastWall.getMetadataValueFor(Wall.WallMetadataKey.PRIVATE_EVENT);
                                            final String privatePhotoString = hopefullyLastWall.getMetadataValueFor(Wall.WallMetadataKey.PRIVATE_PHOTO);

                                            if (privateEventString != null) {
                                                final PrivateEvent privateEvent = ai.ilikeplaces.logic.Listeners.widgets.privateevent
                                                        .PrivateEventViewSidebar.PRIVATE_EVENT_BASIC_INFO_CACHE.get(Long.parseLong(privateEventString), criteria.getHumanId());


                                                final String href = new Parameter(Organize.getURL())
                                                        .append(DocOrganizeCategory, DocOrganizeModeEvent, true)
                                                        .append(DocOrganizeLocation, privateEvent.getPrivateLocation().getPrivateLocationId())
                                                        .append(DocOrganizeEvent, privateEvent.getPrivateEventId())
                                                        .get();

                                                $$sendJS(JSCodeToSend.redirectPageWithURL(href));

                                            } else if (privatePhotoString != null) {
                                                $$sendJS(JSCodeToSend.refreshPageIn(0));
                                            } else {
                                                Loggers.error(criteria.getHumanId() + " HAS AN UPDATE ON WALL " + hopefullyLastWall.toString() + " BUT CANNOT ACCESS IT SINCE ITS METADATA AND/OR TYPE ARE NOT UPDATED.");
                                            }
                                        }
                                    }
                                }

                                break;
                            }
                            case PAUSED: {
                                break;
                            }


                        }
                    }
                });
    }
}
