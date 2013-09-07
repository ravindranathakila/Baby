package ai.baby.logic.crud.unit;

import ai.baby.util.exception.DBDishonourCheckedException;
import ai.baby.util.exception.DBFetchDataException;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.scribble.License;

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
