package ai.ilikeplaces.security;

import ai.ilikeplaces.doc.*;
import java.util.WeakHashMap;
import java.util.Random;

/**
 *
 * @author Ravindranath Akila
 */
@Deprecated
@FIXME(issue = "this class is not being used. it was returning wrong values and was not debugged")
@OK
final public class FileRandom {

    final static private Random r = new Random(System.currentTimeMillis());
    final static private WeakHashMap<Long, Long> whm = new WeakHashMap<Long, Long>();

    @FIXME(issue = "Returns odd names java.utile.random kind of names. Is it due to weak hashmap?")
    final static public String getName() {
        return genRandom(System.currentTimeMillis());
    }

    final static synchronized String genRandom(Long time) {
        Long randomVal = r.nextLong();
        while (whm.containsValue(randomVal)) {
            randomVal = r.nextLong();
        }
        whm.put(time, randomVal);
        return String.valueOf(r) + String.valueOf(time);
    }
}
