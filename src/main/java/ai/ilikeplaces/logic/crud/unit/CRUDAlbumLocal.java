package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CRUDAlbumLocal {
    public Album doUAlbumAddEntry(final long privateEventId, final String humanId, final String photoUrl) throws DBDishonourCheckedException, DBFetchDataException;

    /**
     * @param humanId
     * @param privateEventId by which to fetch the album
     * @param eager
     * @return
     * @throws DBDishonourCheckedException
     */
    public Album doRAlbumByPrivateEvent(final String humanId, final long privateEventId, final boolean eager) throws DBDishonourCheckedException, DBFetchDataException;


}