package ai.baby.logic.crud.unit;

import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.baby.util.exception.DBFetchDataException;
import ai.baby.util.jpa.CrudServiceLocal;
import ai.baby.util.AbstractSLBCallbacks;
import ai.scribble.License;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 14, 2010
 * Time: 12:04:30 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CPrivateLocation extends AbstractSLBCallbacks implements CPrivateLocationLocal {

    @EJB
    private CrudServiceLocal<PrivateLocation> privateLocationCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<HumansPrivateLocation> humansPrivateLocationCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<Human> humanCrudServiceLocal_;


    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public PrivateLocation doNTxCPrivateLocation(final String humanId, final String locationName, final String locationInfo) throws DBFetchDataException, DBRefreshDataException {
        final Human owner = humanCrudServiceLocal_.findBadly(Human.class, humanId);

        final PrivateLocation privateLocation = new PrivateLocation();

        privateLocation.setPrivateLocationName(locationName);
        privateLocation.setPrivateLocationInfo(locationInfo);

        final PrivateLocation returnVal = privateLocationCrudServiceLocal_.create(privateLocation);

        /*Give rights to CRUD private location*/
        returnVal.refresh().getPrivateLocationOwners().add(owner.getHumansPrivateLocation());

        /*Give rights to View private location*/
        returnVal.refresh().getPrivateLocationViewers().add(owner.getHumansPrivateLocation());

        /*Enrolled private locations. Right to view only*/
        owner.getHumansPrivateLocation().getPrivateLocationsViewed().add(returnVal);

        return returnVal;

    }

    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public PrivateLocation doNTxCPrivateLocation(final String humanId, final String locationName, final String locationInfo, final Double latitude, final Double longitude) throws DBFetchDataException, DBRefreshDataException {
        final Human owner = humanCrudServiceLocal_.findBadly(Human.class, humanId);

        final PrivateLocation privateLocation = new PrivateLocation();

        privateLocation.setPrivateLocationName(locationName);
        privateLocation.setPrivateLocationInfo(locationInfo);
        privateLocation.setPrivateLocationLatitude(latitude);
        privateLocation.setPrivateLocationLongitude(longitude);

        final PrivateLocation returnVal = privateLocationCrudServiceLocal_.create(privateLocation);

        /*Give rights to CRUD private location*/
        returnVal.refresh().getPrivateLocationOwners().add(owner.getHumansPrivateLocation());

        /*Give rights to View private location*/
        returnVal.refresh().getPrivateLocationViewers().add(owner.getHumansPrivateLocation());

        /*Enrolled private locations. Right to view only*/
        owner.getHumansPrivateLocation().getPrivateLocationsViewed().add(returnVal);

        return returnVal;

    }
}
