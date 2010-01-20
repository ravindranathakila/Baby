package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
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
    public PrivateLocation doNTxCPrivateLocation(final String humanId, final String locationName, final String locationInfo) {
        final Human owner = humanCrudServiceLocal_.find(Human.class, humanId);

        final PrivateLocation privateLocation = new PrivateLocation();

        privateLocation.setPrivateLocationName(locationName);
        privateLocation.setPrivateLocationInfo(locationInfo);

        final PrivateLocation returnVal = privateLocationCrudServiceLocal_.create(privateLocation);

        /*Give rights to CRUD private location*/
        returnVal.getPrivateLocationOwners().add(owner.getHumansPrivateLocation());

        /*Enrolled private locations. Right to view only*/
        owner.getHumansPrivateLocation().getPrivateLocations().add(returnVal);

        return returnVal;

    }
}
