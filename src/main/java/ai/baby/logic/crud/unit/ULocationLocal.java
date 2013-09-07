package ai.baby.logic.crud.unit;

import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.baby.util.exception.DBFetchDataException;
import ai.scribble.License;

import javax.ejb.Local;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/19/10
 * Time: 9:34 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface ULocationLocal {
    public Location doULocationLatLng(long locationId, final double latitude, final double longitude);

    public Location doULocationPosts(final long locationId, final Map<String, String> posts, final RefreshSpec refreshSpec) throws DBFetchDataException;
}
