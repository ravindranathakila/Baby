package ai.baby.logic.crud.unit;

import ai.baby.util.AbstractSLBCallbacks;
import ai.ilikeplaces.entities.Human;
import ai.baby.util.jpa.CrudServiceLocal;
import ai.scribble.License;
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
public class CHuman extends AbstractSLBCallbacks implements CHumanLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceLocal_;

    public CHuman() {
    }

    final static Logger logger = LoggerFactory.getLogger(CHuman.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void doNTxCHuman(final Human newUser) {
        crudServiceLocal_.create(newUser);
    }
}
