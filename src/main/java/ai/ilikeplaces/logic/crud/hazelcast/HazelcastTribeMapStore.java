package ai.ilikeplaces.logic.crud.hazelcast;

import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.util.cache.AbstractHCMapStore;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/3/12
 * Time: 10:40 PM
 */
public class HazelcastTribeMapStore extends AbstractHCMapStore {

    private Class<Tribe> type = Tribe.class;
    ;

    @Override
    public void store(Object o, Object o1) {

        super.store(o, o1, type);
    }

    @Override
    public void delete(Object o) {
        super.delete(o, type);
    }


    @Override
    public Object load(Object o) {
        return super.load(o, type);
    }


}
