package ai.ilikeplaces.logic.role;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.AbstractSFBCallbacks;
import ai.ilikeplaces.util.DelegatedObservable;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.ManageObservers;

import javax.annotation.PreDestroy;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.Observer;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateful
final public class HumanUser extends AbstractSFBCallbacks implements HumanUserLocal, ManageObservers {

    private String humanUserId_ = null;
    @FIXME(issue = "transent",
            issues = {"is marking this transient consistent?",
                    "Will this field make the session variable huge? There could be millions of users!"})
    final private transient DelegatedObservable delegatedObservable;

    public HumanUser() {
        this.delegatedObservable = new DelegatedObservable();
    }

    /**
     * @return
     */
    @Override
    public String getHumanUserId() {
        return humanUserId_;
    }

    /**
     * @param humanUserId__
     * @throws IllegalStateException
     */
    @Override
    public void setHumanUserId(final String humanUserId__) throws IllegalStateException {
        if (this.humanUserId_ == null) {
            this.humanUserId_ = humanUserId__;
            logger.debug(RBGet.logMsgs.getString("ai.ilikeplaces.logic.role.HumanUser.0001"),humanUserId__);
        } else {
            this.remove();
            throw new IllegalStateException(
                    "SORRY! YOU ATTEMPT TO RE-ASSIGN USER ID WHICH IS NOT ALLOWED. PLEASE MAKE THE USER SIGN OUT AND SIGN IN INSTEAD!" +
                            "\n EXISTING ID:" + this.humanUserId_ +
                            "\n ID YOU ASKED ME TO ASSIGN:" + humanUserId__ +
                            "\n ALSO, AS A PRECAUTION, I AM REMOVING THIS STATEFUL SESSION BEAN!");
        }
    }

    /**
     *
     */
    @Remove
    @Override
    public void remove() {
        Loggers.USER.info(humanUserId_+ " logged out");
        logger.info(RBGet.logMsgs.getString("javax.ejb.Remove"));
    }

    @PreDestroy
    @Override
    public void preDestroy() {
        this.delegatedObservable.setChanged();
        delegatedObservable.notifyObservers(true);
    }

    /**
     * @param event
     */
    @Override
    public void valueBound(final HttpSessionBindingEvent event) {
        logger.info(RBGet.logMsgs.getString("javax.servlet.http.HttpSessionBindingEvent.valuBound"));
    }

    /**
     * @param event
     */
    @Override
    public void valueUnbound(final HttpSessionBindingEvent event) {
        logger.info(RBGet.logMsgs.getString("javax.servlet.http.HttpSessionBindingEvent.valueUnbound"));
        this.remove();
    }

    /**
     * @param o
     */
    @Override
    public void addObserver(Observer o) {
        this.delegatedObservable.addObserver(o);
    }

    /**
     * @param o
     */
    @Override
    public void deleteObserver(Observer o) {
        this.delegatedObservable.deleteObserver(o);
    }
}
