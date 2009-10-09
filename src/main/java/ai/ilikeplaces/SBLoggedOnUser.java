package ai.ilikeplaces;

import ai.ilikeplaces.util.AbstractSFBCallbacks;
import ai.ilikeplaces.doc.*;
import javax.ejb.Stateful;
import javax.ejb.Remove;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.Observer;
import javax.annotation.PreDestroy;

/**
 *
 * @author Ravindranath Akila
 */
@Stateful
final public class SBLoggedOnUser extends AbstractSFBCallbacks implements SBLoggedOnUserFace, ManageObservers {

    private String loggedOnUserId;
    @FIXME(issue = "transent", 
    issues = {"is marking this transient consistant?",
        "Will this field make the session variable huge? There could be millions of users!"})
    final private transient DelegatedObservable delegatedObservable;

    public SBLoggedOnUser() {
        this.delegatedObservable = new DelegatedObservable();
    }

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
        logger.info("HELLO, REMOVING BEAN");
    }

    @PreDestroy
    public void preDestroy() {
        this.delegatedObservable.setChanged();
        delegatedObservable.notifyObservers(true);
    }

    /**
     *
     * @param event
     */
    @Override
    public void valueBound(final HttpSessionBindingEvent event) {
        logger.info("HELLO, BINDING BEAN TO SESSION.");
    }

    /**
     *
     * @param event
     */
    @Override
    public void valueUnbound(final HttpSessionBindingEvent event) {
        logger.info("HELLO! UNBINDING BEAN FROM SESSION AND MARKING AS REMOVE.");
        this.remove();
    }

    /**
     *
     * @param o
     */
    @Override
    public void addObserver(Observer o) {
        this.delegatedObservable.addObserver(o);
    }

    /**
     *
     * @param o
     */
    @Override
    public void deleteObserver(Observer o) {
        this.delegatedObservable.deleteObserver(o);
    }
}
