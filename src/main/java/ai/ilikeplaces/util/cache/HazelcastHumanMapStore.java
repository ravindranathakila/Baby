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
public class HazelcastHumanMapStore implements MapStore<String, Human> {
    @Override
    public void store(String s, Human human) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU_DN");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(human);
        em.getTransaction().commit();
    }

    @Override
    public void storeAll(Map<String, Human> stringHumanMap) {
        for (final String key : stringHumanMap.keySet()) {
            store(key, stringHumanMap.get(key));
        }
    }

    @Override
    public void delete(String s) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU_DN");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.find(Human.class, s));
        em.getTransaction().commit();
    }

    @Override
    public void deleteAll(Collection<String> strings) {
        for (final String string : strings) {
            delete(string);
        }

    }

    @Override
    public Human load(String s) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU_DN");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        final Human human = em.find(Human.class, s);
        em.getTransaction().commit();
        return human;
    }

    @Override
    public Map<String, Human> loadAll(Collection<String> strings) {
        final Map<String, Human> returnVal = new HashMap<String, Human>(strings.size());
        for (final String string : strings) {
            returnVal.put(string, load(string));
        }
        return returnVal;
    }

    @Override
    public Set<String> loadAllKeys() {
        return null;
    }
}
