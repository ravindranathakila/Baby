package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.CONVENTION;
import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.exception.NoPrivilegesException;
import ai.ilikeplaces.logic.crud.unit.CPrivatePhotoLocal;
import ai.ilikeplaces.logic.crud.unit.DPrivatePhotoLocal;
import ai.ilikeplaces.logic.crud.unit.RPrivatePhotoLocal;
import ai.ilikeplaces.logic.crud.unit.UPrivatePhotoLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.*;
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
@CONVENTION(convention = "do all the possible needful setters etc) before going into the transaction, via an intermediate method. saves resources. " +
        "why not let the caller do this? lets do the hard work. give the guy a break! besides, we can enforce him to give us required fields. this also " +
        "facilitates setting granular role permissions.")
@Interceptors({DBOffilne.class, ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
final public class HumanCRUDPrivatePhoto extends AbstractSLBCallbacks implements HumanCRUDPrivatePhotoLocal {

    @EJB
    private CPrivatePhotoLocal cPrivatePhotoLocal_;

    @EJB
    private UPrivatePhotoLocal uPrivatePhotoLocal_;

    @EJB
    private RPrivatePhotoLocal rPrivatePhotoLocal_;

    @EJB
    private DPrivatePhotoLocal dPrivatePhotoLocal_;

    protected static final String FIND_ALL_PHOTOS_BY_HUMAN_FAILED = "Find all photos by human FAILED!";
    protected static final String FIND_ALL_PHOTOS_BY_HUMAN_SUCCESSFUL = "Find all photos by human Successful!";
    private static final String DELETE_PRIVATE_PHOTO_SUCCESSFUL = "Delete PrivatePhoto Successful!";
    private static final String DELETE_PRIVATE_PHOTO_FAILED = "Delete PrivatePhoto FAILED!";
    private static final String DELETE_PHOTO_WITH_ID = "delete photo with id:";

    public HumanCRUDPrivatePhoto() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", HumanCRUDPrivatePhoto.class, this.hashCode());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @WARNING(warning = "transactional", warnings = {"These two Merge and Persist Each other. Do not update their fields after this block"})
    private boolean doHumanCPrivatePhoto(final String humanId, PrivatePhoto privatePhoto) throws javax.ejb.EJBTransactionRolledbackException {
        cPrivatePhotoLocal_.doNTxCPrivatePhotoLocal(humanId, privatePhoto);
        return true;
    }


    @FIXME(issue = "When adding a location, there cannot be two equal location names such that super locations are equal too. Please update specific CRUD service. Also His As, My As.")
    @Override
    public Return<PrivatePhoto> cPrivatePhoto(final String humanId, final String fileName, final String privatePhotoName, final String privatePhotoDescription, final String privatePhotoURLPath) {
        Return<PrivatePhoto> r;

        r = new ReturnImpl<PrivatePhoto>(cPrivatePhotoLocal_.doNTxCPrivatePhotoLocal(humanId,
                                                                                     new PrivatePhoto().
                                                                                             setPrivatePhotoNameR(privatePhotoName)
                                                                                             .setPrivatePhotoFilePathR(fileName)
                                                                                             .setPrivatePhotoDescriptionR(privatePhotoDescription)
                                                                                             .setPrivatePhotoURLPathR(privatePhotoURLPath)), "Create PrivatePhoto by human Successful!");
        return r;
    }

    @FIXME(issue = "When adding a location, there cannot be two equal location names such that super locations are equal too. Please update specific CRUD service. Also His As, My As.")
    @Override
    public Return<PrivatePhoto> addPrivatePhotoAlbum(final long publicPhotoId, final long albumId) {
//        Return<PrivatePhoto> r;
//        r = new ReturnImpl<PrivatePhoto>(null, "Add photo to album Successful!");
//
//        return r;
        throw ExceptionCache.METHOD_NOT_IMPLEMENTED;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<List<PrivatePhoto>> rPrivatePhoto(final String humanId) {
        Return<List<PrivatePhoto>> r;
        try {
            return new ReturnImpl<List<PrivatePhoto>>(rPrivatePhotoLocal_.doRAllPrivatePhotos(humanId), FIND_ALL_PHOTOS_BY_HUMAN_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<List<PrivatePhoto>>(t, FIND_ALL_PHOTOS_BY_HUMAN_FAILED, true);
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
        for (final PrivatePhoto privatePhoto : rPrivatePhoto(humanId.getHumanId()).returnValue()) {
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


    /*END OF NON TRANSACTIONAL METHODS*/
    final static Logger logger = LoggerFactory.getLogger(HumanCRUDPrivatePhoto.class);
}