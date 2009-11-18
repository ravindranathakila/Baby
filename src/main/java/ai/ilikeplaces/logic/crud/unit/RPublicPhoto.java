package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansPublicPhoto;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class RPublicPhoto implements RPublicPhotoLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceHuman_;
    @EJB
    private CrudServiceLocal<Location> crudServiceLocation_;
    @EJB
    private CrudServiceLocal<PublicPhoto> CrudServicPublicPhoto_;

    public RPublicPhoto() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", RPublicPhoto.class, this.hashCode());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PublicPhoto doRPublicPhotoLocal(final String humanId, final long locationId, final PublicPhoto publicPhoto) {
        return doCommonRPublicPhotoLocal(humanId, locationId, publicPhoto);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    private PublicPhoto doCommonRPublicPhotoLocal(final String humanId, final long locationId, final PublicPhoto publicPhoto) {
        if (publicPhoto.getLocation() != null || publicPhoto.getHumansPublicPhoto() != null) {
            logger.warn("HEY! DON'T SET THE Location OR HumansPublicPhoto PARAMETERS OF PublicPhoto WHEN CALLING THIS METHOD!");
        }
        final HumansPublicPhoto humansPublicPhto = crudServiceHuman_.find(Human.class, humanId).getHumansPublicPhoto();
        final Location location = crudServiceLocation_.find(Location.class, locationId);

        publicPhoto.setLocation(location);
        publicPhoto.setHumansPublicPhoto(humansPublicPhto);

        final PublicPhoto managedPublicPhoto = CrudServicPublicPhoto_.create(publicPhoto);

        location.getPublicPhotos().add(managedPublicPhoto);
        humansPublicPhto.getPublicPhotos().add(managedPublicPhoto);

        return publicPhoto;
    }

    final static Logger logger = LoggerFactory.getLogger(RPublicPhoto.class);
}
