package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.util.Loggers;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

import javax.persistence.*;
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
    public void postPersist(final Human human) {
        try {
            final IMap<Object, Object> map = Hazelcast.getMap(Human.class.getName());
            map.put(human.getHumanId(), human);
        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }

    @Override
    @PostLoad
    public void postLoad(final Human human) {
        try {
            final IMap<String, Human> map = Hazelcast.getMap(Human.class.getName());
            final Human nsHuman = map.get(human.getHumanId());
            //compare and update here
        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }

    @Override
    @PreUpdate
    public void postUpdate(final Human human) {
        try {
            final IMap<Object, Object> map = Hazelcast.getMap(Human.class.getName());
            map.put(human.getHumanId(), human);
        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }

    @Override
    @PostRemove
    public void postRemove(final Human human) {
        try {
            final IMap<Object, Object> map = Hazelcast.getMap(Human.class.getName());
            map.remove(human.getHumanId());
        } catch (final Throwable t) {
            Loggers.error("HAZELCAST: ERROR PERSISTING DATA VIA HAZELCAST", t);
        }
    }
}
