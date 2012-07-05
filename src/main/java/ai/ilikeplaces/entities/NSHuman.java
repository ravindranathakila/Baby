package ai.ilikeplaces.entities;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/5/12
 * Time: 8:24 PM
 */
public class NSHuman implements NSEntityLifecycleCallbacks<Human> {
    @Override
    @PostPersist
    public void postPersist(final Human human) {
        final IMap<Object, Object> map = Hazelcast.getMap(Human.class.getName());
        map.put(human.getHumanId(), human);
    }

    @Override
    @PostLoad
    public void postLoad(final Human human) {
        final IMap<String, Human> map = Hazelcast.getMap(Human.class.getName());
        final Human nsHuman = map.get(human.getHumanId());
        //compare and update here
    }

    @Override
    @PostUpdate
    public void postUpdate(final Human human) {
        final IMap<Object, Object> map = Hazelcast.getMap(Human.class.getName());
        map.put(human.getHumanId(), human);
    }

    @Override
    @PostRemove
    public void postRemove(final Human human) {
        final IMap<Object, Object> map = Hazelcast.getMap(Human.class.getName());
        map.remove(human.getHumanId());
    }
}
