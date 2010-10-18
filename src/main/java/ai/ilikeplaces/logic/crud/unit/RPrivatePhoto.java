package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.HumansPrivatePhoto;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
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
public class RPrivatePhoto implements RPrivatePhotoLocal {

    @EJB
    private CrudServiceLocal<PrivatePhoto> privatePhotoCrudServiceLocal_;
    @EJB
    private CrudServiceLocal<Album> albumCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<HumansPrivatePhoto> humansPrivatePhotoCrudServiceLocal_;

    public RPrivatePhoto() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", RPrivatePhoto.class, this.hashCode());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<PrivatePhoto> doRAllPrivatePhotos(final String humanId) throws DBDishonourCheckedException {

        return humansPrivatePhotoCrudServiceLocal_.findBadly(HumansPrivatePhoto.class, humanId).getPrivatePhotos();
    }

    final static Logger logger = LoggerFactory.getLogger(RPrivatePhoto.class);
}