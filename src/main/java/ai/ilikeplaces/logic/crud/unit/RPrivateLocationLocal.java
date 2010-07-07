package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;

import javax.ejb.Local;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 12:20:48 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RPrivateLocationLocal {

    public PrivateLocation doRPrivateLocationAsAny(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException;

    public boolean doRPrivateLocationIsOwner(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException;

    public boolean doRPrivateLocationIsViewer(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException;

    public PrivateLocation doRPrivateLocationAsViewer(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException;

    public PrivateLocation doRPrivateLocationAsOwner(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException;
}
