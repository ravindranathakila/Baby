package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.EXPECTNULL;
import ai.ilikeplaces.doc.FORGETNULL;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.HumansPrivateEvent;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.exception.NoPrivilegesException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.AbstractSLBCallbacks;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
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
    
    private static final String DELETE_THIS_EVENT = "delete this event.";

    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public boolean doNTxDPrivateEvent(final String humanId, final long privateEventId) {

        boolean returnVal = false;

        checkIfOwner:
        {
            @FORGETNULL
            final HumansPrivateEvent humansPrivateEvent = humansPrivateEventCrudServiceLocal.find(HumansPrivateEvent.class, humanId);

            @EXPECTNULL
            final PrivateEvent privateEvent = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId);

            if (privateEvent.getPrivateEventOwners().contains(humansPrivateEvent)) {
                privateEventCrudServiceLocal_.delete(PrivateEvent.class, privateEvent.getPrivateEventId());
                returnVal = true;
            } else {
                throw new NoPrivilegesException(humanId, DELETE_THIS_EVENT);
            }
        }
        return returnVal;
    }
}
