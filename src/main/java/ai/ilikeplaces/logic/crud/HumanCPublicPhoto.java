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
final public class HumanCPublicPhoto extends AbstractSLBCallbacks implements HumanCPublicPhotoLocal {

    @EJB
    private RLocationLocal rLocationLocal_;
    @EJB
    private RHumanLocal rHumanLocal_;
    @EJB
    private CPublicPhotoLocal cPublicPhotoLocal_;

    public HumanCPublicPhoto() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", HumanCPublicPhoto.class, this.hashCode());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @WARNING(warning = "transactional_1", warnings = {"These two Merge and Persist Each other. Do not update their fields after this block"})
    private boolean doHumanCPublicPhoto(final String humanId, final long locationId, PublicPhoto publicPhoto) throws javax.ejb.EJBTransactionRolledbackException {
            cPublicPhotoLocal_.doCPublicPhotoLocal(humanId, locationId, publicPhoto);
//        /*By now, photo is persisted. So only Merge, Refresh should work*/
//        updateHumansPublicPhoto:
//        {
//            /*Set location for merging*/
//            @TRANSACTION(TransactionAttributeType.REQUIRED)
//            final Location location_ = rLocationLocal_.doRLocation(locationId);
//            /* These two Merge and Persist Each other*/
//            transactional_1:
//            {
//                /*@ManyToOne(cascade={CascadeType.PERSIST})*/
//                /*NO TX*/
//                publicPhoto.setLocation(location_);
//                /*NO TX*/
//                @TRANSACTION(TransactionAttributeType.MANDATORY)
//                final HumansPublicPhoto humansPublicPhoto = rHumanLocal_.doRHuman(humanId).getHumansPublicPhoto();
//
//                /*Set humansPublicPhoto for merging*/
//                /*NO TX*/
//                publicPhoto.setHumansPublicPhoto(humansPublicPhoto);
//
//                humansPublicPhoto.getPublicPhotos().add(publicPhoto);
//                /*@OneToMany(cascade = CascadeType.MERGE)*/
//                location_.getPublicPhotos().add(publicPhoto);
//            }
//
//        }

        return true;
    }

    private boolean doHumanCPublicPhotoRetry(final String humanId, final long locationId, final PublicPhoto publicPhoto) {
        logger.debug("Processing");
        boolean transactionSuccessful = false;
        /* retryLimit X (retryLimit + 1) / 2 = total seconds*/
        retryLoop:
        {
            for (int retries = 1, retryLimit = 4; retries <= retryLimit; retries++) {
                try {
                    transactionSuccessful = doHumanCPublicPhoto(humanId, locationId, publicPhoto);
                    logger.debug("HELLO, DONE PERSISTING!");
                    if (retries > 1) {
                        logger.info("HELLO, I MANAGED TO PERSIST THE DATA ONLY AFTER {} RETRIES.", retries);
                    }
                    break retryLoop;
                } catch (Exception e_) {
                    logger.debug("SORRY! I AM UNABLE TO PERSIST FILE UPLOAD DATA.", e_);
                    if (retries == retryLimit) {/*ok this is the last retrey. Failed. lets report to the client*/
                        transactionSuccessful = false;
                    } else {
                        logger.info("HELLO, I AM RETRYING TO PERSIST THE DATA AFTER {} SECONDS THREAD SLEEP. I AM GOING TO SLEEP NOW.", retries);
                        try {
                            Thread.sleep(1000 * retries);
                        } catch (InterruptedException ex) {
                            logger.error(null, ex);
                        }
                    }
                }
            }
        }
        return transactionSuccessful;
    }

    @FIXME(issue = "When adding a location, there cannot be two equal location names such that super locations are equal too. Please update specific CRUD service")
    @Override
    public boolean doHumanCPublicPhoto(final String humanId, final long locationId, final String fileName, final String publicPhotoName, final String publicPhotoDescription, final String publicPhotoURLPath, final int retries) {

        final PublicPhoto publicPhoto = new PublicPhoto();

        publicPhoto.setPublicPhotoName(publicPhotoName);
        publicPhoto.setPublicPhotoFilePath(fileName);
        publicPhoto.setPublicPhotoDescription(publicPhotoDescription);
        publicPhoto.setPublicPhotoURLPath(publicPhotoURLPath);

        return doHumanCPublicPhotoRetry(humanId, locationId, publicPhoto);
    }
    final static Logger logger = LoggerFactory.getLogger(HumanCPublicPhoto.class);
}
