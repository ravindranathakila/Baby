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
public class CHuman extends AbstractSLBCallbacks implements CHumanLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceLocal_;

    public CHuman() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", CHuman.class, this.hashCode());
    }

    final static Logger logger = LoggerFactory.getLogger(CHuman.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void doCHuman(final Human newUser) {
        crudServiceLocal_.create(newUser);
    }
}
