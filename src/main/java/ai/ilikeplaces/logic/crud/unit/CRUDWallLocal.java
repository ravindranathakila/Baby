package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Wall;

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
    public Wall doDirtyRWall(final long wallId);

    /**
     * @param wallId
     * @param contentToBeAppended
     * @return Wall
     */
    public Wall doNTxUAppendToWall(final long wallId, final String contentToBeAppended);

    /**
     * @param wallId
     * @return Wall
     */
    public Wall doNTxUClearWall(final long wallId);

    /**
     * @param wallId
     */
    public void doNTxDWall(final long wallId);
}