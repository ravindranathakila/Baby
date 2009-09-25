package ai.ilikeplaces;

import javax.ejb.Remove;

/**
 *
 * @author Ravindranath Akila
 */
public class AbstractSLBCallbacks extends AbstractSBCallbacks {

    final static private String MsgRemove = "HELLO, I JUST REMOVED AN INSTANCE OF ";

    /**
     *
     */
    @Remove
    public void remove() {
        logger.info(MsgRemove + className + this.hashCode());
    }
}
