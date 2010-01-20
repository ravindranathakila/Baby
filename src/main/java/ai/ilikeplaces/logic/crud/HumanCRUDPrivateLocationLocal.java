package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;


/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 2:41:13 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDPrivateLocationLocal {
    final static public String NAME = HumanCRUDPrivateLocationLocal.class.getSimpleName();

    public Return<PrivateLocation> cPrivateLocation(final String humanId, final String privateLocationName, final String privateLocationInfo);

    public Return<PrivateLocation> rPrivateLocation(final String humanId, final long privateLocationId);

    public Return<Boolean> dPrivateLocation(final String humanId, final long privateLocationId);
}
