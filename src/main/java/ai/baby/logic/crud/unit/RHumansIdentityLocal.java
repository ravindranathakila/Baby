package ai.baby.logic.crud.unit;

import ai.ilikeplaces.entities.HumansIdentity;
import ai.baby.util.exception.DBDishonourCheckedException;
import ai.scribble.License;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RHumansIdentityLocal {

    public List<HumansIdentity> doDirtyRHumansIdentitiesByEmails(final List<String> emails);

    public String doDirtyProfilePhoto(final String humanId) throws DBDishonourCheckedException;

    public String doDirtyPublicURL(final String humanId) throws DBDishonourCheckedException;

    public HumansIdentity doDirtyRHumansIdentity(final String humanId) throws DBDishonourCheckedException;

    public HumansIdentity doRHumansIdentity(final String humanId) throws DBDishonourCheckedException;

    public String doDirtyProfileFromURL(final String url);

    public String test(final String emails);
}
