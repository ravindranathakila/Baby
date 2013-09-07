package ai.baby.util;

import ai.scribble.License;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class AbstractSBCallbacks {

    /**
     *
     */
    final static protected Logger INFO = Loggers.INFO;
    public static final boolean DEBUG_ENABLED = Loggers.DEBUG.isDebugEnabled();
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
        if (DEBUG_ENABLED) {
            Loggers.debug(MsgConstruct + className + this.hashCode());
        }
    }

    /**
     *
     */
    @PreDestroy
    public void preDestroy() {
        if (Loggers.DEBUG.isDebugEnabled()) {
            Loggers.debug(MsgDestroy + className + this.hashCode());
        }
    }
}
