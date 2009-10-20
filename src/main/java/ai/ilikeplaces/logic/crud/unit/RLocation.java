package ai.ilikeplaces.logic.crud.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.entities.Location;
import static ai.ilikeplaces.entities.Location.*;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.jpa.QueryParameter;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class RLocation extends AbstractSLBCallbacks implements RLocationLocal {

    @EJB
    private CrudServiceLocal<Location> crudServiceLocation_;

    public RLocation() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", RLocation.class, this.hashCode());
    }

    @Override
    @WARNING(warning = "SUPER LOCATION CAN BE NULL(CALLER SENDS NULL OR DB HAS NULL). HENCE JUST FIND BY LOCATION. IF PLENTY(THEN SUPER IS DEFINITELY THERE, MATCH SUPER.")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Location doRLocation(final String locationName, final String superLocationName) {

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
            logger.debug("HELLO, NO SUCH LOCATION AS {}. I DID NOT COMPARE SUPER LOCATIONS AS LOCATIONS DID NOT MATCHf.", locationName, superLocationName);
            returnVal = null;
        }
        return returnVal;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Location doRLocation(final long locationId) {
        return crudServiceLocation_.find(Location.class, locationId);
    }

    final static Logger logger = LoggerFactory.getLogger(RLocation.class);
}
