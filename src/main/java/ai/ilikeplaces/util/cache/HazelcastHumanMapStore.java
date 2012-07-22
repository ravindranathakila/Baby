package ai.ilikeplaces.util.cache;

import ai.ilikeplaces.entities.Human;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapStore;
import com.hazelcast.core.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/3/12
 * Time: 10:40 PM
 */
public class HazelcastHumanMapStore extends AbstractHCMapStore {

    public HazelcastHumanMapStore() {
    }

    @Override
    public void delete(Object o) {
        super.delete(o, Human.class);
    }


    @Override
    public Object load(Object o) {
        return super.load(o, Human.class);
    }


}
