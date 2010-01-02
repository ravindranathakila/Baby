package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.logic.crud.unit.CPublicPhotoLocal;
import ai.ilikeplaces.logic.crud.unit.DPublicPhotoLocal;
import ai.ilikeplaces.logic.crud.unit.RPublicPhotoLocal;
import ai.ilikeplaces.logic.crud.unit.UPublicPhotoLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.ReturnImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@CONVENTION(convention = "DO ALL THE POSSIBLE NEEDFUL9SETTERS ETC) BEFORE GOING INTO THE TRANSACTION, VIA AN INTERMEDIATE METHOD. SAVES RESOURCES. " +
        "WHY NOT LET THE CALLER DO THIS? LETS DO THE HARD WORK. GIVE THE GUY A BREAK! BESIDES, WE CAN ENFORCE HIM TO GIVE US REQUIRED FIELDS. THIS ALSO " +
        "FACILITATES SETTING GRANULAR ROLE PERMISSIONS.")
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
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", HumanCRUDPublicPhoto.class, this.hashCode());
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

    @FIXME(issue = "When adding a location, there cannot be two equal location names such that super locations are equal too. Please update specific CRUD service. Also His As, My As.")
    @Override
    public boolean doHumanCPublicPhoto(final String humanId, final long locationId, final String fileName, final String publicPhotoName, final String publicPhotoDescription, final String publicPhotoURLPath, final int retries) {

        final PublicPhoto publicPhoto = new PublicPhoto();

        publicPhoto.setPublicPhotoName(publicPhotoName);
        publicPhoto.setPublicPhotoFilePath(fileName);
        publicPhoto.setPublicPhotoDescription(publicPhotoDescription);
        publicPhoto.setPublicPhotoURLPath(publicPhotoURLPath);

        return doHumanCPublicPhoto(humanId, locationId, publicPhoto);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<List<PublicPhoto>> doHumanRPublicPhoto(final String humanId) {
        Return<List<PublicPhoto>> r;
        try {
            r = new ReturnImpl<List<PublicPhoto>>(rPublicPhotoLocal_.doRAllPublicPhotos(humanId), "Find all photos by human Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<List<PublicPhoto>>(t, "Find all photos by human FAILED!", true);
        }
        return r;
    }

    /*BEGINING OF NON TRANSACTIONAL METHODS*/

    @Override
    @TODO(task = "verify if it is the right human before deleting")
    public boolean doHumanUPublicPhotoDescription(final String humanId, final long publicPhotoId, final String publicPhotoDescription) {
        return doHumanUPublicPhotoDescription(publicPhotoId, publicPhotoDescription);
    }

    @Override
    @TODO(task = "verify if it is the right human before deleting")
    public boolean doHumanDPublicPhoto(final String humanId, final long publicPhotoId) {
        return doHumanDPublicPhoto(publicPhotoId);
    }
    /*END OF NON TRANSACTIONAL METHODS*/
    final static Logger logger = LoggerFactory.getLogger(HumanCRUDPublicPhoto.class);
}
