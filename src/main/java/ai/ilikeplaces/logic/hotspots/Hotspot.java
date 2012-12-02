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
public class Hotspot {
    private W3CPoint coordinates;
    private String commonName;
    private long hits;


    public Hotspot(final W3CPoint coordinates, final String commonName, final long hits) {
        this.coordinates = coordinates;
        this.commonName = commonName;
        this.hits = hits;
    }

    public W3CPoint getCoordinates() {
        return coordinates;
    }

    public Hotspot setCoordinates(final W3CPoint coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public String getCommonName() {
        return commonName;
    }

    public Hotspot setCommonName(final String commonName) {
        this.commonName = commonName;
        return this;
    }

    public long getHits() {
        return hits;
    }

    public Hotspot setHits(final long hits) {
        this.hits = hits;
        return this;
    }

    /**
     * @param obj object to compare with
     * @return if both locations are same(ignores name)
     */
    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof Hotspot) &&
                (this.getCoordinates().getLatitude().equals(((Hotspot) obj).getCoordinates().getLatitude()) &&
                        this.getCoordinates().getLongitude().equals(((Hotspot) obj).getCoordinates().getLongitude()));
    }
}
