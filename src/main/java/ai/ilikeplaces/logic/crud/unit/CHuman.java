package ai.ilikeplaces.logic.crud.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CHuman extends AbstractSLBCallbacks implements CHumanLocal {

    public CHuman() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", CHuman.class, this.hashCode());
    }
    final static Logger logger = LoggerFactory.getLogger(CHuman.class);
}
