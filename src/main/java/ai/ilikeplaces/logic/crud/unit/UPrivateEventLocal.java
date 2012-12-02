package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.ilikeplaces.entities.etc.HumansFriend;
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
public interface UPrivateEventLocal {

    public PrivateEvent doUPrivateEventData(final String humanId__, final long privateEventId__, final String privateEventName__, final String privateEventInfo__, final String privateEventStartDate__, final String privateEventE) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;

    public PrivateEvent doUPrivateEventAddOwner(final String adder__, final long privateEventId__, final HumansFriend addeee__) throws DBDishonourCheckedException;

    public PrivateEvent doUPrivateEventRemoveOwner(final String remover__, final long privateEventId__, final HumansFriend removeee__) throws DBDishonourCheckedException;

    public PrivateEvent doUPrivateEventAddViewer(final String adder__, final long privateEventId__, final HumansFriend addeee__) throws DBDishonourCheckedException;

    public PrivateEvent doUPrivateEventRemoveViewer(final String remover__, final long privateEventId__, final HumansFriend removeee__) throws DBDishonourCheckedException;

    public PrivateEvent doUPrivateEventAddInvite(final String adder__, final long privateEventId__, final HumansFriend addeee__) throws NoPrivilegesException;

    public PrivateEvent doUPrivateEventRemoveInvite(final String remover__, final long privateEventId__, final HumansFriend removeee__) throws DBDishonourCheckedException;

    public PrivateEvent doUPrivateEventAddReject(final String adder__, final long privateEventId__, final HumansFriend addeee__) throws DBDishonourCheckedException;

    public PrivateEvent doUPrivateEventRemoveReject(final String remover__, final long privateEventId__, final HumansFriend removeee__) throws DBDishonourCheckedException;

}
