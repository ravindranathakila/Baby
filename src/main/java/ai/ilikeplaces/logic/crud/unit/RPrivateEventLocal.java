package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.util.Return;

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

    public PrivateEvent doDirtyRPrivateEvent(final String humanId, final long privateEventId) throws DBDishonourCheckedException;

    public boolean doDirtyRPrivateEventIsOwner(final String humanId, final Long privateEventId) throws DBDishonourCheckedException;

    public boolean doDirtyRPrivateEventIsViewer(final String humanId, final Long privateEventId) throws DBDishonourCheckedException;

    public PrivateEvent doRPrivateEventAsViewer(final String humanId, final Long privateEventId) throws DBDishonourCheckedException;

    public PrivateEvent doRPrivateEventAsOwner(final String humanId, final Long privateEventId) throws DBDishonourCheckedException;
}
