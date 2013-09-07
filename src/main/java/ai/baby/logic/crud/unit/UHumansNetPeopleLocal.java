package ai.baby.logic.crud.unit;

import ai.baby.util.exception.DBDishonourCheckedException;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */


@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UHumansNetPeopleLocal {

    public boolean doNTxAddHumansNetPeople(final String adderHumanId, final String addeeHumanId) throws DBDishonourCheckedException;

    public boolean doNTxRemoveHumansNetPeople(final String adderHumanId, final String addeeHumanId) throws DBDishonourCheckedException;

    public boolean doNTxIsHumansNetPeople(final String checker, final String checkeee);

    public boolean doDirtyIsHumansNetPeople(final String adderHumanId, final String addeeHumanId);
}
