package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.Human;
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

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class UPublicPhoto implements UPublicPhotoLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceHuman_;
    @EJB
    private CrudServiceLocal<Location> crudServiceLocation_;
    @EJB
    private CrudServiceLocal<PublicPhoto> crudServicePublicPhoto_;

    public UPublicPhoto() {
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean doNTxUPublicPhotoDescriptionLocal(final long publicPhotoId, final String publicPhotoDescription) {
        crudServicePublicPhoto_.find(PublicPhoto.class, publicPhotoId).setPublicPhotoDescription(publicPhotoDescription);
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean doUPublicPhotoDescriptionLocal(final long publicPhotoId, final String publicPhotoDescription) {
        crudServicePublicPhoto_.find(PublicPhoto.class, publicPhotoId).setPublicPhotoDescription(publicPhotoDescription);
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean doDirtyUPublicPhotoDescriptionLocal(final long publicPhotoId, final String publicPhotoDescription) {
        crudServicePublicPhoto_.find(PublicPhoto.class, publicPhotoId).setPublicPhotoDescription(publicPhotoDescription);
        return true;
    }

    final static Logger logger = LoggerFactory.getLogger(UPublicPhoto.class);
}
