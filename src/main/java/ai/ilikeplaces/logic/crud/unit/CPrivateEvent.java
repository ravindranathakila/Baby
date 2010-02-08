package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 14, 2010
 * Time: 12:04:30 AM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CPrivateEvent extends AbstractSLBCallbacks implements CPrivateEventLocal {

    @EJB
    private CrudServiceLocal<PrivateLocation> privateLocationCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<PrivateEvent> privateEventCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<HumansPrivateLocation> humansPrivateLocationCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<Human> humanCrudServiceLocal_;

    @EJB
    private RPrivateLocationLocal rPrivateLocationLocal_;


    @Override
    public PrivateEvent doNTxCPrivateEvent(final String humanId, final long privateLocationId, final String locationName, final String locationInfo, final Date startDate, final Date endDate) {
        final Human managedOwner = humanCrudServiceLocal_.find(Human.class, humanId);


        @WARNING(warning = "This call will throw an exception if user has not rights to the location So do not move it to AFTER creation of PrivateEvent." +
                "Even so, the transaction manager will roll this back too, but do it the safe way.")
        final PrivateLocation managedPrivateLocation = rPrivateLocationLocal_.doRPrivateLocation(humanId, privateLocationId);


        final PrivateEvent privateEvent = new PrivateEvent();

        privateEvent.setPrivateEventName(locationName);
        privateEvent.setPrivateEventInfo(locationInfo);

        final PrivateEvent managedPrivateEvent = privateEventCrudServiceLocal_.create(privateEvent);

        managedPrivateEvent.setPrivateLocation(managedPrivateLocation);
        managedPrivateLocation.getPrivateEvents().add(managedPrivateEvent);
        
        managedPrivateEvent.getPrivateEventOwners().add(managedOwner.getHumansPrivateEvent());
        managedOwner.getHumansPrivateEvent().getPrivateEventOwned().add(managedPrivateEvent);

        managedPrivateEvent.getPrivateEventViewers().add(managedOwner.getHumansPrivateEvent());
        managedOwner.getHumansPrivateEvent().getPrivateEventViewed().add(managedPrivateEvent);
        


        return managedPrivateEvent;

    }
}