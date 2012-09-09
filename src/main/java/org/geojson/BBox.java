package org.geojson;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
public class BBox {

    private static final String OSQRBRCKT = "[";
    private static final String CSQRBRCKT = "]";
    final public List<Coordinates> coordinatesList;

    public BBox(final List<Coordinates> coordinatesList) {
        this.coordinatesList = new LinkedList<Coordinates>(coordinatesList);
        Collections.sort(coordinatesList);
        Collections.unmodifiableCollection(coordinatesList);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder("");
        for (final Coordinates coordinates : coordinatesList) {
            stringBuilder.append(",");
            stringBuilder.append(coordinates.toString());
        }
        stringBuilder.delete(0, 1);//removing the first comma

        return OSQRBRCKT + stringBuilder.toString() + CSQRBRCKT;
    }
}
