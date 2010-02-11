package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;
import java.util.List;


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

    public Return<PrivateLocation> uPrivateLocationAddOwners(final String humanId__, final long privateLocationId__, final List<String> privateLocationOwners__);

    public Return<PrivateLocation> uPrivateLocationAddOwner(final HumanId humanId__, final long privateLocationId__, final HumansFriend owner);
    
    public Return<PrivateLocation> uPrivateLocationRemoveOwner(final HumanId humanId__, final long privateLocationId__, final HumansFriend owner);

    public Return<Boolean> dPrivateLocation(final String humanId, final long privateLocationId);
}
