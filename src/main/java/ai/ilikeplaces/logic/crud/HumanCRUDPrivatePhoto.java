package ai.ilikeplaces.logic.crud;

import ai.doc.License;
import ai.doc.WARNING;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.exception.NoPrivilegesException;
import ai.ilikeplaces.logic.crud.unit.*;
import ai.ilikeplaces.util.*;
import ai.util.HumanId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({EntityManagerInjector.class, DBOffline.class, ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
final public class HumanCRUDPrivatePhoto extends AbstractSLBCallbacks implements HumanCRUDPrivatePhotoLocal {

    private static final String WRITE_WALL_SUCCESSFUL = "Write Wall Successful!";
    private static final String WRITE_WALL_FAILED = "Write Wall FAILED!";
    private static final String MUTE_WALL_SUCCESSFUL = "Mute Wall Successful!";
    private static final String MUTE_WALL_FAILED = "Mute Wall FAILED!";
    private static final String UNMUTE_WALL_SUCCESSFUL = "Unmute Wall Successful!";
    private static final String UNMUTE_WALL_FAILED = "Unmute Wall FAILED!";
    private static final String READ_WALL_SUCCESSFUL = "Read Wall Successful!";
    private static final String READ_WALL_FAILED = "Read Wall FAILED!";
    private static final String EMPTY = "";
    @EJB
    private CPrivatePhotoLocal cPrivatePhotoLocal_;

    @EJB
    private UPrivatePhotoLocal uPrivatePhotoLocal_;

    @EJB
    private RPrivatePhotoLocal rPrivatePhotoLocal_;

    @EJB
    private DPrivatePhotoLocal dPrivatePhotoLocal_;

    @EJB
    private CRUDWallLocal crudWallLocal_;

    protected static final String FIND_ALL_PHOTOS_BY_HUMAN_FAILED = "Find all photos by human FAILED!";
    protected static final String FIND_ALL_PHOTOS_BY_HUMAN_SUCCESSFUL = "Find all photos by human Successful!";
    private static final String DELETE_PRIVATE_PHOTO_SUCCESSFUL = "Delete PrivatePhoto Successful!";
    private static final String DELETE_PRIVATE_PHOTO_FAILED = "Delete PrivatePhoto FAILED!";
    private static final String DELETE_PHOTO_WITH_ID = "delete photo with id:";

    private static final RefreshSpec REFRESH_SPEC = new RefreshSpec("privatePhotoWall");

    public HumanCRUDPrivatePhoto() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @WARNING(warning = "transactional", warnings = {"These two Merge and Persist Each other. Do not update their fields after this block"})
    private boolean doHumanCPrivatePhoto(final String humanId, PrivatePhoto privatePhoto) throws javax.ejb.EJBTransactionRolledbackException {
        cPrivatePhotoLocal_.doCPrivatePhotoLocal(humanId, privatePhoto);
        return true;
    }


    @Override
    public Return<PrivatePhoto> cPrivatePhoto(final String humanId, final String fileName, final String privatePhotoName, final String privatePhotoDescription, final String privatePhotoURLPath) {
        Return<PrivatePhoto> r;
        r = new ReturnImpl<PrivatePhoto>(cPrivatePhotoLocal_.doCPrivatePhotoLocal(humanId,
                privatePhotoName,
                fileName,
                privatePhotoDescription,
                privatePhotoURLPath
        ), "Create PrivatePhoto by human Successful!");
        return r;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<List<PrivatePhoto>> rPrivatePhotos(final String humanId) {
        Return<List<PrivatePhoto>> r;
        try {
            return new ReturnImpl<List<PrivatePhoto>>(rPrivatePhotoLocal_.doDirtyRAllPrivatePhotos(humanId), FIND_ALL_PHOTOS_BY_HUMAN_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<List<PrivatePhoto>>(t, FIND_ALL_PHOTOS_BY_HUMAN_FAILED, true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<PrivatePhoto> rPrivatePhoto(final HumanId humanId, final Obj<Long> privatePhotoId, final RefreshSpec refreshSpec) {
        Return<PrivatePhoto> r;
        try {
            return new ReturnImpl<PrivatePhoto>(rPrivatePhotoLocal_.doRPrivatePhoto(humanId.getObjectAsValid(), privatePhotoId.getObjectAsValid(), refreshSpec), FIND_ALL_PHOTOS_BY_HUMAN_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivatePhoto>(t, FIND_ALL_PHOTOS_BY_HUMAN_FAILED, true);
        }
        return r;
    }

    /**
     * Proper demonstration of using unit classes and role/authorization based classes.
     * <p/>
     * Check roles/authorizations here.
     * <p/>
     * Call units based on the roles/authorizaions.
     *
     * @param humanId
     * @param privatePhotoId
     * @return
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Boolean> dPrivatePhoto(final HumanId humanId, final long privatePhotoId) {
        final Return<Boolean> r;
        boolean isHumansPhoto = false;
        for (final PrivatePhoto privatePhoto : rPrivatePhotos(humanId.getHumanId()).returnValue()) {
            if (privatePhoto.getPrivatePhotoId() == privatePhotoId) {
                isHumansPhoto = true;
                break;
            }
        }

        if (!isHumansPhoto) {
            r = new ReturnImpl<Boolean>(new NoPrivilegesException(humanId.getHumanId(), DELETE_PHOTO_WITH_ID + privatePhotoId), DELETE_PRIVATE_PHOTO_FAILED, true);
        } else {
            r = new ReturnImpl<Boolean>(dPrivatePhotoLocal_.doNTxDPrivatePhoto(privatePhotoId), DELETE_PRIVATE_PHOTO_SUCCESSFUL);
        }

        return r;
    }


    @Override
    public Return<Wall> addEntryToWall(final HumanId whosWall__, final HumanId msgOwner__, final Obj wallReference__, final String contentToBeAppended) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doUAddEntry(rPrivatePhoto(whosWall__, wallReference__, REFRESH_SPEC).returnValueBadly().getPrivatePhotoWall().getWallId(),
                            msgOwner__.getObj(),
                            contentToBeAppended), WRITE_WALL_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Wall>(t, WRITE_WALL_FAILED, true);
        }
        return r;


    }

    @Override
    public Return<Wall> muteWall(final HumanId operator__, final HumanId mutee, final Obj wallReference__) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doUAddMuteEntry(rPrivatePhoto(operator__, wallReference__, REFRESH_SPEC).returnValueBadly().getPrivatePhotoWall().getWallId(),
                            mutee.getObj()), MUTE_WALL_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Wall>(t, MUTE_WALL_FAILED, true);
        }
        return r;


    }

    @Override
    public Return<Wall> unmuteWall(final HumanId operator__, final HumanId mutee, final Obj wallReference__) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doURemoveMuteEntry(rPrivatePhoto(operator__, wallReference__, REFRESH_SPEC).returnValueBadly().getPrivatePhotoWall().getWallId(),
                            mutee.getObj()), UNMUTE_WALL_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Wall>(t, UNMUTE_WALL_FAILED, true);
        }
        return r;


    }


    @Override
    public Return<Wall> readWall(final HumanId whosWall__, final Obj requester__, RefreshSpec refreshSpec__) {

        Return<Wall> r;
        try {
            final PrivatePhoto privatePhoto = rPrivatePhoto(whosWall__, requester__, REFRESH_SPEC).returnValueBadly();
            final Wall privatePhotoWall = privatePhoto.getPrivatePhotoWall();

            if (privatePhotoWall.getWallMetadata() == null) {
                RecoveringFromAbsentMetadata:
                {
                    final String key = Wall.WallMetadataKey.PRIVATE_PHOTO.toString();
                    final String value = EMPTY + privatePhoto.getPrivatePhotoId();

                    crudWallLocal_.doUpdateMetadata(privatePhotoWall.getWallId(), key, value);
                }
            }

            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doRWall(privatePhotoWall.getWallId(), refreshSpec__), READ_WALL_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Wall>(t, READ_WALL_FAILED, true);
        }
        return r;
    }


    /*END OF NON TRANSACTIONAL METHODS*/
    final static Logger logger = LoggerFactory.getLogger(HumanCRUDPrivatePhoto.class);
}
