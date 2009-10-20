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
public class UPublicPhoto implements UPublicPhotoLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceHuman_;
    @EJB
    private CrudServiceLocal<Location> crudServiceLocation_;
    @EJB
    private CrudServiceLocal<PublicPhoto> CrudServicPublicPhoto_;

    public UPublicPhoto() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", UPublicPhoto.class, this.hashCode());
    }

    @Override
    public boolean doUPublicPhotoDescriptionLocal(final long publicPhotoId, final String publicPhotoDescription) {
        CrudServicPublicPhoto_.find(PublicPhoto.class, publicPhotoId).setPublicPhotoDescription(publicPhotoDescription);
        return true;
    }
    final static Logger logger = LoggerFactory.getLogger(UPublicPhoto.class);
}
