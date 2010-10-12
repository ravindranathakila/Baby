package ai.ilikeplaces.logic.hotspots;

import ai.ilikeplaces.doc.License;
import com.google.gdata.data.geo.impl.W3CPoint;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Oct 10, 2010
 * Time: 6:52:34 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class Rawspot {
    private W3CPoint coordinates;
    private String commonName;

    public Rawspot(final W3CPoint coordinates, final String commonName) {
        this.coordinates = coordinates;
        this.commonName = commonName;
    }

    public W3CPoint getCoordinates() {
        return coordinates;
    }

    public Rawspot setCoordinates(final W3CPoint coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public String getCommonName() {
        return commonName;
    }

    public Rawspot setCommonName(final String commonName) {
        this.commonName = commonName;
        return this;
    }


    /**
     * @param obj object to compare with
     * @return if both locations are same(ignores name)
     */
    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof Rawspot) &&
                (this.getCoordinates().getLatitude().equals(((Rawspot) obj).getCoordinates().getLatitude()) &&
                        this.getCoordinates().getLongitude().equals(((Rawspot) obj).getCoordinates().getLongitude()));
    }
}
