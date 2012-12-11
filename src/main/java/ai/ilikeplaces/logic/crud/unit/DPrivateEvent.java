package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.HumansPrivateEvent;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.exception.NoPrivilegesException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.scribble.License;
import ai.scribble._expect_null;
import ai.scribble._forget_null;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 14, 2010
 * Time: 11:53:11 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class DPrivateEvent extends AbstractSLBCallbacks implements DPrivateEventLocal {

    @EJB
    private CrudServiceLocal<PrivateEvent> privateEventCrudServiceLocal_;


    @EJB
    private CrudServiceLocal<HumansPrivateEvent> humansPrivateEventCrudServiceLocal;

    private static final String DELETE_THIS_EVENT = "delete this event. Reason: Neither a Owner of Viewer. Probable cause is that the visual representation of Owners or Viewers are wrong!";

    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public boolean doNTxDPrivateEvent(final String humanId, final long privateEventId) throws NoPrivilegesException {

        boolean returnVal = false;

        checkIfOwner:
        {
            @_forget_null
            final HumansPrivateEvent humansPrivateEvent = humansPrivateEventCrudServiceLocal.find(HumansPrivateEvent.class, humanId);

            @_expect_null
            final PrivateEvent privateEvent = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId);

            final List<HumansPrivateEvent> privateEventOwners = privateEvent.getPrivateEventOwners();

            if (privateEventOwners.contains(humansPrivateEvent)) {
                if (privateEventOwners.size() == 1) {
                    privateEventCrudServiceLocal_.delete(PrivateEvent.class, privateEvent.getPrivateEventId());
                    returnVal = true;
                } else {
                    privateEventOwners.remove(humansPrivateEvent);

                    final List<HumansPrivateEvent> privateEventViewers = privateEvent.getPrivateEventViewers();
                    if (privateEventViewers.contains(humansPrivateEvent)) {
                        privateEventViewers.remove(humansPrivateEvent);
                    }

                    final List<HumansPrivateEvent> privateEventInvites = privateEvent.getPrivateEventInvites();
                    if (privateEventInvites.contains(humansPrivateEvent)) {
                        privateEventInvites.remove(humansPrivateEvent);
                    }
                }
            } else {
                final List<HumansPrivateEvent> privateEventViewers = privateEvent.getPrivateEventViewers();
                if (privateEventViewers.contains(humansPrivateEvent)) {
                    privateEventViewers.remove(humansPrivateEvent);
                } else {
                    throw new NoPrivilegesException(humanId, DELETE_THIS_EVENT);
                }
            }
        }
        return returnVal;
    }
}
