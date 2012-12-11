package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.scribble.License;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CRUDWallLocal {
    /**
     * @param wall
     * @return Wall
     */
    public Wall doNTxCWall(final Wall wall);

    /**
     * @param wallId
     * @return Wall
     * @throws ai.ilikeplaces.exception.DBDishonourCheckedException
     *
     */
    public Wall doDirtyRWall(final long wallId) throws DBDishonourCheckedException;

    /**
     * @param wallId
     * @param refreshSpec
     * @return Wall
     * @throws ai.ilikeplaces.exception.DBDishonourCheckedException
     *
     * @throws ai.ilikeplaces.exception.DBFetchDataException
     *
     */
    public Wall doRWall(final long wallId, final RefreshSpec refreshSpec) throws DBDishonourCheckedException, DBFetchDataException;

//    /**
//     * @param wallId
//     * @param contentToBeAppended
//     * @return Wall
//     * @throws ai.ilikeplaces.exception.DBDishonourCheckedException
//     */
//    @Deprecated
//    public Wall doNTxUAppendToWall(final long wallId, final String contentToBeAppended) throws DBDishonourCheckedException;

    /**
     * @param wallId
     * @param humanId
     * @param contentToBeAppended
     * @return Wall
     * @throws ai.ilikeplaces.exception.DBDishonourCheckedException
     *
     */
    public Wall doUAddEntry(long wallId, final String humanId, String contentToBeAppended) throws DBDishonourCheckedException;

    /**
     * @param wallId
     * @param mutee
     * @return Wall
     * @throws ai.ilikeplaces.exception.DBDishonourCheckedException
     *
     */
    public Wall doUAddMuteEntry(long wallId, final String mutee) throws DBDishonourCheckedException;

    /**
     * @param wallId
     * @param mutee
     * @return Wall
     * @throws ai.ilikeplaces.exception.DBDishonourCheckedException
     *
     */
    public Wall doURemoveMuteEntry(long wallId, final String mutee) throws DBDishonourCheckedException;

//    /**
//     * @param wallId
//     * @return Wall
//     * @throws ai.ilikeplaces.exception.DBDishonourCheckedException
//     */
//    public Wall doUClearWall(final long wallId) throws DBDishonourCheckedException;

    /**
     * @param wallId
     */
    public void doNTxDWall(final long wallId);

    public List<Msg> doRHumansWallLastEntries(final long wallId, final Integer numberOfEntriesToFetch);

    public void doUpdateMetadata(final long wallId, final String key, final String value);
}
