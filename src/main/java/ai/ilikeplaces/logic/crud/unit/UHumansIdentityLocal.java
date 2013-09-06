package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.util.exception.DBDishonourCheckedException;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UHumansIdentityLocal {
    public HumansIdentity doUHumansProfilePhoto(final String humanId, final String url) throws DBDishonourCheckedException;

    public HumansIdentity doUHumansPublicURL(final String humanId, final String url) throws DBDishonourCheckedException;

    public void doUHumansPublicURLDeleteUrl(final String humanId) throws DBDishonourCheckedException;

    public void doUHumansPublicURLAdd(final String humanId, final String url) throws DBDishonourCheckedException;
}
