package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.util.RefObj;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.jpa.RefreshSpec;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDLocationLocal {

    final static public String NAME = HumanCRUDLocationLocal.class.getSimpleName();

    public Return<Location> dirtyRLocation(final String locationName, final String superLocationName);

    public Return<Location> dirtyRLocation(final long locationId);

    public Return<Location> doRLocation(final long locationId, final RefreshSpec refreshSpec);

    public Return<Location> doULocationComments(final long locationId, final Map<String, String> posts, final RefreshSpec refreshSpec);

    public List<String> dirtyRLikeLocationNames(final String likeLocationName);

    public List<Location> dirtyRLikeLocations(final String likeLocationName);

    public List<Location> doDirtyRLocationsBySuperLocation(final Location locationSuperset);

    public Location doULocationLatLng(RefObj<Long> locationId, final RefObj<Double> latitude, final RefObj<Double> longitude);
}
