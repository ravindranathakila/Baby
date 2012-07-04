package ai.ilikeplaces.util.cache;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/9/11
 * Time: 10:45 AM
 */
public class SmartCache2String<V, R> {

    private static final IllegalAccessError ILLEGAL_ACCESS_ERROR = new IllegalAccessError("RECOVERY MECHANISM NOT SPECIFIED!");
    public Map<String, V> MAP = new WeakHashMap<String, V>();

    private RecoverWith<V, R> recoverWith = null;

    public SmartCache2String() {
    }

    public SmartCache2String(final RecoverWith<V, R> recoverWith) {
        this.recoverWith = recoverWith;
    }

    public V get(final String key, final R runtimeArgument) {
        if (recoverWith == null) {
            throw ILLEGAL_ACCESS_ERROR;
        }
        if (!(MAP.containsKey(key) && (MAP.get(key)) != null)) {
            MAP.put(new String(key), recoverWith.getValue(key, runtimeArgument));
        }

        return MAP.get(key);
    }

    public V get(final String key, final RecoverWith<V, R> recoverWith) {
        if (!(MAP.containsKey(key) && (MAP.get(key)) != null)) {
            MAP.put(new String(key), recoverWith.getValue(key, null));
        }

        return MAP.get(key);
    }


    public void invalidate(final String key) {
        MAP.remove(key);
    }

    public interface RecoverWith<V, R> {
        public V getValue(final String k, final R runtimeArgument);
    }
}
