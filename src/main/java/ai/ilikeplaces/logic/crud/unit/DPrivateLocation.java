package ai.ilikeplaces.logic.crud.unit;

import ai.doc.License;
import ai.doc._note;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.AbstractSLBCallbacks;

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
public class DPrivateLocation extends AbstractSLBCallbacks implements DPrivateLocationLocal {

    @EJB
    private CrudServiceLocal<PrivateLocation> privateLocationCrudServiceLocal_;


    @EJB
    private CrudServiceLocal<HumansPrivateLocation> humansPrivateLocationCrudServiceLocal;

    @_note(note = "Complex Logic", mustsee = "logic.conf.1")
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public boolean doNTxDPrivateLocation(final String humanId, final long privateLocationId) throws DBFetchDataException, DBRefreshDataException {

        boolean returnVal = false;

        checkIfOwner:
        {
            final HumansPrivateLocation humansPrivateLocation = humansPrivateLocationCrudServiceLocal.find(HumansPrivateLocation.class, humanId);

            final PrivateLocation privateLocation = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId);

            final List<HumansPrivateLocation> privateLocationOwners = privateLocation.refresh().getPrivateLocationOwners();
            if (privateLocationOwners.contains(humansPrivateLocation)) {
                if (privateLocationOwners.size() == 1) {
                    privateLocationCrudServiceLocal_.delete(PrivateLocation.class, privateLocation.getPrivateLocationId());
                    returnVal = true;
                } else {
                    List<HumansPrivateLocation> privateLocationViewers = privateLocation.getPrivateLocationViewers();
                    privateLocationOwners.remove(humansPrivateLocation);
                    privateLocationViewers.remove(humansPrivateLocation);
                    //Not removing this human from all private events associated with this location since this might happen accidentally
                    //In that case, upon reallocating the user as a owner or viewer, he can navigate to the specific moment.
                }
            } else {
                throw new SecurityException(RBGet.expMsgs.getString("ai.ilikeplaces.logic.crud.unit.DPrivateLocation.0001"));
            }
        }
        return returnVal;
    }
}
