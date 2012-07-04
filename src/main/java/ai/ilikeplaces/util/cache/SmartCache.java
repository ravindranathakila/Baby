package ai.ilikeplaces.util.cache;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/9/11
 * Time: 10:45 AM
 */
public class SmartCache<K, V> {

    private static final IllegalAccessError ILLEGAL_ACCESS_ERROR = new IllegalAccessError("RECOVERY MECHANISM NOT SPECIFIED!");
    public Map<K, V> MAP = new WeakHashMap<K, V>();

    private RecoverWith<K, V> recoverWith = null;

    public SmartCache() {
    }

    public SmartCache(final RecoverWith<K, V> recoverWith) {
        this.recoverWith = recoverWith;
    }

    public V get(final K key) {
        if(recoverWith == null){
            throw ILLEGAL_ACCESS_ERROR;
        }
        if (!(MAP.containsKey(key) && (MAP.get(key)) != null)) {
            MAP.put(key, recoverWith.getValue(key));
        }

        return MAP.get(key);
    }

    public V get(final K key, final RecoverWith<K, V> recoverWith) {
        if (!(MAP.containsKey(key) && (MAP.get(key)) != null)) {
            MAP.put(key, recoverWith.getValue(key));
        }

        return MAP.get(key);
    }

    public interface RecoverWith<K, V> {
        public V getValue(final K k);
    }
}
