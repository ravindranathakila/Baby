package ai.baby.logic.crud.unit;

import ai.baby.util.exception.DBDishonourCheckedException;
import ai.baby.util.exception.DBFetchDataException;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.scribble.License;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 12:20:48 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RPrivateLocationLocal {

    /**
     * @param humanId
     * @param privateLocationId
     * @return
     * @throws ai.baby.util.exception.DBDishonourCheckedException
     * @throws ai.baby.util.exception.DBFetchDataException
     */
    public PrivateLocation doRPrivateLocationAsAny(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;

    /**
     * @param humanId
     * @param privateLocationId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public boolean doRPrivateLocationIsOwner(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;

    /**
     * @param humanId
     * @param privateLocationId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public boolean doRPrivateLocationIsViewer(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;

    /**
     * @param humanId
     * @param privateLocationId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public boolean doRPrivateLocationIsViewerOrOwner(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;

    /**
     * @param humanId
     * @param privateLocationId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public PrivateLocation doRPrivateLocationAsViewer(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;

    /**
     * @param humanId
     * @param privateLocationId
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public PrivateLocation doRPrivateLocationAsOwner(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;

    /**
     * @param PrivateLocationLatitudeSouth horizontal bottom of bounding box
     * @param PrivateLocationLatitudeNorth horizontal up of bounding box
     * @param PrivateLocationLongitudeWest vertical left of bounding box
     * @param PrivateLocationLongitudeEast vertical right of bounding box
     * @return the set of private locations inside a specific bounding box
     */
    public List<PrivateLocation> doRPrivateLocationsAsGlobal(
            final Double PrivateLocationLatitudeSouth,
            final Double PrivateLocationLatitudeNorth,
            final Double PrivateLocationLongitudeWest,
            final Double PrivateLocationLongitudeEast);

    /**
     * @param privateLocationId
     * @param eager
     * @return
     * @throws DBDishonourCheckedException
     * @throws DBFetchDataException
     */
    public PrivateLocation doRPrivateLocationAsSystem(final Long privateLocationId, final boolean eager) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;
}
