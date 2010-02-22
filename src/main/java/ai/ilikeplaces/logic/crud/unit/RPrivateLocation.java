package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.entities.PrivateLocation;
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
 * Date: Jan 18, 2010
 * Time: 10:45:35 PM
 */


@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class RPrivateLocation extends AbstractSLBCallbacks implements RPrivateLocationLocal {

    @EJB
    private CrudServiceLocal<PrivateLocation> privateLocationCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<HumansPrivateLocation> humansPrivateLocationCrudServiceLocal_;

    @Override
    public PrivateLocation doDirtyRPrivateLocation(final String humanId, final Long privateLocationId) {
        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId);
        final HumansPrivateLocation humansPrivateLocation_ = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, humanId);

        securityChecks:
        {
            if (!(privateLocation_.getPrivateLocationOwners().contains(humansPrivateLocation_)
                    || humansPrivateLocation_.getPrivateLocationsViewed().contains(privateLocation_))) {
                throw new NoPrivilegesException(humanId, "view private location:" + privateLocation_.toString());
            }
        }

        return privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId);
    }

    @Override
    public boolean doDirtyRPrivateLocationIsOwner(final String humanId, final Long privateLocationId) {
        return privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId).getPrivateLocationOwners()
                .contains(humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, humanId));
    }

    @Override
    public boolean doDirtyRPrivateLocationIsViewer(final String humanId, final Long privateLocationId) {
        return privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId).getPrivateLocationViewers()
                .contains(humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, humanId));
    }

    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public PrivateLocation doRPrivateLocationAsViewer(final String humanId, final Long privateLocationId) {
        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId);
        final HumansPrivateLocation humansPrivateLocation_ = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, humanId);

        securityChecks:
        {
            if (!(humansPrivateLocation_.getPrivateLocationsViewed().contains(privateLocation_))) {
                throw new NoPrivilegesException(RBGet.expMsgs.getString("ai.ilikeplaces.logic.crud.unit.RPrivateLocation.0001"));
            }
        }

        return privateLocation_;
    }

    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public PrivateLocation doRPrivateLocationAsOwner(final String humanId, final Long privateLocationId) {
        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId);
        final HumansPrivateLocation humansPrivateLocation_ = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, humanId);

        securityChecks:
        {
            if (!(privateLocation_.getPrivateLocationOwners().contains(humansPrivateLocation_))) {
                throw new NoPrivilegesException(RBGet.expMsgs.getString("ai.ilikeplaces.logic.crud.unit.RPrivateLocation.0002"));
            }
        }

        return privateLocation_;
    }
}
