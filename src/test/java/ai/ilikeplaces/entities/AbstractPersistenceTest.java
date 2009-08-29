package ai.ilikeplaces.entities;

import org.junit.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ravindranath Akila
 */
public abstract class AbstractPersistenceTest {

    protected EntityManagerFactory emf;
    protected EntityManager manager;

    @Before
    public void beforeTests() {
        emf = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU");
        manager = emf.createEntityManager();
    }

    @Test
    abstract public void runTest() throws Exception;

    @After
    abstract public void revertTest();

    @After
    public void afterTests() {
        manager.close();
        manager = null;//GC
        emf.close();
        emf = null;//GC
    }
}
