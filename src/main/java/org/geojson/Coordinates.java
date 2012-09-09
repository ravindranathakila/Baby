package org.geojson;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
public class Coordinates implements Comparable<Coordinates> {
    private static final String OPENSQRBRCKT = "[";
    private static final char CLOSESQRBRCKT = ']';
    private static final String COMMA = ",";
    final private double x;
    final private double y;

    public Coordinates(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return OPENSQRBRCKT +
                x +
                COMMA +
                y +
                CLOSESQRBRCKT;
    }

    /**
     * As per spec "To include information on the coordinate range for geometries, features, or feature collections, a GeoJSON object may have a member named "bbox".
     * The value of the bbox member must be a 2*n array where n is the number of dimensions represented in the contained geometries,
     * with the lowest values for all axes followed by the highest values.
     * The axes order of a bbox follows the axes order of geometries. In addition,
     * the coordinate reference system for the bbox is assumed to match the coordinate reference system of the GeoJSON object of which it is a member."
     *
     * @param o
     * @return
     */
    public int compareTo(Coordinates o) {
        if (this.getX() == o.getX() && this.getY() == o.getY()) {
            return 0;
        }

        if (this.getX() == o.getX()) {
            if (this.getY() < o.getY()) {
                return -1;
            } else {
                return 1;
            }
        }

        if (this.getY() == o.getY()) {
            if (this.getX() < o.getX()) {
                return -1;
            } else {
                return 1;
            }
        }

        if (this.getX() < o.getX()) {
            return -1;
        } else {
            return 1;
        }

    }
}
