package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
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
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<PrivatePhoto> doDirtyRAllPrivatePhotos(final String humanId) throws DBDishonourCheckedException {
        return humansPrivatePhotoCrudServiceLocal_.findBadly(HumansPrivatePhoto.class, humanId).getPrivatePhotos();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PrivatePhoto doRPrivatePhoto(final String humanId, final Long privatePhotoId, final RefreshSpec refreshSpec) throws DBFetchDataException {
        try {
            return privatePhotoCrudServiceLocal_.findBadly(PrivatePhoto.class, privatePhotoId).refresh(refreshSpec);
        } catch (final RefreshException e) {
            throw new DBFetchDataException(e);
        }
    }

    final static Logger logger = LoggerFactory.getLogger(RPrivatePhoto.class);
}
