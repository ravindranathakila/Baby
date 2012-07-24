package ai.ilikeplaces.logic.crud.hazelcast;

import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.util.cache.AbstractHCMapStore;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/3/12
 * Time: 10:40 PM
 */
public class HazelcastHumansNetPeopleMapStore extends AbstractHCMapStore {

    private Class<HumansNetPeople> type = HumansNetPeople.class;
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
