package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class AbstractSBCallbacks {

    /**
     *
     */
    final static protected Logger INFO = Loggers.INFO;
    /**
     *
     */
    final protected String className = this.getClass().getName();
    final static private String MsgConstruct = "HELLO, I JUST CONSTRUCTED AN INSTANCE OF ";
    final static private String MsgDestroy = "HELLO, I JUST DESTROYED AN INSTANCE OF ";

    /**
     *
     */
    @PostConstruct
    public void postConstruct() {
        INFO.info(MsgConstruct + className + this.hashCode());
    }

    /**
     *
     */
    @PreDestroy
    public void preDestroy() {
        INFO.info(MsgDestroy + className + this.hashCode());
    }
}
