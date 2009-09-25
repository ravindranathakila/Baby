package ai.ilikeplaces;

import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;

/**
 *
 * @author Ravindranath Akila
 */
public class AbstractSFBCallbacks extends AbstractSBCallbacks {

    final static private String MsgPassivate = "HELLO, I JUST PASSIVATED AN INSTANCE OF ";
    final static private String MsgActivate = "HELLO, I JUST ACTIVATED AN INSTANCE OF ";
    final static private String MsgRemove = "HELLO, I JUST REMOVED AN INSTANCE OF ";

    /**
     *
     */
    @PrePassivate
    public void prePassivate() {
        logger.info(MsgPassivate + className + this.hashCode());
    }

    /**
     *
     */
    @PostActivate
    public void postActivate() {
        logger.info(MsgActivate + className + this.hashCode());
    }

    /**
     *
     */
    @Remove
    public void remove() {
        logger.info(MsgRemove + className + this.hashCode());
    }
}
