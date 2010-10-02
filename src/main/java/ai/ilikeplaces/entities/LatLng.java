package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Sep 12, 2010
 * Time: 9:26:21 PM
 * To change this template use File | Settings | File Templates.
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface LatLng {
    public Double getLatitude();

    public void setLatitude(final Double latitude);

    public Double getLongitude();

    public void setLongitude(final Double longitude);

}
