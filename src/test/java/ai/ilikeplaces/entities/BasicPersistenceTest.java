package ai.ilikeplaces.entities;

import org.junit.*;

public class BasicPersistenceTest extends AbstractPersistenceTest {

    String locationName_ = "Sigiriya";
    String locationInfo_ = "Sigiriya is a world heritage in located Sri Lanka.";
    Location location_ = new Location();

    @Test
    public void runTest() throws Exception {

        location_.setLocationName(locationName_);
        location_.setLocationInfo(locationInfo_);

        manager.getTransaction().begin();
        manager.persist(location_);
        manager.getTransaction().commit();

        Location result = (Location) manager.createQuery("SELECT location FROM Location location WHERE location.locationName = :locationName").setParameter("locationName", locationName_).getSingleResult();

        Assert.assertEquals(locationName_, result.getLocationName());
        Assert.assertEquals(locationInfo_, result.getLocationInfo());
    }

    @After
    public void revertTest() {
        manager.getTransaction().begin();
        manager.remove(location_);
        manager.getTransaction().commit();
    }
}
