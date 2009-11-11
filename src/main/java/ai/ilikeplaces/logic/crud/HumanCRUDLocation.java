package ai.ilikeplaces.logic.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.logic.crud.unit.RLocationLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class HumanCRUDLocation extends AbstractSLBCallbacks implements HumanCRUDLocationLocal{

    @EJB
    private RLocationLocal rLocationLocal_;

    public HumanCRUDLocation() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", HumanCRUDLocation.class, this.hashCode());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Location doDirtyHumanRLocation(final String locationName, final String superLocationName) {
        return rLocationLocal_.doDirtyRLocation(locationName, superLocationName);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public Location doDirtyHumanRLocation(final long locationId) {
        return rLocationLocal_.doDirtyRLocation(locationId);
    }

    final static Logger logger = LoggerFactory.getLogger(HumanCRUDLocation.class);
}
