package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansNet;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.SimpleString;
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
public class UHumansNet extends AbstractSLBCallbacks implements UHumansNetLocal {

    @EJB
    private CrudServiceLocal<HumansNet> humansNetCrudServiceLocal_;

    public UHumansNet() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", UHumansNet.class, this.hashCode());
    }

    final static Logger logger = LoggerFactory.getLogger(UHumansNet.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void doNTxUDisplayName(final HumanId humanId, String displayName) {
        humansNetCrudServiceLocal_.getReference(HumansNet.class, humanId.getObj()).setDisplayName(displayName);
    }

}