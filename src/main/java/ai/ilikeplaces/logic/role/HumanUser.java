package ai.ilikeplaces.logic.role;

import ai.doc.License;
import ai.doc.WARNING;
import ai.doc._fix;
import ai.doc._ok;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.cache.SmartCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSessionBindingEvent;
import java.io.Serializable;
import java.util.*;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateful
public class HumanUser extends AbstractSFBCallbacks implements HumanUserLocal, ManageObservers, Serializable {

    public static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("SORRY! THE CODE REFLECTING THIS CALL SHOULD ONLY WORK IF THE USER IS LOGGED IN, BUT ACTUALLY IS NOT.");
    private String humanUserId_ = null;
    @_fix(issue = "transient",
            issues = {"is marking this transient consistent?",
                    "Will this field make the session variable huge? There could be millions of users!"})
    @WARNING(warning = "DO NOT MAKE TRANSIENT AS PASSIVATION MAKES THE VALUE OF THIS NULL.")
    private DelegatedObservable delegatedObservable;

    /**
     * DO NOT USE CACHE DIRECTLY. IT MIGHT BE JUST OUT OF DE-SERIALIZATION AND BE NULL. Use {@link #getCache()} TO DO THIS.
     * DO NOT REMOVE transient SINCE THIS IS A CACHE, AND WILL GET VERY BULKY.
     */
    private transient SmartCache<String, Object> cache;

    private Map<String, Object> store = new HashMap<String, Object>(0);


    final static private Properties P_ = new Properties();
    static private Context Context_ = null;
    static private boolean OK_ = false;
    final static private ResourceBundle exceptionMsgs = ResourceBundle.getBundle("ai.ilikeplaces.rbs.ExceptionMsgs");
    final static private ResourceBundle config = ResourceBundle.getBundle("ai.ilikeplaces.rbs.Config");
    final static private String ICF = config.getString("oejb.LICF");
    final static Logger logger = LoggerFactory.getLogger(DB.class);

    public static final String NAMING_EXCEPTION = "SORRY! I ENCOUNTERED AN NAMING EXCEPTION WHILE DOING A CONTEXT OPERATION.";

    static {
        try {
            P_.put(Context.INITIAL_CONTEXT_FACTORY, ICF);
            Context_ = new InitialContext(P_);
            OK_ = true;
        } catch (NamingException ex) {
            OK_ = false;
            logger.error(NAMING_EXCEPTION, ex);
        }
    }

    static public void isOK() {
        if (!OK_) {
            throw new IllegalStateException(exceptionMsgs.getString("ai.ilikeplaces.static_initialization_failure"));
        }
    }


    @Override
    public HumanUserLocal getHumanUserLocal() {
        isOK();
        HumanUserLocal h = null;
        try {
            h = (HumanUserLocal) Context_.lookup(HumanUserLocal.NAME);
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanUserLocal) LogNull.logThrow();

    }

    public static HumanUserLocal getHumanUserLocal(final boolean nonInjected) {
        HumanUserLocal h = null;
        try {
            h = ((HumanUserLocal) Context_.lookup(HumanUser.NAME)).getHumanUserLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanUserLocal) LogNull.logThrow();

    }

    /**
     * Get the HumanId of the Logged in user, or throw exception.
     * This is a call by prevention where the calls will be made to this method after one
     * validation that the user is logged in. This is the safe approach to code that assumes logged in.
     * When code gets bulky, at times calls to just getUserName might trigger null if not used in this form.
     * With this approach, we expect to throw an exception immediately instead of late discovery.
     *
     * @param sessionBoundBadRefWrapper
     * @return The HumanUserLocal of the Logged in user or throws a RuntimeException if not alive or null
     */
    static public HumanUserLocal getHumanUserAsValid(final SessionBoundBadRefWrapper<HumanUserLocal> sessionBoundBadRefWrapper) {
        if (sessionBoundBadRefWrapper == null) {
            throw ILLEGAL_STATE_EXCEPTION;
        } else if (!sessionBoundBadRefWrapper.isAlive()) {//(Defensive)This is checked in the constructor of this class
            throw ILLEGAL_STATE_EXCEPTION;
        }
        return sessionBoundBadRefWrapper.getBoundInstance();
    }

    /**
     * @return
     */
    @Override
    public String getHumanUserId() {
        return humanUserId_;
    }

    public HumanId getHumanId() {
        return (HumanId) new HumanId().setObjAsValid(getHumanUserId());
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
    @_ok(details = "Properly being called by container according to logs as of 20100323")
    @PrePassivate
    @Override
    public void prePassivate() {
        Loggers.USER.info(humanUserId_ + " disked out");
        Loggers.DEBUG.debug(humanUserId_ + " disked out");
    }

    /**
     *
     */
    @PostActivate
    @Override
    public void postActivate() {
        Loggers.USER.info(humanUserId_ + " disked in");
        Loggers.DEBUG.debug(humanUserId_ + " disked in");
    }

    @PreDestroy
    @Override
    public void preDestroy() {
        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, humanUserId_ + " is destroyed. Notifying observers:", 60000, null, true);
        try {
            Loggers.USER.info(humanUserId_ + " is destroyed");
            Loggers.DEBUG.debug(humanUserId_ + " is destroyed");
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
    @_ok(details = "According to logs, this is being called upon binding as of 20100323")
    @Override
    public void valueBound(final HttpSessionBindingEvent event) {
        Loggers.DEBUG.debug((RBGet.logMsgs.getString("javax.servlet.http.HttpSessionBindingEvent.valuBound")));
    }

    /**
     * @param event
     */
    @_ok(details = "According to logs, this is being called upon unbinding as of 20100323")
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

    /**
     * DO NOT USE CACHE DIRECTLY. IT MIGHT BE JUST OUT OF DE-SERIALIZATION AND BE NULL;
     *
     * @return
     */
    private SmartCache<String, Object> getCache() {
        return cache == null ? cache = new SmartCache<String, Object>() : cache;
    }

    /**
     * This is Approach One To Cache: Key is an unknown constant
     * <p/>
     * Approach Two To Cache: {@link #cacheAndUpdateWith(ai.ilikeplaces.logic.role.HumanUserLocal.CACHE_KEY, Object)}
     *
     * @param key
     * @param recoverWith
     * @return the cached or recovered value
     * @see {@link #cacheAndUpdateWith(ai.ilikeplaces.logic.role.HumanUserLocal.CACHE_KEY, Object)}
     */
    public Object cache(final String key, final SmartCache.RecoverWith<String, Object> recoverWith) {
        return getCache().get(new String(key), recoverWith);//DO NOT REMOVE NEW STRING, WILL NOT ALLOW GC AS A WEAK REFERENCE IF SO
    }


    /**
     * This is Approach Two To Cache: Key is a known constant
     * <p/>
     * Approach One To Cache: {@link #cache(String, ai.ilikeplaces.util.cache.SmartCache.RecoverWith)}
     * <p/>
     * Updates the cache(if value is not null) and returns the old value.
     * Returns value if value is null
     *
     * @param key
     * @param valueToUpdateWith
     * @return old value
     *         <p/>
     * @see {@link #cache(String, ai.ilikeplaces.util.cache.SmartCache.RecoverWith)}
     */
    public Object cacheAndUpdateWith(final CACHE_KEY key, final Object valueToUpdateWith) {
        return valueToUpdateWith == null ? getCache().MAP.get(key.name()) : getCache().MAP.put(new String(key.name()), valueToUpdateWith);
    }

    /**
     * Updates the cache(if value is not null) and returns the old value.
     * Returns value if value is null
     *
     * @param key
     * @param valueToUpdateWith
     * @return old value
     *         <p/>
     * @see {@link #cache(String, ai.ilikeplaces.util.cache.SmartCache.RecoverWith)}
     */
    public Object storeAndUpdateWith(final STORE_KEY key, final Object valueToUpdateWith) {
        return valueToUpdateWith == null ? store.get(key.name()) : store.put(key.name(), valueToUpdateWith);
    }
}
