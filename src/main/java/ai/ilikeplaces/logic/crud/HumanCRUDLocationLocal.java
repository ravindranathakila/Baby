package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Location;
import javax.ejb.Local;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDLocationLocal {

    final static public String NAME = HumanCRUDLocationLocal.class.getSimpleName();

    public Location doDirtyHumanRLocation(final String locationName, final String superLocationName);

    public Location doDirtyHumanRLocation(final long locationId);
}
