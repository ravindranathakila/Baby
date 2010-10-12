package ai.ilikeplaces.logic.hotspots;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.validators.unit.BoundingBox;
import com.google.gdata.data.geo.impl.W3CPoint;
import net.sf.oval.Validator;

import java.util.HashSet;
import java.util.Set;

/**
 * This class calculates hotspots on a given bounding box.
 * <p/>
 * It takes a list of geocordinates along with their common names,
 * and analyzes the hotspots and the "mode" name for that spot.
 * <p/>
 * <p/>
 * It also takes an optional block size(per hotspot).
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Oct 10, 2010
 * Time: 2:35:54 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class HotspotAnalyzer {

    public Set<Rawspot> rawspots;

    public BoundingBox bb;

    private BoundingBox[][] mesh;

    private Hotspot[][] hotspots;

    public double[] blockWH = new double[]{0.000184401869775, 0.000133145585763375};

    private Validator v = new Validator();
    private int arrayWSize;
    private int arrayHSize;

    //lat 0.0005325823430535 / 4 = 0.000133145585763375
    //lng 0.0007376074791 / 4 =  0.000184401869775

    //6.876754135813088,79.93997275829315,6.8772867181561415,79.94071036577225

    public HotspotAnalyzer(final Set<Rawspot> rawspots, final BoundingBox bb) {
        this.rawspots = rawspots;
        this.bb = bb;
    }

    private void initMesh() {
        final double widthStart = bb.getObjectAsValid()[0].getObjectAsValid().getLongitude();
        final double widthEnd = bb.getObjectAsValid()[1].getObjectAsValid().getLongitude();

        final double heightStart = bb.getObjectAsValid()[0].getObjectAsValid().getLatitude();
        final double heightEnd = bb.getObjectAsValid()[1].getObjectAsValid().getLatitude();


        final double width = widthEnd - widthStart;
        final double height = heightEnd - heightStart;

        arrayWSize = (int) Math.ceil(width / blockWH[0]);
        arrayHSize = (int) Math.ceil(height / blockWH[1]);
        mesh = new BoundingBox[arrayWSize][arrayHSize];
        hotspots = new Hotspot[arrayWSize][arrayHSize];

        double currentLatitude, currentLongitude;

        currentLongitude = widthStart;
        for (int i = 0; i < arrayWSize; i++) {

            currentLatitude = heightStart;
            for (int j = 0; j < arrayHSize; j++) {

                mesh[i][j] = new BoundingBox().setObj(currentLatitude, currentLongitude, currentLatitude + blockWH[1], currentLongitude + blockWH[0]);
                mesh[i][j].validateThrow(v);

                currentLatitude += blockWH[1];
            }

            currentLongitude += blockWH[0];
        }
    }

    private synchronized void generateHotspots() {
        final BBWithSpots[][] allocatedrawspots = new BBWithSpots[arrayWSize][arrayHSize];
        for (int i = 0; i < mesh.length; i++) {
            for (int j = 0; j < mesh[i].length; j++) {
                allocatedrawspots[i][j] = (BBWithSpots) new BBWithSpots().setPair(mesh[i][j], new RawspotElasticArray());
            }
        }

        for (final Rawspot location : rawspots) {
            for (int i = 0; i < mesh.length; i++) {
                for (int j = 0; j < mesh[i].length; j++) {
                    if (mesh[i][j].bounds(location.getCoordinates())) {
                        allocatedrawspots[i][j].getValue().addEntries(location);
                    }
                }
            }
        }

        for (int i = 0; i < allocatedrawspots.length; i++) {
            for (int j = 0; j < allocatedrawspots[i].length; j++) {
                hotspots[i][j] = new Hotspot(allocatedrawspots[i][j].spotAverage(), "");
            }
        }
    }

    private synchronized void refresh() {
        initMesh();
        generateHotspots();
    }


    public synchronized Hotspot[][] getHotspots() {
        refresh();
        return hotspots;
    }

    public static void main(String args[]) {
        final Set<Rawspot> rs = new HashSet<Rawspot>();
        //tl
        rs.add(new Rawspot(new W3CPoint(6.877182864646137, 79.94012966752052), "a1"));
        rs.add(new Rawspot(new W3CPoint(6.877180201735318, 79.94016319513321), "a2"));
        rs.add(new Rawspot(new W3CPoint(6.877192184833859, 79.94016319513321), "a3"));
        rs.add(new Rawspot(new W3CPoint(6.877192184833859, 79.94012966752052), "a4"));
        rs.add(new Rawspot(new W3CPoint(6.877182864646137, 79.94016319513321), "a5"));
        rs.add(new Rawspot(new W3CPoint(6.877182864646137, 79.94016319513321), "a6"));
        rs.add(new Rawspot(new W3CPoint(6.877182864646137, 79.94016319513321), "a7"));
        rs.add(new Rawspot(new W3CPoint(6.877182864646137, 79.94016319513321), "a8"));

        //middle
        rs.add(new Rawspot(new W3CPoint(6.876984477749757, 79.9405038356781), "m1"));
        rs.add(new Rawspot(new W3CPoint(6.876984477749757, 79.9405038356781), "m2"));
        rs.add(new Rawspot(new W3CPoint(6.877064365100539, 79.94048774242401), "m3"));
        rs.add(new Rawspot(new W3CPoint(6.877064365100539, 79.94048774242401), "m4"));
        rs.add(new Rawspot(new W3CPoint(6.8770377359851045, 79.94051322340965), "m5"));
        rs.add(new Rawspot(new W3CPoint(6.8770377359851045, 79.94051322340965), "m6"));
        rs.add(new Rawspot(new W3CPoint(6.876997792309147, 79.94052529335022), "m7"));
        rs.add(new Rawspot(new W3CPoint(6.876997792309147, 79.94052529335022), "m8"));


        final BoundingBox bb = new BoundingBox().setObj("6.876754135813088,79.93997275829315,6.8772867181561415,79.94071036577225");
        bb.validateThrow();
        final HotspotAnalyzer hsa = new HotspotAnalyzer(rs, bb);
        final Hotspot[][] hotspots = hsa.getHotspots();
        for (final Hotspot[] hotspotspitch : hotspots) {
            for (final Hotspot yaw : hotspotspitch) {
                if (yaw.getCoordinates() != null) {
                    System.out.println("Hotspot:" + yaw.getCoordinates().getLatitude() + "," + yaw.getCoordinates().getLongitude());
                }
            }
        }
    }

}
