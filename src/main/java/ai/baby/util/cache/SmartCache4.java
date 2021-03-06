package ai.baby.util.cache;


import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

/**
 * This implementation goes with Hazelcast.
 * We don't actually need this intermediary.
 * However, to isolate the caching mechanism, let's have this in place.
 * For example, this makes it possible for us to change to EHCache later if we'd like.
 * <p/>
 * We use EHCache for L2 caching in OpenJPA.
 * Hazelcast seems to be the easy and wise way to go with for distributed fast storage and retrieval.
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 5/5/12
 * Time: 12:10 PM
 */
public class SmartCache4<K, V, R> {

    private static final IllegalAccessError ILLEGAL_ACCESS_ERROR = new IllegalAccessError("RECOVERY MECHANISM NOT SPECIFIED!");
    public static final String ATTEMPT_TO_RECOVER_VALUE_FOR_KEY_FAILED_THE_KEY_IN_CONCERN = "ATTEMPT TO RECOVER VALUE FOR KEY FAILED. THE KEY IN CONCERN:";

    final public Map<K, V> MAP;

    private RecoverWith<K, V, R> recoverWith = null;

    public SmartCache4() {
        INITIALIZE_HAZELCAST:
        {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
            clientConfig.addAddress("127.0.0.1:5701");

            HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
            MAP = Hazelcast.getMap("SmartCache");
        }
    }

    public SmartCache4(final RecoverWith<K, V, R> recoverWith) {
        INITIALIZE_HAZELCAST:
        {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
            clientConfig.addAddress("127.0.0.1:5701");

            HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
            MAP = Hazelcast.getMap("SmartCache");
        }

        this.recoverWith = recoverWith;
    }

    public V get(final K key, final R runtimeArgument) {
        if (recoverWith == null) {
            throw ILLEGAL_ACCESS_ERROR;
        }
        V returnVal = null;

        if (MAP.containsKey(key) && returnVal != null) {
            returnVal = MAP.get(key);
        } else {
            final V recoverWithValue = recoverWith.getValue(key, runtimeArgument);
            if (recoverWithValue != null) {
                MAP.put(key, recoverWithValue);
                returnVal = MAP.get(key);
            } else {
                returnVal = null;
            }
        }

        return returnVal;
    }

    public V get(final K key, final RecoverWith<K, V, R> recoverWith) {
        V returnVal = null;
        if (MAP.containsKey(key) && returnVal != null) {
            returnVal = MAP.get(key);
        } else {
            final V recoverWithValue = recoverWith.getValue(key, null);
            if (recoverWithValue != null) {
                MAP.put(key, recoverWithValue);
                returnVal = MAP.get(key);
            } else {
                returnVal = null;
            }
        }

        return returnVal;
    }

    public void invalidate(final K key) {
        MAP.remove(key);
    }

    public interface RecoverWith<K, V, R> {
        public V getValue(final K k, final R runtimeArgument);
    }
}

