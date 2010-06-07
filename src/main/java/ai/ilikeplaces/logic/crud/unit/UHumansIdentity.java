package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class UHumansIdentity extends AbstractSLBCallbacks implements UHumansIdentityLocal {

    @EJB
    private CrudServiceLocal<HumansIdentity> crudServiceLocal_;

    public UHumansIdentity() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", UHumansIdentity.class, this.hashCode());
    }


    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    @Override
    public HumansIdentity doUHumansProfilePhoto(final String humanId, final String url) {
        final HumansIdentity humansIdentity = crudServiceLocal_.findBadly(HumansIdentity.class, humanId);
        humansIdentity.setHumansIdentityProfilePhoto(url);
        return humansIdentity;
    }
}