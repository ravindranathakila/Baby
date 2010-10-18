package ai.ilikeplaces.logic.hotspots;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.logic.validators.unit.BoundingBox;
import ai.ilikeplaces.util.Pair;
import com.google.gdata.data.geo.impl.W3CPoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Oct 10, 2010
 * Time: 9:14:23 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class BBWithSpots extends Pair<BoundingBox, RawspotElasticArray> {
    private static final String SPACE = " ";
    private static final String DOUBLESPACE = "  ";
    private static final String EMPTY = "";

    /**
     * This is hard, but lets give it a try. First lets go for, requirements, then a small algorithm
     * <p/>
     * Requirements:
     * <p/>
     * Use less memory.
     * Compatibility with names like "titi kaka".
     * Support for multiple common names.
     * <p/>
     * Approaches:
     * <p/>
     * To grab names as "titi kaka", a substring(character sequence) based algorithm should be used.
     * Try to use exclusion. Note that to exclude, 3 items should be present
     * <p/>
     * <p/>
     * define Strings<br/><br/>
     * <p/>
     * "a is dumb"(A)                                                                                              <br/>
     * "b is a hotel"(B)                                                                                           <br/>
     * "hotel california"(C)                                                                                       <br/>
     * "this is hotel california, usa"(D)                                                                          <br/>
     * "this is not a hotel"(E)                                                                                    <br/>
     * <br/>
     * Triplet trip = {A,B,C}                                                                                      <br/>
     * Get 2 out of trip which have most matches                                                                   <br/>
     * <br/>
     * <br/>
     * <br/>
     * <br/>
     * <br/>
     * <br/>
     *
     * @return common name of this area
     */
    @TODO(tasks = {"Use random fetching for large datasets", "Greedy fetching of nearby locations"})
    final String modeName() {


        final Map<Integer, Pair<String, Integer>> index = new HashMap<Integer, Pair<String, Integer>>();

        int i = 0;
        while (i < getValue().array.length) {
            final String name = getValue().array[i].getCommonName().trim().replace(DOUBLESPACE, SPACE/*Very important as this will introduce bugs if missing*/).toLowerCase().replaceAll("[^\\s0-9a-zA-Z]", EMPTY);//remove all alpha numeric characters except spaces
            index.put(i, new Pair<String, Integer>(name, 0));
            i++;
        }//if performance becomes a big issue, move this inside the next loop

        int j = 0;
        while (j < getValue().array.length) {
            final Pair<String, Integer> fancy = index.get(j);
            int k = 0;
            while (k < getValue().array.length) {
                if (j != k) {//lets avoid fancy and copy cat being the same person ;)
                    final Pair<String, Integer> cat = index.get(k);
                    int rank = 0;
                    for (final String fancyWord : fancy.getKey().split(SPACE)) {
                        rank = cat.getKey().contains(fancyWord) ? rank + 1 : rank;//increase ranking
                    }
                    fancy.setValue(fancy.getValue() + rank);//increment her rank
                }
                k++;
            }
            j++;
        }


        int m = 0;

        Pair<String, Integer> promoted = null;
        Pair<String, Integer> assistant = null;
        Pair<String, Integer> current = null;
        while (m < getValue().array.length) {

            current = index.get(m);


//            System.out.println("m:" + m);
//            System.out.println("Pre promoted:" + promoted);
//            System.out.println("Post assistant:" + assistant);
//            System.out.println("Pre current:" + current);

            if (promoted == null) {
                promoted = current;
            }


            if (current.getValue() > promoted.getValue()) {
                assistant = promoted;
                promoted = current;
            } else {
                if (assistant == null) {
                    assistant = current;
                } else if (current.getValue() > assistant.getValue()) {
                    assistant = current;
                }
            }
            m++;
        }


        return promoted == null ? EMPTY :
               (promoted.getValue() > assistant.getValue()
                ? promoted.getKey()//if ranks are not equal
                : (promoted.getKey().length() < assistant.getKey().length()
                   ? promoted.getKey()//if ranks are equal and promoted has shorter name
                   : assistant.getKey()));//if ranks are equal assistant and has shorter name
    }

    final W3CPoint spotAverage() {
        double latTotal = 0;
        double lngTotal = 0;
        int i = 0;
        while (i < getValue().array.length) {
            final Rawspot rawspot = getValue().array[i];

            latTotal += rawspot.getCoordinates().getLatitude();
            lngTotal += rawspot.getCoordinates().getLongitude();
            i++;
        }

        return i == 0 || latTotal == 0 || lngTotal == 0 ? null : new W3CPoint(latTotal / i, lngTotal / i);
    }
}
