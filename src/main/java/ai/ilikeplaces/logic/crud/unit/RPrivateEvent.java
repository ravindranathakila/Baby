package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansPrivateEvent;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.exception.NoPrivilegesException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 18, 2010
 * Time: 10:45:35 PM
 */


@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class RPrivateEvent extends AbstractSLBCallbacks implements RPrivateEventLocal {

    @EJB
    private CrudServiceLocal<PrivateEvent> privateEventCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<HumansPrivateEvent> humansPrivateEventCrudServiceLocal_;

    private static final String READ_EVENT_AS_OWNER = "read event as owner.";
    private static final String READ_EVENT_AS_VIEWER = "read event as viewer.";
    private static final String VIEW_PRIVATE_LOCATION = "view private location:";

    @Override
    public PrivateEvent doDirtyRPrivateEvent(final String humanId, final long privateEventId) {
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.findBadly(PrivateEvent.class, privateEventId);

        final HumansPrivateEvent humansPrivateEvent_ = humansPrivateEventCrudServiceLocal_.findBadly(HumansPrivateEvent.class, humanId);

        securityChecks:
        {
            if (!(privateEvent_.getPrivateEventOwners().contains(humansPrivateEvent_)
                    || humansPrivateEvent_.getPrivateEventsViewed().contains(privateEvent_))) {
                throw new NoPrivilegesException(humanId, VIEW_PRIVATE_LOCATION + privateEvent_.toString());
            }
        }

        return privateEvent_;
    }

    @Override
    public boolean doDirtyRPrivateEventIsOwner(final String humanId, final Long privateEventId) {
        return privateEventCrudServiceLocal_.findBadly(PrivateEvent.class, privateEventId).getPrivateEventOwners()
                .contains(humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, humanId));
    }

    @Override
    public boolean doDirtyRPrivateEventIsViewer(final String humanId, final Long privateEventId) {
        return privateEventCrudServiceLocal_.findBadly(PrivateEvent.class, privateEventId).getPrivateEventViewers()
                .contains(humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, humanId));
    }

    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public PrivateEvent doRPrivateEventAsViewer(final String humanId, final Long privateEventId) {
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId);
        final HumansPrivateEvent humansPrivateEvent_ = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, humanId);

        securityChecks:
        {
            if (!(humansPrivateEvent_.getPrivateEventsViewed().contains(privateEvent_))) {
                throw new NoPrivilegesException(humanId, READ_EVENT_AS_VIEWER);
            }
        }

        return privateEvent_;
    }

    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public PrivateEvent doRPrivateEventAsOwner(final String humanId, final Long privateEventId) {
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId);
        final HumansPrivateEvent humansPrivateEvent_ = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, humanId);

        securityChecks:
        {
            if (!(privateEvent_.getPrivateEventOwners().contains(humansPrivateEvent_))) {
                throw new NoPrivilegesException(humanId, READ_EVENT_AS_OWNER);
            }
        }

        return privateEvent_;
    }
}
