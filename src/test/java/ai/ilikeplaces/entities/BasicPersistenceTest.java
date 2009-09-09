package ai.ilikeplaces.entities;

import ai.ilikeplaces.CrudService;
import org.junit.*;

public class BasicPersistenceTest extends AbstractPersistenceTest {

    String locationName_ = "Sigiriya";
    String locationInfo_ = "Sigiriya is a world heritage in located Sri Lanka.";
    Location location_ = new Location();

    @Test
    public void runTest() throws Exception {

        location_.setLocationName(locationName_);
        location_.setLocationInfo(locationInfo_);

        CrudService<Location> cs = new CrudService<Location>();

        cs.entityManager.getTransaction().begin();
        location_ = cs.update(location_);
        cs.entityManager.getTransaction().commit();
        Location result = (Location) cs.entityManager.createQuery("SELECT location FROM Location location WHERE location.locationName = :locationName").setParameter("locationName", locationName_).getSingleResult();

        Assert.assertEquals(locationName_, result.getLocationName());
        Assert.assertEquals(locationInfo_, result.getLocationInfo());
    }

    @After
    public void revertTest() {
        CrudService<Location> cs = new CrudService<Location>();

        cs.entityManager.getTransaction().begin();
        cs.delete(Location.class, locationName_);
        cs.entityManager.getTransaction().commit();
    }
}
