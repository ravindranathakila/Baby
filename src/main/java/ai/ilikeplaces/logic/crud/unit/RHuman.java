package ai.ilikeplaces.logic.crud.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.jpa.CrudServiceLocal;
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
@NOTE(note="SEE CRUDSERVICE WHERE TO SUPPORT READ AND DIRTY READ, THE TX TYPE IS SUPPORTS.")
@Stateless
public class RHuman extends AbstractSLBCallbacks implements RHumanLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceLocal_;

    public RHuman() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", RHuman.class, this.hashCode());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public Human doRHuman(String humandId) {
        return crudServiceLocal_.find(Human.class, humandId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Human doNTxRHuman(String humandId) {
        return crudServiceLocal_.find(Human.class, humandId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Human doDirtyRHuman(String humanId) {
        return crudServiceLocal_.find(Human.class, humanId);
    }
    final static Logger logger = LoggerFactory.getLogger(RHuman.class);
}
