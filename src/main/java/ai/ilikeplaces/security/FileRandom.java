package ai.ilikeplaces.security;

import ai.scribble._fix;
import ai.scribble._ok;

import java.util.Random;
import java.util.WeakHashMap;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Deprecated
@_fix(issue = "this class is not being used. it was returning wrong values and was not debugged")
@_ok
final public class FileRandom {

    final static private Random r = new Random(System.currentTimeMillis());
    final static private WeakHashMap<Long, Long> whm = new WeakHashMap<Long, Long>();

    @_fix(issue = "Returns odd names java.utile.random kind of names. Is it due to weak hashmap?")
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
