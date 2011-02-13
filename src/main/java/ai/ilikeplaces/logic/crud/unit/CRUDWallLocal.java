package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.exception.DBDishonourCheckedException;

import javax.ejb.Local;

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
     */
    public Wall doDirtyRWall(final long wallId) throws DBDishonourCheckedException;

    /**
     * @param wallId
     * @param contentToBeAppended
     * @return Wall
     */
    @Deprecated
    public Wall doNTxUAppendToWall(final long wallId, final String contentToBeAppended) throws DBDishonourCheckedException;

    /**
     * @param wallId
     * @param contentToBeAppended
     * @return Wall
     */
    public Wall doUAddEntry(long wallId, final String humanId, String contentToBeAppended) throws DBDishonourCheckedException;

    /**
     * @param wallId
     * @return Wall
     */
    public Wall doUAddMuteEntry(long wallId, final String mutee) throws DBDishonourCheckedException;

    /**
     * @param wallId
     * @return Wall
     */
    public Wall doURemoveMuteEntry(long wallId, final String mutee) throws DBDishonourCheckedException;

    /**
     * @param wallId
     * @return Wall
     */
    public Wall doUClearWall(final long wallId) throws DBDishonourCheckedException;

    /**
     * @param wallId
     */
    public void doNTxDWall(final long wallId);
}