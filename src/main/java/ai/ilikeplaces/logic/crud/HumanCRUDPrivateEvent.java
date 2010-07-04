package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.logic.crud.unit.*;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 11:58:04 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
public class HumanCRUDPrivateEvent extends AbstractSLBCallbacks implements HumanCRUDPrivateEventLocal {

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


    @Override
    public Return<PrivateEvent> cPrivateEvent(final String humanId, final long privateLocationId, final String privateEventName, final String privateEventInfo, final String startDate, final String endDate) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(cPrivateEventLocal_.doNTxCPrivateEvent(humanId, privateLocationId, privateEventName, privateEventInfo, startDate, endDate), "Save private event Successful!");
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, "Save private event FAILED!", true);
        }
        return r;

    }


    @Override
    public Return<PrivateEvent> uPrivateEventAddOwner(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventAddOwner(humanId__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventRemoveOwner(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventRemoveOwner(humanId__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventAddVisitor(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventAddViewer(humanId__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventRemoveVisitor(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventRemoveViewer(humanId__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventAddInvite(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventAddInvite(humanId__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventRemoveInvite(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventRemoveInvite(humanId__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventAddReject(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventAddReject(humanId__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventRemoveReject(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal_.doUPrivateEventRemoveReject(humanId__.getObj(), privateEventId__, owner), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateEvent>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<Wall> uPrivateEventAddToWall(HumanId operator__, long privateEventId__, String contentToBeAppended) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doNTxUAppendToWall(dirtyRPrivateEvent(operator__.getObj(), privateEventId__).returnValueBadly().getPrivateEventWall().getWallId(),
                    contentToBeAppended), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;


    }

    @Override
    public Return<Wall> uPrivateEventAddEntryToWall(final HumanId operator__, final HumanId msgOwner__, final long privateEventId__, final String contentToBeAppended) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doNTxUAddEntry(dirtyRPrivateEvent(operator__.getObj(), privateEventId__).returnValueBadly().getPrivateEventWall().getWallId(),
                    msgOwner__.getObj(),
                    contentToBeAppended), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;


    }

    @Override
    public Return<Wall> uPrivateEventClearWall(HumanId operator__, long privateEventId__) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doNTxUClearWall(dirtyRPrivateEvent(operator__.getObj(), privateEventId__).returnValueBadly().getPrivateEventWall().getWallId()), UPDATE_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, UPDATE_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<Wall> rPrivateEventReadWall(HumanId operator__, long privateEventId__) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doDirtyRWall(dirtyRPrivateEvent(operator__.getObj(), privateEventId__).returnValueBadly().getPrivateEventWall().getWallId()), "Read wall Successful!");
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, READ_WALL_FAILED, true);
        }
        return r;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<PrivateEvent> dirtyRPrivateEvent(final String humanId, final long privateEventId) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(rPrivateEventLocal_.doDirtyRPrivateEvent(humanId, privateEventId), VIEW_PRIVATE_EVENT_SUCCESSFUL);
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
            r = new ReturnImpl<Boolean>(rPrivateEventLocal_.doDirtyRPrivateEventIsOwner(humanId.getObjectAsValid(), privateEventId), CHECK_OWNERSHIP_OF_PRIVATE_EVENT_SUCCESSFUL);
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
            r = new ReturnImpl<Boolean>(rPrivateEventLocal_.doDirtyRPrivateEventIsViewer(humanId.getObjectAsValid(), privateEventId), CHECK_VIEWERSHIP_OF_PRIVATE_EVENT_SUCCESSFUL);
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
    public Return<Album> rPrivateEventReadAlbum(final HumanId operator__, final long privateEventId__) {
        throw ExceptionCache.METHOD_NOT_IMPLEMENTED;
    }

    @Override
    public Return<Album> uPrivateEventAddEntryToAlbum(final HumanId operator__, final long privateEventId__, final RefObj<String> cdnFileName) {
        Return<Album> r;
        try {
            r = new ReturnImpl<Album>(crudAlbumLocal_.doUAlbumAddEntry(privateEventId__, operator__.getObjectAsValid(), cdnFileName.getObjectAsValid()), ADD_PHOTO_TO_ALBUM_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Album>(t, ADD_PHOTO_TO_ALBUM_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<Album> uPrivateEventRemoveEntryFromAlbum(final HumanId operator__, final long privateEventId__) {
        throw ExceptionCache.METHOD_NOT_IMPLEMENTED;
    }
}