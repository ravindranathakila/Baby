package ai.ilikeplaces;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ravindranath Akila
 */
public class GlobalEntityManager {

    final private EntityManagerFactory emf = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU");
    /**
     *
     */
    final public EntityManager em = emf.createEntityManager();
    private static GlobalEntityManager Singleton;
    final private static boolean Initialized = false;

    private GlobalEntityManager() {
    }

    /**
     *
     * @return
     */
    final public static GlobalEntityManager getSingleton() {
        if (Initialized) {
            return Singleton;
        } else {
            Singleton = new GlobalEntityManager();
            return GlobalEntityManager.Singleton;
        }
    }

    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        throw new java.lang.CloneNotSupportedException("THIS IS A SINGLETON!");
    }
}
