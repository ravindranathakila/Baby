package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansPublicPhoto;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.scribble.License;
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
public class CPublicPhoto implements CPublicPhotoLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceHuman_;
    @EJB
    private CrudServiceLocal<Location> crudServiceLocation_;
    @EJB
    private CrudServiceLocal<PublicPhoto> CrudServicePublicPhoto_;

    public CPublicPhoto() {
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PublicPhoto doNTxCPublicPhotoLocal(final String humanId, final long locationId, final PublicPhoto publicPhoto) {
        if (publicPhoto.getLocation() != null || publicPhoto.getHumansPublicPhoto() != null) {
            logger.warn("HEY! DON'T SET THE "
                    + Location.class.getSimpleName() + " OR "
                    + HumansPublicPhoto.class.getSimpleName() + " PARAMETERS OF "
                    + PublicPhoto.class.getSimpleName() + " WHEN CALLING THIS METHOD!");
        }
        final HumansPublicPhoto humansPublicPhoto = crudServiceHuman_.find(Human.class, humanId).getHumansPublicPhoto();
        final Location location = crudServiceLocation_.find(Location.class, locationId);

        publicPhoto.setLocation(location);
        publicPhoto.setHumansPublicPhoto(humansPublicPhoto);

        final PublicPhoto managedPublicPhoto = CrudServicePublicPhoto_.create(publicPhoto);

        location.getPublicPhotos().add(managedPublicPhoto);
        humansPublicPhoto.getPublicPhotos().add(managedPublicPhoto);

        return publicPhoto;
    }

    final static Logger logger = LoggerFactory.getLogger(CPublicPhoto.class);
}
