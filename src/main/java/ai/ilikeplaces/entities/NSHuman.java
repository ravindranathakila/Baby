package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.util.Loggers;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

import javax.persistence.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/5/12
 * Time: 8:24 PM
 */
@WARNING("EXCEPTIONS IN CALLBACK METHODS MIGHT NOT GET LOGGED UNLESS EXPLICITLY CAUGHT WITHIN AND LOGGED.")
public class NSHuman implements NSEntityLifecycleCallbacks<Human> {
    final Boolean DEBUG_ENABLED = Loggers.DEBUG.isDebugEnabled();

    @Override
    @PrePersist
    public void create(final Human human) {
        try {

            ClientConfig clientConfig = new ClientConfig();

            clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
            clientConfig.addAddress("127.0.0.1:5701");

            if (DEBUG_ENABLED) {
                Loggers.debug("THESE ARE THE HAZELCAST ADDRESSES FYR:");
                for (final InetSocketAddress inetSocketAddress : clientConfig.getAddressList()) {
                    Loggers.debug("Host " + inetSocketAddress.getHostName() + " on Port " + inetSocketAddress.getPort());
                }
            }

            HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

            if (DEBUG_ENABLED) {
                Loggers.debug("THESE ARE THE HAZELCAST CLIENT(ME) DETAILS FYR:");
                Loggers.debug("clientConfig.getGroupConfig().getName():");
                Loggers.debug(clientConfig.getGroupConfig().getName());
                Loggers.debug("clientConfig.getGroupConfig().toString():");
                Loggers.debug(clientConfig.getGroupConfig().toString());
            }

            final IMap<Object, Object> map = client.getMap(Human.class.getName());
            map.put(human.getHumanId(), human);
        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }

    @Override
    @PostLoad
    public void read(final Human human) {
        try {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");

            //Please gravely note that the address:port here is the one mentioned in the hazelcast.xml of the group
            clientConfig.addAddress("127.0.0.1:5701");

            HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
            final IMap<String, Human> map = client.getMap(Human.class.getName());
            final Human nsHuman = map.get(human.getHumanId());
            //compare and update here
        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }

    @Override
    @PostUpdate
    public void update(final Human human) {
        try {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
            clientConfig.addAddress("127.0.0.1", "127.0.0.1:5702");

            HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
            final IMap<Object, Object> map = client.getMap(Human.class.getName());
            map.put(human.getHumanId(), human);
        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }

    @Override
    @PostRemove
    public void remove(final Human human) {
        try {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
            clientConfig.addAddress("127.0.0.1", "127.0.0.1:5702");

            HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
            final IMap<Object, Object> map = client.getMap(Human.class.getName());
            map.remove(human.getHumanId());
        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }
}
