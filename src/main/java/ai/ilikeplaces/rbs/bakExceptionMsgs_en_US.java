package ai.ilikeplaces.rbs;

import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import java.util.ResourceBundle;
/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class bakExceptionMsgs_en_US extends ResourceBundle {

    public bakExceptionMsgs_en_US() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.",bakExceptionMsgs_en_US.class,this.hashCode());
    }

    final static Logger logger = LoggerFactory.getLogger(bakExceptionMsgs_en_US.class);

    @Override
    protected Object handleGetObject(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration<String> getKeys() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
