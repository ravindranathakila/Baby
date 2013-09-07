package ai.baby.logic.crud.unit;

import ai.baby.util.exception.DBDishonourCheckedException;
import ai.ilikeplaces.entities.HumansPrivateEvent;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RHumansPrivateEventLocal {

    public HumansPrivateEvent doNTxRHumansPrivateEvent(String humanId) throws DBDishonourCheckedException;
}
