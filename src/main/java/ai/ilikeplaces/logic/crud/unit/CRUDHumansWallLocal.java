package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansWall;
import ai.ilikeplaces.exception.DBDishonourCheckedException;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CRUDHumansWallLocal {
    public HumansWall dirtyRHumansWall(final String humanId);

    public HumansWall rHumansWall(final String humanId) throws DBDishonourCheckedException;

    public Long dirtyRHumansWallID(final String humanId) throws DBDishonourCheckedException;
}