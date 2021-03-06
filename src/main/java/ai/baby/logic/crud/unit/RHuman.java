package ai.baby.logic.crud.unit;

import ai.ilikeplaces.entities.Human;
import ai.baby.util.jpa.CrudServiceLocal;
import ai.baby.util.AbstractSLBCallbacks;
import ai.scribble.License;
import ai.scribble._note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_note(note = "SEE CRUDSERVICE WHERE TO SUPPORT READ AND DIRTY READ, THE TX TYPE IS SUPPORTS.")
@Stateless
public class RHuman extends AbstractSLBCallbacks implements RHumanLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceLocal_;

    public RHuman() {
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Human doRHuman(String humanId) {
        return crudServiceLocal_.find(Human.class, humanId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Human doNTxRHuman(String humanId) {
        return crudServiceLocal_.find(Human.class, humanId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Human doDirtyRHuman(String humanId) {
        return crudServiceLocal_.find(Human.class, humanId);
    }

    /**
     * If a user exists, returns true;
     *
     * @param humanId
     * @return userExists
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean doDirtyCheckHuman(final String humanId) {
        return crudServiceLocal_.find(Human.class, humanId) != null;
    }

    final static Logger logger = LoggerFactory.getLogger(RHuman.class);
}
