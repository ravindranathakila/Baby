package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateLocation;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 12, 2010
 * Time: 10:31:21 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UPrivateLocationLocal {

    public PrivateLocation doUPrivateLocationData(final String humanId__, final String privateLocationId__, final String privateLocationName__, final String privateLocationInfo__);

    public PrivateLocation doUPrivateLocationAddOwners(final String humanId__, final String privateLocationId__, final List<String> privateLocationOwners__);

    public PrivateLocation doUPrivateLocationRemoveOwners(final String humanId__, final String privateLocationId__, final List<String> privateLocationOwners__);

    public PrivateLocation doUPrivateLocationAddVisitors(final String humanId__, final String privateLocationId__, final List<String> privateLocationVisitors__);

    public PrivateLocation doUPrivateLocationRemoveVisitors(final String humanId__, final String privateLocationId__, final List<String> privateLocationVisitors__);

}