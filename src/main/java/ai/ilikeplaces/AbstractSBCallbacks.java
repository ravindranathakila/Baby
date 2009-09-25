package ai.ilikeplaces;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 * @author Ravindranath Akila
 */
public class AbstractSBCallbacks {

    /**
     *
     */
    final protected Logger logger = Logger.getLogger(this.getClass().getName());
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
        logger.info(MsgConstruct + className + this.hashCode());
    }

    /**
     *
     */
    @PreDestroy
    public void preDestroy() {
        logger.info(MsgDestroy + className + this.hashCode());
    }
}
