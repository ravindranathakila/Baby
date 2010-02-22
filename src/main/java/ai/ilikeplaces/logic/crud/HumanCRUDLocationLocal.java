package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDLocationLocal {

    final static public String NAME = HumanCRUDLocationLocal.class.getSimpleName();

    public Return<Location> dirtyRLocation(final String locationName, final String superLocationName);

    public Location dirtyRLocation(final long locationId);

    public List<String> dirtyRLikeLocationNames(final String likeLocationName);

    public List<Location> dirtyRLikeLocations(final String likeLocationName);

    public List<Location> doNTxRLocationsBySuperLocation(final Location locationSuperset);
}
