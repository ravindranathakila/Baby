package ai.ilikeplaces.logic.crud.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansPublicPhoto;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class DPublicPhoto implements DPublicPhotoLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceHuman_;
    @EJB
    private CrudServiceLocal<Location> crudServiceLocation_;
    @EJB
    private CrudServiceLocal<PublicPhoto> CrudServicPublicPhoto_;

    public DPublicPhoto() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", DPublicPhoto.class, this.hashCode());
    }

    @Override
    public boolean doDPublicPhotoLocal(final long publicPhotoId) {
        final PublicPhoto publicPhoto = CrudServicPublicPhoto_.find(PublicPhoto.class, publicPhotoId);

        logger.debug("REMOVING FROM HUMAN");
        if (!publicPhoto.getHumansPublicPhoto().getPublicPhotos().remove(publicPhoto)) {
            throw new IllegalStateException("SORRY, I FIND NO SUCH VALUE.");
        }
        
        logger.debug("REMOVING FROM LOCATION");
        if (!publicPhoto.getLocation().getPublicPhotos().remove(publicPhoto)) {
            throw new IllegalStateException("SORRY, I FIND NO SUCH VALUE.");
        }

        CrudServicPublicPhoto_.delete(PublicPhoto.class, publicPhoto.getPublicPhotoId());

        return true;
    }
    final static Logger logger = LoggerFactory.getLogger(DPublicPhoto.class);
}
