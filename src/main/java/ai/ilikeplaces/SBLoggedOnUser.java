package ai.ilikeplaces;

import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.ejb.Remove;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 *
 * @author Ravindranath Akila
 */
@Stateful
public class SBLoggedOnUser extends AbstractSFBCallbacks implements SBLoggedOnUserFace{

    private  String loggedOnUserId;

    /**
     *
     * @return
     */
    @Override
    public String getLoggedOnUserId() {
        return loggedOnUserId;
    }

    /**
     *
     * @param loggedOnUserId__
     * @throws IllegalStateException
     */
    @Override
    public void setLoggedOnUserId(final String loggedOnUserId__) throws IllegalStateException {
        if (loggedOnUserId == null) {
            this.loggedOnUserId = loggedOnUserId__;
        } else {
            this.remove();
            throw new java.lang.IllegalStateException(
                    "YOU ATTEMPT TO RE-ASSIGN USER ID WHICH IS NOT ALLOWED. PLEASE MAKE THE USER SIGN OUT AND SIGN IN INSTREAD!" +
                    "\n EXISTING ID:" + loggedOnUserId +
                    "\n ID YOU ASKED ME TO ASSIGN:" + loggedOnUserId__ +
                    "\n ALSO, AS A PRECAUTION, I AM REMOVING THIS STATEFUL SESSION BEAN!");
        }
    }

    /**
     *
     */
    @Remove
    @Override
    public void remove() {
        logger.info("REMOVING BEAN");
    }

    /**
     *
     * @param event
     */
    @Override
    public void valueBound(final HttpSessionBindingEvent event) {
        logger.info("BINDING BEAN TO SESSION.");
    }

    /**
     *
     * @param event
     */
    @Override
    public void valueUnbound(final HttpSessionBindingEvent event) {
        logger.info("UNBINDING BEAN FROM SESSION AND MAKING AS REMOVE.");
        this.remove();
    }
}
