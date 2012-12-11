package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UPrivatePhotoLocal {

    public PrivatePhoto doNAAddToAlbum(final long privatePhotoId, final long albumId) throws DBDishonourCheckedException;
}
