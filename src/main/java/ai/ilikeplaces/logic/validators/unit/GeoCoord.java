package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.util.RefObj;
import ai.scribble.License;
import com.google.gdata.data.geo.GeoLat;
import com.google.gdata.data.geo.GeoLong;
import com.google.gdata.data.geo.impl.W3CPoint;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Sep 12, 2010
 * Time: 7:29:37 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class GeoCoord extends RefObj<W3CPoint> {
    public GeoCoord() {
    }

    /**
     * @param latitudeCOMMAlongitude
     * @return this
     */
    public GeoCoord setObj(final String latitudeCOMMAlongitude) {
        try {
            final GeoLat geoLat = new GeoLat();
            geoLat.setValue(latitudeCOMMAlongitude.split(",")[0]);//will throw a runtime exception if wrong.

            final GeoLong geoLong = new GeoLong();
            geoLong.setValue(latitudeCOMMAlongitude.split(",")[1]);//will throw a runtime exception if wrong. 

            setObj(new W3CPoint(geoLat.getLatitude(), geoLong.getLongitude()));
        } catch (final RuntimeException e) {
            super.setObj(null);
        }
        return this;
    }

    public static GeoCoord[] getGeoCoordsByBounds(final String listOfLatCommaLongOfBoundsInOrder) {
        final String[] vals = listOfLatCommaLongOfBoundsInOrder.split(",");
        final GeoCoord[] bounds = new GeoCoord[(vals.length / 2) + 1];

        for (int i = 0; i < vals.length; i += 2) {
            bounds[i == 0 ? i : i / 2] = new GeoCoord().setObj(vals[i], vals[i + 1]);
        }
        return bounds;
    }

    /**
     * @param latitude
     * @param longitude
     * @return this
     */
    public GeoCoord setObj(final String latitude, final String longitude) {
        try {
            final GeoLat geoLat = new GeoLat();
            geoLat.setValue(latitude);//will throw a runtime exception if wrong.

            final GeoLong geoLong = new GeoLong();
            geoLong.setValue(longitude);//will throw a runtime exception if wrong.

            setObj(new W3CPoint(geoLat.getLatitude(), geoLong.getLongitude()));
        } catch (final RuntimeException e) {
            super.setObj(null);
        }
        return this;
    }

    /**
     * @param latitude
     * @param longitude
     * @return this
     */
    public GeoCoord setObj(final double latitude, final double longitude) {
        try {
            final GeoLat geoLat = new GeoLat();
            geoLat.setLatitude(latitude);//will throw a runtime exception if wrong.

            final GeoLong geoLong = new GeoLong();
            geoLong.setLongitude(longitude);//will throw a runtime exception if wrong.

            setObj(new W3CPoint(geoLat.getLatitude(), geoLong.getLongitude()));
        } catch (final RuntimeException e) {
            super.setObj(null);
        }
        return this;
    }

    @IsInvariant
    @NotNull(message = "Sorry! The given coordinates seem to be wrong!")
    @Override
    public W3CPoint getObj() {
        return obj;
    }

    /**
     * @return 3.13343, 23.23232 format lat,lng
     */
    @Override
    public String toString() {
        return getObjectAsValid().getLatitude() + "," + getObjectAsValid().getLongitude();
    }
}
