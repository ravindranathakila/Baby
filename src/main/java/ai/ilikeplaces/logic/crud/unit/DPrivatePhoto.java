package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.PrivatePhoto;
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
public class DPrivatePhoto implements DPrivatePhotoLocal {

    @EJB
    private CrudServiceLocal<PrivatePhoto> privatePhotoCrudServiceLocal_;

    public DPrivatePhoto() {
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean doNTxDPrivatePhoto(final long privatePhotoId) {
        privatePhotoCrudServiceLocal_.delete(PrivatePhoto.class, privatePhotoId);
        return true;
    }

    final static Logger logger = LoggerFactory.getLogger(DPrivatePhoto.class);
}
