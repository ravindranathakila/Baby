package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.exception.NoPrivilegesException;

import javax.ejb.Local;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 12, 2010
 * Time: 10:31:21 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UPrivateLocationLocal {

    public PrivateLocation doUPrivateLocationData(final String humanId__, final String privateLocationId__, final String privateLocationName__, final String privateLocationInfo__);

    public PrivateLocation doUPrivateLocationAddOwner(final String adder__, final long privateLocationId__, final HumansFriend addeee__) throws NoPrivilegesException, DBFetchDataException;

    public PrivateLocation doUPrivateLocationRemoveOwner(final String remover__, final long privateLocationId__, final HumansFriend removeee__) throws NoPrivilegesException, DBFetchDataException;

    public PrivateLocation doUPrivateLocationAddViewer(final String adder__, final long privateLocationId__, final HumansFriend addeee__) throws NoPrivilegesException, DBFetchDataException;

    public PrivateLocation doUPrivateLocationAddViewer(final String adder__, final long privateLocationId__, final String addeee__) throws NoPrivilegesException, DBFetchDataException;

    public PrivateLocation doUPrivateLocationRemoveViewer(final String remover__, final long privateLocationId__, final HumansFriend removeee__) throws DBDishonourCheckedException, DBFetchDataException;

}