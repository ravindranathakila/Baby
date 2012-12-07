package ai.ilikeplaces.entities;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 20/11/12
 * Time: 12:14 AM
 */
public class HumanTest {

    @Test
    public void test() {
        System.out.println("\n=====================================================================================\n");
        System.out.println("Testing");
        System.out.println("\n-------------------------------------------------------------------------------------\n");

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ilpMainSchema");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        final Msg msg = new Msg();
        msg.setMsgId(1L);
        msg.setMsgTypeR(Msg.msgTypeHUMAN)
                .setMsgContentR("YureekA!")
                .setMsgMetadata("Okie!");

        entityManager.persist(msg);


        entityManager.getTransaction().commit();

        System.out.println("\n-------------------------------------------------------------------------------------\n");
        System.out.println("Test Complete");
        System.out.println("\n=====================================================================================\n");


    }
}
