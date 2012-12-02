package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RPrivatePhotoLocal {

    public List<PrivatePhoto> doDirtyRAllPrivatePhotos(final String humanId) throws DBDishonourCheckedException;

    public PrivatePhoto doRPrivatePhoto(final String humanId, final Long privatePhotoId, RefreshSpec refreshSpec) throws DBFetchDataException;
}
