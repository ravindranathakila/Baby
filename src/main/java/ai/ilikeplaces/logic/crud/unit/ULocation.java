package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.Loggers;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/19/10
 * Time: 9:32 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class ULocation extends AbstractSLBCallbacks implements ULocationLocal {

    @EJB
    private CrudServiceLocal<Location> crudServiceLocation_;

    public ULocation() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Location doULocationLatLng(long locationId, final double latitude, final double longitude) {
        final Location managedLocation = crudServiceLocation_.findBadly(Location.class, locationId);
        managedLocation.setLocationGeo1(String.valueOf(latitude));
        managedLocation.setLocationGeo2(String.valueOf(longitude));
        return managedLocation;
    }
}
