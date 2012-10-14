package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.exception.AbstractEjbApplicationRuntimeException;
import ai.ilikeplaces.logic.crud.unit.*;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.jpa.RefreshSpec;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 11:58:04 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({EntityManagerInjector.class, DBOffline.class, ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
public class HumanCRUDPrivateEvent extends AbstractSLBCallbacks implements HumanCRUDPrivateEventLocal {

    private static final String COMMA = ",";
    @EJB
    private CPrivateEventLocal cPrivateEventLocal_;

    @EJB
    private RPrivateEventLocal rPrivateEventLocal_;

    @EJB
    private UPrivateEventLocal uPrivateEventLocal_;

    @EJB
    private CRUDWallLocal crudWallLocal_;

    @EJB
    private CRUDAlbumLocal crudAlbumLocal_;

    @EJB
    private DPrivateEventLocal dPrivateEventLocal_;

    @EJB
    private RPrivateLocationLocal rPrivateLocationLocal_;

    @EJB
    private UPrivateLocationLocal uPrivateLocationLocal_;


    private static final String CHECK_OWNERSHIP_OF_PRIVATE_EVENT_SUCCESSFUL = "Check ownership of private event Successful!";
    private static final String CHECK_OWNERSHIP_OF_PRIVATE_EVENT_FAILED = "Check ownership of private event FAILED!";
    private static final String CHECK_VIEWERSHIP_OF_PRIVATE_EVENT_SUCCESSFUL = "Check viewership of private event Successful!";
    private static final String CHECK_VIEWERSHIP_OF_PRIVATE_EVENT_FAILED = "Check viewership of private event FAILED!";
    private static final String ADD_PHOTO_TO_ALBUM_SUCCESSFUL = "Add photo to album Successful!";
    private static final String ADD_PHOTO_TO_ALBUM_FAILED = "Add photo to album FAILED!";
    private static final String DELETE_PRIVATE_EVENT_FAILED = "Delete private event FAILED!";
    private static final String DELETE_PRIVATE_EVENT_SUCCESSFUL = "Delete private event Successful!";
    private static final String VIEW_PRIVATE_EVENT_FAILED = "View private event FAILED!";
    private static final String VIEW_PRIVATE_EVENT_SUCCESSFUL = "View private event Successful!";
    private static final String READ_WALL_FAILED = "Read wall FAILED!";
    private static final String UPDATE_PRIVATE_EVENT_FAILED = "Update private event FAILED!";
    private static final String UPDATE_PRIVATE_EVENT_SUCCESSFUL = "Update private event Successful!";
    private static final String READ_ALBUM_SUCCESSFUL = "Read album Successful!";
    private static final String READ_ALBUM_FAILED = "Read album FAILED!";
    private static final String VIEWER_OWNERSHIP_STATUS = "Viewer/Ownership status:";
    private static final String ADDING_USER_AS_VIEWER_TO_PRIVATE_LOCATION_OF_THIS_EVENT_AS_HE_ISN_T_ANYBODY_OF_IT = "Adding user as viewer to private location of this event as he isn't anybody of it.";
    private static final String SAVE_PRIVATE_EVENT_FAILED = "Save private event FAILED!";
    private static final String READ_WALL_SUCCESSFUL = "Read wall Successful!";
    private static final String FETCH_BOUNDED_EVENTS_SUCCESSFUL = "Fetch bounded events Successful!";
    private static final String FETCH_BOUNDED_EVENTS_FAILED = "Fetch bounded events FAILED!";


    @Override
    public Return<PrivateEvent> cPrivateEvent(final String humanId, final long privateLocationId, final String privateEventName, final String privateEventInfo, final String startDate, final String endDate) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(cPrivateEventLocal_.doNTxCPrivateEvent(humanId, privateLocationId, privateEventName, privateEventInfo, startDate, endDate), "Save private event Successful!");
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, SAVE_PRIVATE_EVENT_FAILED, true);
        }
        return r;

    }


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public Return<PrivateEvent> uPrivateEventAddOwner(final HumanId friend__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventAddOwner(friend__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public Return<PrivateEvent> uPrivateEventAddOwnerWithPrivateLocationCheck(final HumanId owner__, final long privateEventId__, final HumansFriend friend__, final long privateLocationId__) {
        Return<PrivateEvent> r;
        try {
            final boolean isAnybody = rPrivateLocationLocal_.doRPrivateLocationIsViewerOrOwner(friend__.getHumanId(), privateLocationId__);//Will happen within this transaction
            Loggers.DEBUG.debug(VIEWER_OWNERSHIP_STATUS + isAnybody);
            if (!isAnybody) {
                Loggers.DEBUG.debug(ADDING_USER_AS_VIEWER_TO_PRIVATE_LOCATION_OF_THIS_EVENT_AS_HE_ISN_T_ANYBODY_OF_IT);
                uPrivateLocationLocal_.doUPrivateLocationAddViewer(owner__.getObj(), privateLocationId__, friend__.getHumanId());
            }
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventAddOwner(owner__.getObj(), privateEventId__, friend__), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventRemoveOwner(final HumanId friend__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventRemoveOwner(friend__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventAddVisitor(final HumanId friend__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventAddViewer(friend__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventAddVisitorWithPrivateLocationCheck(final HumanId owner__, final long privateEventId__, final HumansFriend friend__, final long privateLocationId__) {
        Return<PrivateEvent> r;
        try {
            final boolean isAnybody = rPrivateLocationLocal_.doRPrivateLocationIsViewerOrOwner(friend__.getHumanId(), privateLocationId__);//Will happen within this transaction
            Loggers.DEBUG.debug(VIEWER_OWNERSHIP_STATUS + isAnybody);
            if (!isAnybody) {
                Loggers.DEBUG.debug(ADDING_USER_AS_VIEWER_TO_PRIVATE_LOCATION_OF_THIS_EVENT_AS_HE_ISN_T_ANYBODY_OF_IT);
                uPrivateLocationLocal_.doUPrivateLocationAddViewer(owner__.getObj(), privateLocationId__, friend__.getHumanId());
            }
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventAddViewer(owner__.getObj(), privateEventId__, friend__), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventRemoveVisitor(final HumanId friend__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventRemoveViewer(friend__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventAddInvite(final HumanId friend__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventAddInvite(friend__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventAddInviteWithPrivateLocationCheck(final HumanId owner__, final long privateEventId__, final HumansFriend friend__, final long privateLocationId__) {
        Return<PrivateEvent> r;
        try {
            final boolean isAnybody = rPrivateLocationLocal_.doRPrivateLocationIsViewerOrOwner(friend__.getHumanId(), privateLocationId__);//Will happen within this transaction
            Loggers.DEBUG.debug(VIEWER_OWNERSHIP_STATUS + isAnybody);
            if (!isAnybody) {
                Loggers.DEBUG.debug(ADDING_USER_AS_VIEWER_TO_PRIVATE_LOCATION_OF_THIS_EVENT_AS_HE_ISN_T_ANYBODY_OF_IT);
                uPrivateLocationLocal_.doUPrivateLocationAddViewer(owner__.getObj(), privateLocationId__, friend__.getHumanId());
            }
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventAddInvite(owner__.getObj(), privateEventId__, friend__), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventRemoveInvite(final HumanId friend__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventRemoveInvite(friend__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventAddReject(final HumanId friend__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventAddReject(friend__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventRemoveReject(final HumanId friend__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventRemoveReject(friend__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }
//
//    public Return<Wall> uPrivateEventAddToWall(HumanId operator__, long privateEventId__, String contentToBeAppended) {
//
//        Return<Wall> r;
//        try {
//            r = new ReturnImpl<Wall>(crudWallLocal_
//                    .doNTxUAppendToWall(dirtyRPrivateEventAsAny(operator__.getObj(), privateEventId__).returnValueBadly().getPrivateEventWall().getWallId(),
//                            contentToBeAppended), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
//        } catch (final AbstractEjbApplicationException t) {
//            r = new ReturnImpl<Wall>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
//        }
//        return r;
//
//
//    }

    @Override
    public Return<Wall> addEntryToWall(final HumanId whosWall__, final HumanId msgOwner__, final Obj wallReference__, final String contentToBeAppended) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doUAddEntry(dirtyRPrivateEventAsAny(whosWall__.getObj(), (Long) wallReference__.getObjectAsValid()).returnValueBadly().getPrivateEventWall().getWallId(),
                            msgOwner__.getObj(),
                            contentToBeAppended), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;


    }

    @Override
    public Return<Wall> muteWall(final HumanId operator__, final HumanId mutee, final Obj wallReference__) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doUAddMuteEntry(dirtyRPrivateEventAsAny(operator__.getObj(), (Long) wallReference__.getObjectAsValid()).returnValueBadly().getPrivateEventWall().getWallId(),
                            mutee.getObj()), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;


    }

    @Override
    public Return<Wall> unmuteWall(final HumanId operator__, final HumanId mutee, final Obj wallReference__) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doURemoveMuteEntry(dirtyRPrivateEventAsAny(operator__.getObj(), (Long) wallReference__.getObjectAsValid()).returnValueBadly().getPrivateEventWall().getWallId(),
                            mutee.getObj()), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;


    }

//    public Return<Wall> uPrivateEventClearWall(HumanId operator__, long privateEventId__) {
//
//        Return<Wall> r;
//        try {
//            r = new ReturnImpl<Wall>(crudWallLocal_
//                    .doUClearWall(dirtyRPrivateEventAsAny(operator__.getObj(), privateEventId__).returnValueBadly().getPrivateEventWall().getWallId()), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
//        } catch (final AbstractEjbApplicationException t) {
//            r = new ReturnImpl<Wall>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
//        }
//        return r;
//    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public Return<Wall> readWall(final HumanId whosWall__, final Obj requester__, final RefreshSpec refreshSpec__) {

        Return<Wall> r;
        try {
            final PrivateEvent privateEvent = dirtyRPrivateEventAsAny(whosWall__.getObj(), (Long) requester__.getObjectAsValid()).returnValueBadly();
            final Wall wall = crudWallLocal_.doRWall(privateEvent.getPrivateEventWall().getWallId(), refreshSpec__);

            if (wall.getWallMetadata() == null) {
                RecoveringFromAbsentMetadata:
                {
                    final String key = Wall.WallMetadataKey.PRIVATE_EVENT.toString();
                    final String value = "" + privateEvent.getPrivateEventId();

                    crudWallLocal_.doUpdateMetadata(wall.getWallId(), key, value);
                }
            }

            r = new ReturnImpl<Wall>(wall, READ_WALL_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, READ_WALL_FAILED, true);
        }
        return r;
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public Return<List<Msg>> readWallLastEntries(final HumanId humanId, final Obj<Long> wallId, final Integer numberOfEntriesToFetch, final RefreshSpec refreshSpec__) {
        Return<List<Msg>> r;
        r = new ReturnImpl<List<Msg>>(crudWallLocal_.doRHumansWallLastEntries(wallId.getObj(), numberOfEntriesToFetch), READ_WALL_SUCCESSFUL);
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<PrivateEvent> dirtyRPrivateEventAsAny(final String humanId, final long privateEventId) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(rPrivateEventLocal_.doRPrivateEventAsAny(humanId, privateEventId), VIEW_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, VIEW_PRIVATE_EVENT_FAILED, true);
        }
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public Return<List<PrivateEvent>> doDirtyRPrivateEventsOfHuman(final HumanId humanId) {
        Return<List<PrivateEvent>> r;
        try {
            r = new ReturnImpl<List<PrivateEvent>>(rPrivateEventLocal_.doRPrivateEventsOfHuman(humanId.getObj()), VIEW_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationRuntimeException t) {
            r = new ReturnImpl<List<PrivateEvent>>(t, VIEW_PRIVATE_EVENT_FAILED, true);
        }
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<PrivateEvent> dirtyRPrivateEventInfoAsAny(final String humanId, final long privateEventId) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(rPrivateEventLocal_.doRPrivateEventBasicAsAny(humanId, privateEventId), VIEW_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, VIEW_PRIVATE_EVENT_FAILED, true);
        }
        return r;

    }

    @TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<Boolean> dirtyRPrivateEventIsOwner(final HumanId humanId, final Long privateEventId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(rPrivateEventLocal_.doRPrivateEventIsOwner(humanId.getObjectAsValid(), privateEventId), CHECK_OWNERSHIP_OF_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Boolean>(t, CHECK_OWNERSHIP_OF_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<Boolean> dirtyRPrivateEventIsViewer(final HumanId humanId, final Long privateEventId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(rPrivateEventLocal_.doRPrivateEventIsViewer(humanId.getObjectAsValid(), privateEventId), CHECK_VIEWERSHIP_OF_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Boolean>(t, CHECK_VIEWERSHIP_OF_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<Boolean> dPrivateEvent(final HumanId operator, final long privateEventId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(dPrivateEventLocal_.doNTxDPrivateEvent(operator.getObjectAsValid(), privateEventId), DELETE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Boolean>(t, DELETE_PRIVATE_EVENT_FAILED, true);
        }
        return r;

    }

    @Override
    @Deprecated
    public Return<Album> rPrivateEventReadAlbum(final HumanId operator__, final long privateEventId__) {
        Return<Album> r;
        try {
            r = new ReturnImpl<Album>(crudAlbumLocal_.doRAlbumByPrivateEvent(operator__.getObjectAsValid(), privateEventId__, true), READ_ALBUM_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Album>(t, READ_ALBUM_FAILED, true);
        }
        return r;
    }

    @Override
    @Deprecated
    public Return<Album> rPrivateEventReadAlbum(final HumanId operator__, final long privateEventId__, final RefreshSpec refreshSpec) {
        Return<Album> r;
        try {
            r = new ReturnImpl<Album>(crudAlbumLocal_.doRAlbumByPrivateEvent(operator__.getObjectAsValid(), privateEventId__, refreshSpec), READ_ALBUM_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Album>(t, READ_ALBUM_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<Album> uPrivateEventAddEntryToAlbum(final HumanId operator__, final long privateEventId__, final RefObj<String> cdnFileName) {
        Return<Album> r;
        try {
            r = new ReturnImpl<Album>(crudAlbumLocal_.doUAlbumOfPrivateEventAddEntry(privateEventId__, operator__.getObjectAsValid(), cdnFileName.getObjectAsValid()), ADD_PHOTO_TO_ALBUM_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Album>(t, ADD_PHOTO_TO_ALBUM_FAILED, true);
        }
        return r;
    }

    @FIXME(issue = "Use human, or move to a different class")
    @Override
    public Return<List<PrivateEvent>> doRPrivateEventsByBounds(final HumanId operator__, final double latitudeSouth, final double latitudeNorth, final double longitudeWest, final double longitudeEast) {
        Return<List<PrivateEvent>> r;
        try {
            r = new ReturnImpl<List<PrivateEvent>>(rPrivateEventLocal_.doRPrivateEventsByBoundingBoxAsSystem(latitudeSouth, latitudeNorth, longitudeWest, longitudeEast), FETCH_BOUNDED_EVENTS_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<List<PrivateEvent>>(t, FETCH_BOUNDED_EVENTS_FAILED, true);
        }
        return r;
    }

    @FIXME(issue = "Use human, or move to a different class")
    @Override
    public Return<List<PrivateEvent>> doRPrivateEventsByBoundsAsSystem(final double latitudeSouth, final double latitudeNorth, final double longitudeWest, final double longitudeEast) {
        Return<List<PrivateEvent>> r;
        try {
            r = new ReturnImpl<List<PrivateEvent>>(rPrivateEventLocal_.doRPrivateEventsByBoundingBoxAsSystem(latitudeSouth, latitudeNorth, longitudeWest, longitudeEast), FETCH_BOUNDED_EVENTS_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<List<PrivateEvent>>(t, FETCH_BOUNDED_EVENTS_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<Album> uPrivateEventRemoveEntryFromAlbum(final HumanId operator__, final long privateEventId__) {
        throw ExceptionCache.METHOD_NOT_IMPLEMENTED;
    }
}