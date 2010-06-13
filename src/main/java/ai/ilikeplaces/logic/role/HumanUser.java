package ai.ilikeplaces.logic.role;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.Observer;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateful
public class HumanUser extends AbstractSFBCallbacks implements HumanUserLocal, ManageObservers {

    private String humanUserId_ = null;
    @FIXME(issue = "transient",
            issues = {"is marking this transient consistent?",
                    "Will this field make the session variable huge? There could be millions of users!"})
    @WARNING(warning = "DO NOT MAKE TRANSIENT AS PASSIVATION MAKES THE VALUE OF THIS NULL.")
    private DelegatedObservable delegatedObservable;

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
    public HumanUserLocal setHumanUserId(final String humanUserId__) throws IllegalStateException {
        if (humanUserId_ == null) {
            humanUserId_ = humanUserId__;
            INFO.debug(RBGet.logMsgs.getString("ai.ilikeplaces.logic.role.HumanUser.0001"), humanUserId__);
            Loggers.USER.info(humanUserId_ + " logged in");
        } else {
            remove();
            throw new IllegalStateException(
                    "SORRY! YOU ATTEMPT TO RE-ASSIGN USER ID WHICH IS NOT ALLOWED. PLEASE MAKE THE USER SIGN OUT AND SIGN IN INSTEAD!" +
                            "\n EXISTING ID:" + humanUserId_ +
                            "\n ID YOU ASKED ME TO ASSIGN:" + humanUserId__ +
                            "\n ALSO, AS A PRECAUTION, I AM REMOVING THIS STATEFUL SESSION BEAN!");
        }
        return this;
    }

    /**
     *
     */
    @PostConstruct
    @Override
    public void postConstruct() {
        delegatedObservable = new DelegatedObservable();
    }

    /**
     *
     */
    @Remove
    @Override
    public void remove() {
        Loggers.USER.info(humanUserId_ + " logged out");
        INFO.info(RBGet.logMsgs.getString("javax.ejb.Remove"));
    }

    /**
     *
     */
    @OK(details = "Properly being called by container according to logs as of 20100323")
    @PrePassivate
    @Override
    public void prePassivate() {
        Loggers.USER.info(humanUserId_ + " disked out");
        Loggers.DEBUG.info(humanUserId_ + " disked out");
    }

    /**
     *
     */
    @PostActivate
    @Override
    public void postActivate() {
        Loggers.USER.info(humanUserId_ + " disked in");
        Loggers.DEBUG.info(humanUserId_ + " disked in");
    }

    @PreDestroy
    @Override
    public void preDestroy() {
        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, humanUserId_ + " is destroyed. Notifying observers:", 60000, null, true);
        try {
            Loggers.USER.info(humanUserId_ + " is destroyed");
            Loggers.DEBUG.info(humanUserId_ + " is destroyed");
            delegatedObservable.setChanged();
            delegatedObservable.notifyObservers(true);
            sl.multiComplete(new Loggers.LEVEL[]{Loggers.LEVEL.DEBUG, Loggers.LEVEL.USER}, Loggers.DONE);
        } catch (final Throwable t) {
            sl.multiComplete(new Loggers.LEVEL[]{Loggers.LEVEL.ERROR, Loggers.LEVEL.USER}, t);
        }
    }

    /**
     * @param event
     */
    @OK(details = "According to logs, this is being called upon binding as of 20100323")
    @Override
    public void valueBound(final HttpSessionBindingEvent event) {
        Loggers.DEBUG.debug((RBGet.logMsgs.getString("javax.servlet.http.HttpSessionBindingEvent.valuBound")));
    }

    /**
     * @param event
     */
    @OK(details = "According to logs, this is being called upon unbinding as of 20100323")
    @Override
    public void valueUnbound(final HttpSessionBindingEvent event) {
        Loggers.DEBUG.debug(RBGet.logMsgs.getString("javax.servlet.http.HttpSessionBindingEvent.valueUnbound"));
        remove();
    }

    /**
     * @param o
     */
    @Override
    public void addObserver(Observer o) {
        delegatedObservable.addObserver(o);
    }

    /**
     * @param o
     */
    @Override
    public void deleteObserver(Observer o) {
        delegatedObservable.deleteObserver(o);
    }
}
