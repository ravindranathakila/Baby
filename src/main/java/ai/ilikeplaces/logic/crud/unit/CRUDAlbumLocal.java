package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.ilikeplaces.entities.etc.RefreshException;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.util.exception.DBDishonourCheckedException;
import ai.ilikeplaces.util.exception.DBFetchDataException;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CRUDAlbumLocal {

    /**
     * @param privateEventId
     * @param humanId
     * @param photoUrl
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public Album doUAlbumOfPrivateEventAddEntry(final long privateEventId, final String humanId, final String photoUrl) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;

    /**
     * @param humanId
     * @param privateEventId by which to fetch the album
     * @param eager
     * @return
     * @throws DBDishonourCheckedException
     */
    @Deprecated
    public Album doRAlbumByPrivateEvent(final String humanId, final long privateEventId, final boolean eager) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;


    /**
     * @param humanId
     * @param privateEventId by which to fetch the album
     * @param refreshSpec
     * @return
     * @throws DBDishonourCheckedException
     */
    public Album doRAlbumByPrivateEvent(final String humanId, final long privateEventId, final RefreshSpec refreshSpec) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;


    /**
     * @param tribeId
     * @param humanId
     * @param photoUrl
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public Album doUAlbumOfTribeAddEntry(final long tribeId, final String humanId, final String photoUrl) throws DBDishonourCheckedException, DBFetchDataException, RefreshException;


    /**
     * @param humanId
     * @param tribeId     by which to fetch the album
     * @param refreshSpec
     * @return
     * @throws DBDishonourCheckedException
     */
    public Album doRAlbumByTribe(final String humanId, final long tribeId, final RefreshSpec refreshSpec) throws DBDishonourCheckedException, DBFetchDataException;
}
