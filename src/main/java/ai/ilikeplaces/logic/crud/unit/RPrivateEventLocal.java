package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;

import javax.ejb.Local;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 12:07:04 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RPrivateEventLocal {

    public PrivateEvent doRPrivateEventAsAny(final String humanId, final long privateEventId) throws DBDishonourCheckedException, DBFetchDataException;

    public PrivateEvent doRPrivateEventAsSystem(final long privateEventId, final boolean eager) throws DBDishonourCheckedException, DBFetchDataException;

    public boolean doRPrivateEventIsOwner(final String humanId, final Long privateEventId) throws DBDishonourCheckedException, DBFetchDataException;

    public boolean doRPrivateEventIsViewer(final String humanId, final Long privateEventId) throws DBDishonourCheckedException, DBFetchDataException;

    public PrivateEvent doRPrivateEventAsViewer(final String humanId, final Long privateEventId) throws DBDishonourCheckedException, DBFetchDataException;

    public PrivateEvent doRPrivateEventAsOwner(final String humanId, final Long privateEventId) throws DBDishonourCheckedException, DBFetchDataException;
}
