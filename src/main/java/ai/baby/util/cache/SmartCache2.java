package ai.baby.util.cache;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/9/11
 * Time: 10:45 AM
 */
public class SmartCache2<K, V, R> {

    private static final IllegalAccessError ILLEGAL_ACCESS_ERROR = new IllegalAccessError("RECOVERY MECHANISM NOT SPECIFIED!");
    public Map<K, V> MAP = new WeakHashMap<K, V>();

    private RecoverWith<K, V, R> recoverWith = null;

    public SmartCache2() {
    }

    public SmartCache2(final RecoverWith<K, V, R> recoverWith) {
        this.recoverWith = recoverWith;
    }

    public V get(final K key, final R runtimeArgument) {
        if (recoverWith == null) {
            throw ILLEGAL_ACCESS_ERROR;
        }
        V returnVal = null;

        if (MAP.containsKey(key) && returnVal != null) {
            //Yeah, don't mes with this if-else. Beats the logic crap out of you
        } else {
            MAP.put(key, recoverWith.getValue(key, runtimeArgument));
        }

        returnVal = MAP.get(key);

        return returnVal;
    }

    public V get(final K key, final RecoverWith<K, V, R> recoverWith) {
        V returnVal = MAP.get(key);

        if (MAP.containsKey(key) && returnVal != null) {
            //Yeah, don't mes with this if-else. Beats the logic crap out of you
        } else {
            MAP.put(key, recoverWith.getValue(key, null));
        }

        returnVal = MAP.get(key);

        return returnVal;
    }

    public void invalidate(final K key) {
        MAP.remove(key);
    }

    public interface RecoverWith<K, V, R> {
        public V getValue(final K k, final R runtimeArgument);
    }
}
