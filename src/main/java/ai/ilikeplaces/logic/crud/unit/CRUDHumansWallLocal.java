package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansWall;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.util.jpa.RefreshSpec;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CRUDHumansWallLocal {

    /**
     * Warning: lazy initialized fields ignored
     * @param humanId
     * @return
     */
    public HumansWall doRHumansWall(final String humanId, final RefreshSpec wallRefreshSpec) throws DBFetchDataException;

    public HumansWall doRHumansWall(final String humanId);

    public Long doDirtyRHumansWallID(final String humanId) throws DBDishonourCheckedException;
}