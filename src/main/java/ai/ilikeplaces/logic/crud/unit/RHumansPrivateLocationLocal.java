package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RHumansPrivateLocationLocal {

    public HumansPrivateLocation doNTxRHumansPrivateLocation(String humanId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;
}
