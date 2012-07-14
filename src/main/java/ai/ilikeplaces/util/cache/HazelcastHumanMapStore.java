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

    public HazelcastHumanMapStore() {
        System.out.println("Starting Hazelcast MapStore implementation:" + this.getClass().getSimpleName());
    }

    @Override
    public void store(String s, Human human) {
        try {
            System.out.println("Attempting to store data.");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU_DN");
            System.out.println("Obtained EntityManagerFactory.");
            EntityManager em = emf.createEntityManager();
            System.out.println("Obtained EntityManager.");
            em.getTransaction().begin();
            System.out.println("Began Transaction.");
            em.persist(human);
            System.out.println("Persisted data.");
            em.getTransaction().commit();
            System.out.println("Committed Transaction");
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    @Override
    public void storeAll(Map<String, Human> stringHumanMap) {
        try {
            for (final String key : stringHumanMap.keySet()) {
                store(key, stringHumanMap.get(key));
            }
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    @Override
    public void delete(String s) {
        try {
            System.out.println("Attempting to store data.");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU_DN");
            System.out.println("Obtained EntityManagerFactory.");
            EntityManager em = emf.createEntityManager();
            System.out.println("Obtained EntityManager.");
            em.getTransaction().begin();
            System.out.println("Began Transaction.");
            em.remove(em.find(Human.class, s));
            System.out.println("Removed data.");
            em.getTransaction().commit();
            System.out.println("Committed Transaction.");
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    @Override
    public void deleteAll(Collection<String> strings) {
        try {
            for (final String string : strings) {
                delete(string);
            }
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
        }

    }

    @Override
    public Human load(String s) {
        try {
            System.out.println("Attempting to store data.");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU_DN");
            System.out.println("Obtained EntityManagerFactory.");
            EntityManager em = emf.createEntityManager();
            System.out.println("Obtained EntityManager.");
            em.getTransaction().begin();
            System.out.println("Began Transaction.");
            final Human human = em.find(Human.class, s);
            System.out.println("Loaded data.");
            em.getTransaction().commit();
            System.out.println("Committed Transaction.");
            return human;
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
            throw new RuntimeException(t);
        }
    }

    @Override
    public Map<String, Human> loadAll(Collection<String> strings) {
        try {
            final Map<String, Human> returnVal = new HashMap<String, Human>(strings.size());
            for (final String string : strings) {
                returnVal.put(string, load(string));
            }
            return returnVal;
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
            throw new RuntimeException(t);
        }
    }

    @Override
    public Set<String> loadAllKeys() {
        return null;
    }
}
