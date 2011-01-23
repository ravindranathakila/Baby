package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.exception.DBDishonourCheckedException;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RPrivatePhotoLocal {

    public List<PrivatePhoto> doRAllPrivatePhotos(final String humanId) throws DBDishonourCheckedException;

    public PrivatePhoto doDirtyRPrivatePhoto(final String humanId, final Long privatePhotoId) throws DBDishonourCheckedException;
}