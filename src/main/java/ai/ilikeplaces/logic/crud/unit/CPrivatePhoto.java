package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.*;
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
public class CPrivatePhoto implements CPrivatePhotoLocal {

    @EJB
    private CrudServiceLocal<PrivatePhoto> privatePhotoCrudServiceLocal_;
    @EJB
    private CrudServiceLocal<HumansPrivatePhoto> humansPrivatePhotoCrudServiceLocal_;

    public CPrivatePhoto() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", CPrivatePhoto.class, this.hashCode());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivatePhoto doNTxCPrivatePhotoLocal(final String humanId, final PrivatePhoto privatePhoto) {

        final HumansPrivatePhoto humansPrivatePhoto =
                humansPrivatePhotoCrudServiceLocal_.find(HumansPrivatePhoto.class, humanId);

        privatePhoto.setHumansPrivatePhoto(humansPrivatePhoto);

        final PrivatePhoto managedPrivatePhoto = privatePhotoCrudServiceLocal_.create(privatePhoto);

        humansPrivatePhoto.getPrivatePhotos().add(managedPrivatePhoto);

        return privatePhoto;
    }

    final static Logger logger = LoggerFactory.getLogger(CPrivatePhoto.class);
}