package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.logic.crud.unit.RLocationLocal;
import ai.ilikeplaces.logic.crud.unit.ULocationLocal;
import ai.ilikeplaces.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.Map;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({EntityManagerInjector.class, DBOffline.class, ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
public class HumanCRUDLocation extends AbstractSLBCallbacks implements HumanCRUDLocationLocal {

    private static final String FIND_LOCATION_BY_LOCATION_ID_SUCCESSFUL = "Find location by location Id Successful!";
    private static final String FETCH_LOCATION_BY_ID_AND_REFRESH_SPEC_FAILED = "Fetch location by Id and RefreshSpec FAILED!";
    @EJB
    private RLocationLocal rLocationLocal_;

    @EJB
    private ULocationLocal uLocationLocal_;

    public HumanCRUDLocation() {
    }

    @Override
    @TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
    public Return<Location> dirtyRLocation(final String locationName, final String superLocationName) {

        Return<Location> r;
        r = new ReturnImpl<Location>(rLocationLocal_.doDirtyRLocation(locationName, superLocationName), "Find location by location and super location names Successful!");
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Location> dirtyRLocation(final long locationId) {
        Return<Location> r;
        r = new ReturnImpl<Location>(rLocationLocal_.doDirtyRLocation(locationId), "Find location by location id Successful!");
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Location> doRLocation(final long locationId, final RefreshSpec refreshSpec) {
        Return<Location> r;
        try {
            r = new ReturnImpl<Location>(rLocationLocal_.doRLocation(locationId, refreshSpec), FIND_LOCATION_BY_LOCATION_ID_SUCCESSFUL);
            return r;
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Location>(t, FETCH_LOCATION_BY_ID_AND_REFRESH_SPEC_FAILED, true);
        }
        return r;
    }

    public Return<Location> doULocationComments(final long locationId, final Map<String, String> posts, final RefreshSpec refreshSpec) {
        Return<Location> r;
        try {
            r = new ReturnImpl<Location>(uLocationLocal_.doULocationPosts(locationId, posts, refreshSpec), "Update Location Posts Successful!");
            return r;
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Location>(t, "Update Location Posts FAILED!", true);
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
    public List<Location> doDirtyRLocationsBySuperLocation(final Location locationSuperset) {
        return rLocationLocal_.doNTxRLocationsBySuperLocation(locationSuperset);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Location doULocationLatLng(final RefObj<Long> locationId, final RefObj<Double> latitude, final RefObj<Double> longitude) {
        return uLocationLocal_.doULocationLatLng(locationId.getObjectAsValid(), latitude.getObjectAsValid(), longitude.getObjectAsValid());
    }

}
