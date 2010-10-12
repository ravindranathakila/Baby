package ai.ilikeplaces.logic.hotspots;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.ElasticArray;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Oct 10, 2010
 * Time: 9:23:00 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class RawspotElasticArray extends ElasticArray<Rawspot> {
    public RawspotElasticArray() {
        super(new Rawspot[]{});
    }

    public RawspotElasticArray(final Rawspot[] array) {
        super(array);
    }
}
