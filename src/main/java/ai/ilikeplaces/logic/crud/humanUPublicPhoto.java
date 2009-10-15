package ai.ilikeplaces.logic.crud;

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
//@Stateless
//@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class humanUPublicPhoto extends AbstractSLBCallbacks {

    public humanUPublicPhoto() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", humanUPublicPhoto.class, this.hashCode());
    }
    final static Logger logger = LoggerFactory.getLogger(humanUPublicPhoto.class);
}
