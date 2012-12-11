package ai.ilikeplaces.logic.validators.unit;

import ai.reaver.RefObj;
import ai.scribble.License;
import com.google.gdata.data.geo.impl.W3CPoint;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.MaxSize;
import net.sf.oval.constraint.MinSize;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Sep 12, 2010
 * Time: 7:29:37 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class BoundingBox extends RefObj<GeoCoord[]> {
    public BoundingBox() {
    }


    /**
     * @param commaSeparatedBoundingBox
     * @return
     */
    public BoundingBox setObj(final String commaSeparatedBoundingBox) {
        super.setObj(getGeoCoordsByBounds(commaSeparatedBoundingBox));
        return this;
    }

    /**
     * @param swlatitude
     * @param swlongitude
     * @param nelatitude
     * @param nelongitude
     * @return
     */
    public BoundingBox setObj(final double swlatitude, final double swlongitude, final double nelatitude, final double nelongitude) {
        super.setObj(new GeoCoord[]{(
                GeoCoord) new GeoCoord().setObj(swlatitude, swlongitude).getSelfAsValid(),
                (GeoCoord) new GeoCoord().setObj(nelatitude, nelongitude).getSelfAsValid()
        });
        return this;
    }

    public static GeoCoord[] getGeoCoordsByBounds(final String commaSeparatedBoundingBox) {
        final String[] vals = commaSeparatedBoundingBox.split(",");
        final GeoCoord[] bounds = new GeoCoord[(int) Math.ceil(vals.length / 2)];

        for (int i = 0; i < vals.length; i += 2) {
            bounds[i == 0 ? i : i / 2] = (GeoCoord) new GeoCoord().setObj(vals[i], vals[i + 1]).getSelfAsValid();
        }
        return bounds;
    }


    @IsInvariant
    @NotNull(message = "Sorry! The given coordinates seem to be wrong!")
    @MaxSize(value = 2, message = "Sorry! Only two values supported")
    @MinSize(value = 2, message = "Sorry! Two values needed")
    @Override
    public GeoCoord[] getObj() {
        return obj;
    }


    /**
     * @param w3CPointToBeCheckIfBounded W3CPoint To Be Check If Bounded
     * @return if the given coordinates are within the bounding box(includes if it is on South West Spreading Bounds and Excludes if it is on North East Spreading Bounds)
     */
    public boolean bounds(final W3CPoint w3CPointToBeCheckIfBounded) {
        this.validateThrow(new Validator());

        final W3CPoint is = w3CPointToBeCheckIfBounded;

        obj[0].validateThrow();
        obj[1].validateThrow();

        final W3CPoint sw = obj[0].getObjectAsValid();
        final W3CPoint ne = obj[1].getObjectAsValid();

        return ((sw.getLatitude() <= is.getLatitude()) && (ne.getLatitude() > is.getLatitude())) &&
                ((sw.getLongitude() <= is.getLongitude()) && (ne.getLongitude() > is.getLongitude()));
    }

    /**
     * @return 3.13343, 23.23232 format lat,lng
     */
    @Override
    public String toString() {
        return getObjectAsValid()[0].getObjectAsValid().getLatitude() + "," + getObjectAsValid()[0].getObjectAsValid().getLongitude() +
                "," +
                getObjectAsValid()[1].getObjectAsValid().getLatitude() + "," + getObjectAsValid()[1].getObjectAsValid().getLongitude();
    }
}
