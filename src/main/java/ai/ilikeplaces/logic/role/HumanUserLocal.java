package ai.ilikeplaces.logic.role;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.DelegatedObservable;
import ai.ilikeplaces.util.cache.SmartCache;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanUserLocal extends HttpSessionBindingListener, Serializable {

    final static public String NAME = HumanUserLocal.class.getSimpleName();

    static public enum CACHE_KEY {
        BE_FRIENDS,
    }

    static public enum STORE_KEY {
        AUTOPLAY_STATE,
        USER_LOCATION_TYPE, USER_LOCATION_DETAILS, USER_LOCATION_PRIVATE_PHOTOS
    }

    public HumanUserLocal getHumanUserLocal();


    /**
     * @return
     */
    public String getHumanUserId();

    /**
     * @return
     */
    public HumanId getHumanId();

    /**
     * @param loggedOnUserId
     */
    public HumanUserLocal setHumanUserId(String loggedOnUserId);

    public Object cache(final String key, SmartCache.RecoverWith<String, Object> recoverWith);

    /**
     * Updates the cache and returns the old value
     *
     * @param key
     * @param valueToUpdateWith
     * @return old value
     */
    public Object cacheAndUpdateWith(final CACHE_KEY key, final Object valueToUpdateWith);


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
    public Object storeAndUpdateWith(final STORE_KEY key, final Object valueToUpdateWith);

}
