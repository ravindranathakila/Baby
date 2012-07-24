package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.util.Loggers;
import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/5/12
 * Time: 8:24 PM
 */
@WARNING("EXCEPTIONS IN CALLBACK METHODS MIGHT NOT GET LOGGED UNLESS EXPLICITLY CAUGHT WITHIN AND LOGGED.")
public class NSHuman implements NSEntityLifecycleCallbacks<Object> {
    final Boolean DEBUG_ENABLED = Loggers.DEBUG.isDebugEnabled();

    /**
     * Lazily initialized
     */
    @WARNING("Lazily initialized upon entity lifecycle callbacks")
    private static HazelcastClient hazelcastClient;

    public NSHuman() {
        //Better not do anything here since this is also called during build time enhancement
    }

    private HazelcastClient getHCClient() {
        if (hazelcastClient == null) {
            ClientConfig clientConfig = new ClientConfig();

            clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
            clientConfig.addAddress("127.0.0.1:5701");

            if (DEBUG_ENABLED) {
                Loggers.debug("THESE ARE THE HAZELCAST ADDRESSES FYR:");
                for (final InetSocketAddress inetSocketAddress : clientConfig.getAddressList()) {
                    Loggers.debug("Host " + inetSocketAddress.getHostName() + " on Port " + inetSocketAddress.getPort());
                }
            }

            hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);

            if (DEBUG_ENABLED) {
                Loggers.debug("THESE ARE THE HAZELCAST CLIENT(ME) DETAILS FYR:");
                Loggers.debug("clientConfig.getGroupConfig().getName():");
                Loggers.debug(clientConfig.getGroupConfig().getName());
                Loggers.debug("clientConfig.getGroupConfig().toString():");
                Loggers.debug(clientConfig.getGroupConfig().toString());
            }
        } else {
            if (!hazelcastClient.isActive()) {
                Loggers.info(Loggers.CODE_HC + "Hazelcast client is shutdown. Restarting...");
                hazelcastClient.restart();
                Loggers.info(Loggers.CODE_HC + "Hazelcast client start state after attempted restart:" + hazelcastClient.isActive());
            }
        }

        return hazelcastClient;
    }

    @Override
    @PrePersist
    public void create(final Object entity) {

        try {
            Object id = getId(entity);

            IMap<Object, Object> hcMap = getHCMap(entity);

            hcMap.lock(id);
            hcMap.put(id, entity);
            hcMap.unlock(id);

        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }

    @Override
    @PostLoad
    public void read(final Object entity) {


        try {
            //final Human nsHuman = getHCMap(entity).get(object.getHumanId());
            //compare and update here
        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }

    @Override
    @PostUpdate
    public void update(final Object entity) {

        try {

            Object key = getId(entity);

            IMap<Object, Object> hcMap = getHCMap(entity);

            hcMap.lock(key);
            hcMap.put(key, entity);
            hcMap.unlock(key);

        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }

    @Override
    @PostRemove
    public void remove(final Object entity) {

        try {
            Object key = getId(entity);

            IMap<Object, Object> hcMap = getHCMap(entity);

            hcMap.lock(key);
            hcMap.remove(key);
            hcMap.unlock(key);

        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }


    private Object getId(Object entity) throws IllegalAccessException, InvocationTargetException {
        Object key = null;

        final Method[] methods = entity.getClass().getMethods();

        for (final Method method : methods) {
            final Id annotation = method.getAnnotation(Id.class);
            if (annotation != null) {
                key = method.invoke(entity);
            }
        }

        if (DEBUG_ENABLED) {
            Loggers.debug(Loggers.CODE_HC + "Entity's @Id:" + key);
        }

        return key;
    }

    private IMap<Object, Object> getHCMap(final Object entity) {
        final String mapName = entity.getClass().getName();
        if (DEBUG_ENABLED) {
            Loggers.debug(Loggers.CODE_HC + "Attempting to fetch map named from:" + mapName);
        }
        return getHCClient().getMap(mapName);
    }
}
