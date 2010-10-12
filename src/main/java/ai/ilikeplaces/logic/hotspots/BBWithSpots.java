package ai.ilikeplaces.logic.hotspots;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.validators.unit.BoundingBox;
import ai.ilikeplaces.util.Pair;
import com.google.gdata.data.geo.impl.W3CPoint;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Oct 10, 2010
 * Time: 9:14:23 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class BBWithSpots extends Pair<BoundingBox, RawspotElasticArray> {

    final W3CPoint spotAverage() {
        System.out.println("BOUNDING BOX:" + getKey().toString());
        double latTotal = 0;
        double lngTotal = 0;
        int i = 0;
        while (i < getValue().array.length) {
            final Rawspot rawspot = getValue().array[i];

            System.out.println("Rawspot:" + rawspot.getCommonName() + " " + rawspot.getCoordinates().getLatitude() + "," + rawspot.getCoordinates().getLongitude());

            latTotal += rawspot.getCoordinates().getLatitude();
            lngTotal += rawspot.getCoordinates().getLongitude();
            i++;
        }

        System.out.println("lat>" + latTotal + " lng>" + lngTotal + " i>" + i + ":" + latTotal / (double) i + "," + lngTotal / (double) i);

//        new W3CPoint(latTotal / i, lngTotal / i);
//        new W3CPoint(0.0, 0.0);

        return i == 0 || latTotal == 0 || lngTotal == 0 ? null : new W3CPoint(latTotal / i, lngTotal / i);
    }
}
