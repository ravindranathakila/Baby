package ai.ilikeplaces.logic.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansPublicPhoto;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.CascadeType;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@CONVENTION(convention = "DO ALL THE POSSIBLE NEEDFUL9SETTERS ETC) BEFORE GOING INTO THE TRANSACTION, VIA AN INTERMEDIATE METHOD. SAVES RESOURCES. " +
"WHY NOT LET THE CALLER DO THIS? LETS DO THE HARD WORK. GIVE THE GUY A BREAK! BESIDES, WE CAN ENFORCE HIM TO GIVE US REQUIRED FIELDS. THIS ALSO " +
"FACILITATES SETTING GRANULAR ROLE PERMISSIONS.")
final public class HumanCRUDPublicPhoto extends AbstractSLBCallbacks implements HumanCRUDPublicPhotoLocal {

    @EJB
    private RLocationLocal rLocationLocal_;
    @EJB
    private RHumanLocal rHumanLocal_;
    @EJB
    private CPublicPhotoLocal cPublicPhotoLocal_;
    @EJB
    private DPublicPhotoLocal dPublicPhotoLocal_;

    public HumanCRUDPublicPhoto() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", HumanCRUDPublicPhoto.class, this.hashCode());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @WARNING(warning = "transactional_1", warnings = {"These two Merge and Persist Each other. Do not update their fields after this block"})
    private boolean doHumanCPublicPhoto(final String humanId, final long locationId, PublicPhoto publicPhoto) throws javax.ejb.EJBTransactionRolledbackException {
        cPublicPhotoLocal_.doCPublicPhotoLocal(humanId, locationId, publicPhoto);
        return true;
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
    final static Logger logger = LoggerFactory.getLogger(HumanCRUDPublicPhoto.class);

    @Override
    public boolean doHumanRPublicPhoto() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean doHumanUPublicPhoto() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @TODO(task="VERIFY IF IT IS THE RIGHT HUMAN BEFORE DELETING")
    public boolean doHumanDPublicPhoto(final String humanId, final long publicPhotoId) {
        return dPublicPhotoLocal_.doDPublicPhotoLocal(publicPhotoId);
    }
}