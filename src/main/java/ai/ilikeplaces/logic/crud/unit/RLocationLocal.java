package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Location;
import javax.ejb.Local;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RLocationLocal {

    public Location doRLocation(final String locationName, final String superLocationName);

    public Location doRLocation(final long locationId);
}
