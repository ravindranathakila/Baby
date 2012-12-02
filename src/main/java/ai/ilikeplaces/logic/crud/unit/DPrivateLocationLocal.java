package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.ilikeplaces.exception.DBFetchDataException;

import javax.ejb.Local;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 12, 2010
 * Time: 10:31:21 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface DPrivateLocationLocal {

    public boolean doNTxDPrivateLocation(final String humanId, final long privateLocationId) throws DBFetchDataException, DBRefreshDataException;

}
