package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.RefreshException;
import ai.ilikeplaces.entities.RefreshSpec;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.jpa.QueryParameter;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ai.ilikeplaces.entities.Location.*;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@TODO(task = "i18n")
public class RLocation extends AbstractSLBCallbacks implements RLocationLocal {

    @EJB
    private CrudServiceLocal<Location> crudServiceLocation_;

    public RLocation() {
    }

    /**
     * Use if you are going to use this method, then you should be within a
     * transaction on which you will be doing some updates on this location. If
     * not, use the dirty read instead. Holding up a row in a long lived
     * transaction will be a huge bottleneck so dirty read is the best in most
     * cases.
     *
     * @param locationName
     * @param superLocationName
     * @return
     */
    @Override
    @WARNING(warning = "SUPER LOCATION CAN BE NULL(CALLER SENDS NULL OR DB HAS NULL). HENCE JUST FIND BY LOCATION. IF PLENTY(THEN SUPER IS DEFINITELY THERE, MATCH SUPER.")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Location doNTxRLocation(final String locationName, final String superLocationName) {
        return doCommonRLocation(locationName, superLocationName);
    }

    /**
     * Use if you are going to use this method, then you should be within a
     * transaction on which you will be doing some updates on this location. If
     * not, use the dirty read instead. Holding up a row in a long lived
     * transaction will be a huge bottleneck so dirty read is the best in most
     * cases.
     *
     * @param locationName
     * @param superLocationName
     * @return
     */
    @Override
    @WARNING(warning = "SUPER LOCATION CAN BE NULL(CALLER SENDS NULL OR DB HAS NULL). HENCE JUST FIND BY LOCATION. IF PLENTY(THEN SUPER IS DEFINITELY THERE, MATCH SUPER.")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Location doRLocation(final String locationName, final String superLocationName) {
        return doCommonRLocation(locationName, superLocationName);
    }

    /**
     * @param locationName
     * @param superLocationName
     * @return
     */
    @Override
    @WARNING(warning = "SUPER LOCATION CAN BE NULL(CALLER SENDS NULL OR DB HAS NULL). HENCE JUST FIND BY LOCATION. IF PLENTY(THEN SUPER IS DEFINITELY THERE, MATCH SUPER.")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Location doDirtyRLocation(final String locationName, final String superLocationName) {
        return doCommonRLocation(locationName, superLocationName);
    }


    /**
     * @param locationName
     * @param superLocationName
     * @return
     */
    @WARNING(warning = "SUPER LOCATION CAN BE NULL(CALLER SENDS NULL OR DB HAS NULL). HENCE JUST FIND BY LOCATION. IF PLENTY(THEN SUPER IS DEFINITELY THERE, MATCH SUPER.")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    private Location doCommonRLocation(final String locationName, final String superLocationName) {

        logger.debug("HELLO, I AM QUERYING FOR A LOCATION {} OF WHICH SUPER LOCATION IS {}.", locationName, superLocationName);

        final Location returnVal;
        @SuppressWarnings("unchecked")
        final List<Location> existingLocations = crudServiceLocation_.findWithNamedQuery(FindAllLocationsByName,
                QueryParameter.with(LocationName, locationName).parameters());

        if (existingLocations.size() == 1) {
            returnVal = existingLocations.get(0);
        } else if (existingLocations.size() > 1) {
            boolean found = false;
            Location tempVal = null;
            superLocationMatchLoop:
            {
                for (final Location location : existingLocations) {
                    @NOTE(note = "THIS CAN NEVER BE NULL")
                    final Location superLocation = location.getLocationSuperSet();
                    if (superLocation.getLocationName().equals(superLocationName)) {
                        tempVal = location;
                        found = true;
                        break superLocationMatchLoop;
                    }
                }
            }
            if (found) {
                returnVal = tempVal;
            } else {
                logger.debug("HELLO, NO SUCH LOCATION AS {} OF WHICH SUPER LOCATION IS {}.", locationName, superLocationName);
                returnVal = null;
            }

        } else {
            logger.debug("HELLO, NO SUCH LOCATION AS {}. I DID NOT COMPARE SUPER LOCATIONS AS LOCATIONS DID NOT MATCH.", locationName, superLocationName);
            returnVal = null;
        }
        return returnVal;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Location doRLocation(final long locationId) {
        return crudServiceLocation_.find(Location.class, locationId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Location doDirtyRLocation(final long locationId) {
        return crudServiceLocation_.find(Location.class, locationId);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Location doRLocation(final long locationId, final RefreshSpec refreshSpec) throws DBFetchDataException {
        try {
            return doRLocation(locationId).refresh(refreshSpec);
        } catch (final RefreshException e) {
            throw new DBFetchDataException(e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<String> doDirtyRLikeLocationNames(final String locationName) {
        return crudServiceLocation_.findWithNamedQuery(FindAllLocationNamesByLikeName,
                QueryParameter.with(LocationName, locationName.toUpperCase() + "%").parameters());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Location> doDirtyRLikeLocations(final String locationName) {
        return crudServiceLocation_.findWithNamedQuery(FindAllLocationsByLikeName,
                QueryParameter.with(LocationName, locationName.toUpperCase() + "%").parameters());
    }

    @WARNING(warning = "We can receive a detached entity which JPA cannot use to query. It needs an attached entity.")
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Location> doNTxRLocationsBySuperLocation(final Location locationSuperset) {
        @FIXME(issue = "The list JPA returns in read only. Using orderby makes it a db ordering. This uses shallow copy")
        final List<Location> sort = new ArrayList<Location>(crudServiceLocation_.findWithNamedQuery(FindAllLocationsBySuperLocation,
                QueryParameter.with(LocationSuperSet, doRLocation(locationSuperset.getLocationId())).parameters()));
        Collections.sort(sort);
        return sort;
    }

    final static Logger logger = LoggerFactory.getLogger(RLocation.class);
}
