package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.logic.validators.unit.HumanId;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 12:07:04 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RPrivateEventLocal {

    /**
     * @param privateEventId
     * @return
     * @throws DBFetchDataException
     */
    public PrivateEvent rPrivateEventBadly(long privateEventId) throws DBFetchDataException;


    /**
     * @param humanId
     * @param privateEventId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public PrivateEvent doRPrivateEventAsAny(final String humanId, final long privateEventId) throws DBDishonourCheckedException, DBFetchDataException;

    /**
     * Returns an un-refreshed {@link java.awt.event.PaintEvent}
     *
     * @param humanId
     * @param privateEventId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public PrivateEvent doRPrivateEventBasicAsAny(final String humanId, final long privateEventId) throws DBDishonourCheckedException, DBFetchDataException;

    /**
     * @param privateEventId
     * @param eager
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public PrivateEvent doRPrivateEventAsSystem(final long privateEventId, final boolean eager) throws DBDishonourCheckedException, DBFetchDataException;

    /**
     * @param humanId
     * @param privateEventId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public boolean doRPrivateEventIsOwner(final String humanId, final Long privateEventId) throws DBDishonourCheckedException, DBFetchDataException;

    /**
     * @param humanId
     * @param privateEventId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public boolean doRPrivateEventIsViewer(final String humanId, final Long privateEventId) throws DBDishonourCheckedException, DBFetchDataException;

    /**
     * @param humanId
     * @param privateEventId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public PrivateEvent doRPrivateEventAsViewer(final String humanId, final Long privateEventId) throws DBDishonourCheckedException, DBFetchDataException;

    /**
     * @param humanId
     * @param privateEventId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public PrivateEvent doRPrivateEventAsOwner(final String humanId, final Long privateEventId) throws DBDishonourCheckedException, DBFetchDataException;

    /**
     * @param privateLocationId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public List<PrivateEvent> doDirtyRPrivateEventsByPrivateLocationAsSystem(final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException;

    /**
     * @param latitudeSouth horizontal bottom of bounding box
     * @param latitudeNorth horizontal up of bounding box
     * @param longitudeWest vertical left of bounding box
     * @param longitudeEast vertical right of bounding box
     * @return the list of private events inside a specific bounding box
     */
    public List<PrivateEvent> doRPrivateEventsByBoundingBoxAsSystem(
            final double latitudeSouth,
            final double latitudeNorth,
            final double longitudeWest,
            final double longitudeEast) throws DBFetchDataException, DBDishonourCheckedException;

    public List<PrivateEvent> doRPrivateEventsOfHuman(final String humanId);
}
