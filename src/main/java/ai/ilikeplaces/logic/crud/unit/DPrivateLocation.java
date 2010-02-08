package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.entities.PrivateLocation;
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
public class DPrivateLocation extends AbstractSLBCallbacks implements DPrivateLocationLocal {

    @EJB
    private CrudServiceLocal<PrivateLocation> privateLocationCrudServiceLocal_;


    @EJB
    private CrudServiceLocal<HumansPrivateLocation> humansPrivateLocationCrudServiceLocal;

    @NOTE(note = "Complex Logic", mustsee = "logic.conf.1")
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public boolean doNTxDPrivateLocation(final String humanId, final long privateLocationId) {

        boolean returnVal = false;

        checkIfOwner:
        {
            final HumansPrivateLocation humansPrivateLocation = humansPrivateLocationCrudServiceLocal.find(HumansPrivateLocation.class, humanId);

            final PrivateLocation privateLocation = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId);

            if (privateLocation.getPrivateLocationOwners().contains(humansPrivateLocation)) {
                privateLocationCrudServiceLocal_.delete(PrivateLocation.class, privateLocation.getPrivateLocationId());
                returnVal = true;
            } else {
                throw new SecurityException(RBGet.expMsgs.getString("ai.ilikeplaces.logic.crud.unit.DPrivateLocation.0001"));
            }
        }
        return returnVal;
    }
}
