package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
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
@Stateless
public class UHuman extends AbstractSLBCallbacks implements UHumanLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceLocal_;

    public UHuman() {
    }

    final static Logger logger = LoggerFactory.getLogger(UHuman.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean doUActivateHuman(final String humanId) {
        crudServiceLocal_.findBadly(Human.class, humanId).setHumanAlive(true);
        return true;
    }
}