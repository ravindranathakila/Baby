package ai.ilikeplaces.logic.crud;

import ai.doc.*;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.logic.crud.unit.CPublicPhotoLocal;
import ai.ilikeplaces.logic.crud.unit.DPublicPhotoLocal;
import ai.ilikeplaces.logic.crud.unit.RPublicPhotoLocal;
import ai.ilikeplaces.logic.crud.unit.UPublicPhotoLocal;
import ai.ilikeplaces.util.*;
import ai.reaver.Return;
import ai.reaver.ReturnImpl;
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
@_convention(convention = "do all the possible needful9setters etc) before going into the transaction, via an intermediate method. saves resources. " +
        "why not let the caller do this? lets do the hard work. give the guy a break! besides, we can enforce him to give us required fields. this also " +
        "facilitates setting granular role permissions.")
@Interceptors({EntityManagerInjector.class, DBOffline.class, ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
final public class HumanCRUDPublicPhoto extends AbstractSLBCallbacks implements HumanCRUDPublicPhotoLocal {

    @EJB
    private CPublicPhotoLocal cPublicPhotoLocal_;
    @EJB
    private UPublicPhotoLocal uPublicPhotoLocal_;
    @EJB
    private DPublicPhotoLocal dPublicPhotoLocal_;
    @EJB
    private RPublicPhotoLocal rPublicPhotoLocal_;

    public HumanCRUDPublicPhoto() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @WARNING(warning = "transactional", warnings = {"These two Merge and Persist Each other. Do not update their fields after this block"})
    private boolean doHumanCPublicPhoto(final String humanId, final long locationId, PublicPhoto publicPhoto) throws javax.ejb.EJBTransactionRolledbackException {
        cPublicPhotoLocal_.doNTxCPublicPhotoLocal(humanId, locationId, publicPhoto);
        return true;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private boolean doHumanDPublicPhoto(final long publicPhotoId) {
        return dPublicPhotoLocal_.doNTxDPublicPhotoLocal(publicPhotoId);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean doHumanUPublicPhotoDescription(final long publicPhotoId, final String publicPhotoDescription) {
        return uPublicPhotoLocal_.doUPublicPhotoDescriptionLocal(publicPhotoId, publicPhotoDescription);
    }

    @_fix(issue = "When adding a location, there cannot be two equal location names such that super locations are equal too. Please update specific CRUD service. Also His As, My As.")
    @Override
    public Return<PublicPhoto> cPublicPhoto(final String humanId, final long locationId, final String fileName, final String publicPhotoName, final String publicPhotoDescription, final String publicPhotoURLPath, final int retries) {
        Return<PublicPhoto> r;
        final PublicPhoto publicPhoto = new PublicPhoto();

        publicPhoto.setPublicPhotoName(publicPhotoName);
        publicPhoto.setPublicPhotoFilePath(fileName);
        publicPhoto.setPublicPhotoDescription(publicPhotoDescription);
        publicPhoto.setPublicPhotoURLPath(publicPhotoURLPath);
        r = new ReturnImpl<PublicPhoto>(cPublicPhotoLocal_.doNTxCPublicPhotoLocal(humanId, locationId, publicPhoto), "Create photo by human with location Successful!");
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<List<PublicPhoto>> rPublicPhoto(final String humanId) {
        Return<List<PublicPhoto>> r;
        r = new ReturnImpl<List<PublicPhoto>>(rPublicPhotoLocal_.doRAllPublicPhotos(humanId), "Find all photos by human Successful!");
        return r;
    }

    /*BEGINING OF NON TRANSACTIONAL METHODS*/

    @Override
    @_todo(task = "verify if it is the right human before deleting")
    public boolean uPublicPhotoDescription(final String humanId, final long publicPhotoId, final String publicPhotoDescription) {
        return doHumanUPublicPhotoDescription(publicPhotoId, publicPhotoDescription);
    }

    @Override
    @_todo(task = "verify if it is the right human before deleting")
    public boolean dPublicPhoto(final String humanId, final long publicPhotoId) {
        return doHumanDPublicPhoto(publicPhotoId);
    }

    /*END OF NON TRANSACTIONAL METHODS*/
    final static Logger logger = LoggerFactory.getLogger(HumanCRUDPublicPhoto.class);
}
