package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.CONVENTION;
import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.logic.crud.unit.CPrivatePhotoLocal;
import ai.ilikeplaces.logic.crud.unit.UPrivatePhotoLocal;
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
@CONVENTION(convention = "do all the possible needful9setters etc) before going into the transaction, via an intermediate method. saves resources. " +
        "why not let the caller do this? lets do the hard work. give the guy a break! besides, we can enforce him to give us required fields. this also " +
        "facilitates setting granular role permissions.")
@Interceptors({DBOffilne.class, ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
final public class HumanCRUDPrivatePhoto extends AbstractSLBCallbacks implements HumanCRUDPrivatePhotoLocal {

    @EJB
    private CPrivatePhotoLocal cPrivatePhotoLocal_;
    @EJB
    private UPrivatePhotoLocal uPrivatePhotoLocal_;

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
    public Return<PrivatePhoto> addPrivatePhotoAlbum(final long publicPhotoId, final long albumId){
        Return<PrivatePhoto> r;
        r = new ReturnImpl<PrivatePhoto>(null,"Add photo to album Successful!");

        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<List<PrivatePhoto>> rPrivatePhoto(final String humanId) {
        Return<List<PrivatePhoto>> r;
        throw ExceptionCache.UNSUPPORTED_OPERATION_EXCEPTION;
        //r = new ReturnImpl<List<PrivatePhoto>>(rPrivatePhotoLocal_.doRAllPrivatePhotos(humanId), "Find all photos by human Successful!");
    }


    /*END OF NON TRANSACTIONAL METHODS*/
    final static Logger logger = LoggerFactory.getLogger(HumanCRUDPrivatePhoto.class);
}