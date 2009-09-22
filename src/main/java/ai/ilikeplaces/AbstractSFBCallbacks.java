package ai.ilikeplaces;

import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;

/**
 *
 * @author Ravindranath Akila
 */
public class AbstractSFBCallbacks extends AbstractSBCallbacks {

    final static private String MsgPassivate = "HELLO, I JUST PASSIVATED ";
    final static private String MsgActivate = "HELLO, I JUST ACTIVATED ";

    @PrePassivate
    public void prePassivate() {
        logger.info(MsgPassivate + className);
    }

    @PostActivate
    public void postActivate() {
        logger.info(MsgActivate + className);
    }
}
