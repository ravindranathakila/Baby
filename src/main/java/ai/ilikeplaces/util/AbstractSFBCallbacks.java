package ai.ilikeplaces.util;

import ai.scribble.License;

import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class AbstractSFBCallbacks extends AbstractSBCallbacks {

    final static private String MsgPassivate = "HELLO, I JUST PASSIVATED AN INSTANCE OF ";
    final static private String MsgActivate = "HELLO, I JUST ACTIVATED AN INSTANCE OF ";
    final static private String MsgRemove = "HELLO, I JUST REMOVED AN INSTANCE OF ";
    public static final boolean DEBUG_ENABLED = Loggers.DEBUG.isDebugEnabled();

    /**
     *
     */
    @PrePassivate
    public void prePassivate() {
        if (DEBUG_ENABLED) {
            Loggers.debug(MsgPassivate + className + this.hashCode());
        }
    }

    /**
     *
     */
    @PostActivate
    public void postActivate() {
        if (Loggers.DEBUG.isDebugEnabled()) {
            Loggers.debug(MsgActivate + className + this.hashCode());
        }
    }

    /**
     *
     */
    @Remove
    public void remove() {
        if (Loggers.DEBUG.isDebugEnabled()) {
            Loggers.debug(MsgRemove + className + this.hashCode());
        }
    }
}
