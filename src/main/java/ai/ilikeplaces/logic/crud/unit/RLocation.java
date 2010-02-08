package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.jpa.QueryParameter;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

import static ai.ilikeplaces.entities.Location.*;

/**
 *
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@TODO(task = "i18n")
public class RLocation extends AbstractSLBCallbacks implements RLocationLocal {

    @EJB
    private CrudServiceLocal<Location> crudServiceLocation_;

    public RLocation() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", RLocation.class, this.hashCode());
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
        return doCommonRLocation(locationName,superLocationName);
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
        return doCommonRLocation(locationName,superLocationName);
    }

    /**
     *
     * @param locationName
     * @param superLocationName
     * @return
     */
    @Override
    @WARNING(warning = "SUPER LOCATION CAN BE NULL(CALLER SENDS NULL OR DB HAS NULL). HENCE JUST FIND BY LOCATION. IF PLENTY(THEN SUPER IS DEFINITELY THERE, MATCH SUPER.")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Location doDirtyRLocation(final String locationName, final String superLocationName) {
        return doCommonRLocation(locationName,superLocationName);
    }


    /**
     * @param locationName
     * @param superLocationName
     * @return
     */
    @WARNING(warning = "SUPER LOCATION CAN BE NULL(CALLER SENDS NULL OR DB HAS NULL). HENCE JUST FIND BY LOCATION. IF PLENTY(THEN SUPER IS DEFINITELY THERE, MATCH SUPER.")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    private  Location doCommonRLocation(final String locationName, final String superLocationName) {

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
                        tempVal = existingLocations.get(0);
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

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<String> doDirtyRLikeLocation(final String locationName){
        return crudServiceLocation_.findWithNamedQuery(FindAllLocationsByLikeName,
                QueryParameter.with(LocationName, locationName.toUpperCase()+"%").parameters());
    }
    final static Logger logger = LoggerFactory.getLogger(RLocation.class);
}
