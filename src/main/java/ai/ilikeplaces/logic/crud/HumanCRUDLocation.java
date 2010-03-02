package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.logic.crud.unit.RLocationLocal;
import ai.ilikeplaces.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({MethodTimer.class, MethodParams.class})
public class HumanCRUDLocation extends AbstractSLBCallbacks implements HumanCRUDLocationLocal {

    @EJB
    private RLocationLocal rLocationLocal_;

    public HumanCRUDLocation() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", HumanCRUDLocation.class, this.hashCode());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Return<Location> dirtyRLocation(final String locationName, final String superLocationName) {

        Return<Location> r;
        try {
            r = new ReturnImpl<Location>(rLocationLocal_.doDirtyRLocation(locationName, superLocationName), "Find location by location and super location names Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<Location>(t, "Find location by location and super location names FAILED!", true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Location> dirtyRLocation(final long locationId) {
        Return<Location> r;
        try {
            r = new ReturnImpl<Location>(rLocationLocal_.doDirtyRLocation(locationId), "Find location by location id Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<Location>(t, "Find location by location id FAILED!", true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<String> dirtyRLikeLocationNames(final String likeLocationName) {
        return rLocationLocal_.doDirtyRLikeLocationNames(likeLocationName);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Location> dirtyRLikeLocations(final String likeLocationName) {
        return rLocationLocal_.doDirtyRLikeLocations(likeLocationName);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Location> doNTxRLocationsBySuperLocation(final Location locationSuperset) {
        return rLocationLocal_.doNTxRLocationsBySuperLocation(locationSuperset);
    }

    final static Logger logger = LoggerFactory.getLogger(HumanCRUDLocation.class);
}
