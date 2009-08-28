package ai.ilikeplaces.entities;

import org.junit.Assert;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BasicPersistenceTest {

    @Test
    public void testBasicPersistence() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU");
        EntityManager manager = factory.createEntityManager();

        String locationName_ = "Sigiriya";
        String locationInfo_ = "Sigiriya is a world heritage in located Sri Lanka.";

        Location location_ = new Location();

        location_.setLocationName(locationName_);
        location_.setLocationInfo(locationInfo_);

        manager.getTransaction().begin();
        manager.persist(location_);
        manager.getTransaction().commit();

        Location result = (Location) manager.createQuery("SELECT location FROM Location location WHERE location.locationName = :locationName").setParameter("locationName", locationName_).getSingleResult();

        Assert.assertEquals(locationName_, result.getLocationName());
        Assert.assertEquals(locationInfo_, result.getLocationInfo());

        manager.close();
        factory.close();
    }
}
