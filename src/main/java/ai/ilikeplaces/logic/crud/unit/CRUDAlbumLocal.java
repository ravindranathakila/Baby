package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.exception.DBDishonourCheckedException;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CRUDAlbumLocal {
    public Album doUAlbumAddEntry(final long privateEventId, final String humanId, final String photoUrl) throws DBDishonourCheckedException;
}