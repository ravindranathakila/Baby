package ai.ilikeplaces.cache;

import ai.ilikeplaces.util.cache.SmartCache;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/9/11
 * Time: 11:04 AM
 */
public class Main implements SmartCache.RecoverWith<String, Integer> {

    final static public SmartCache<String, Integer> CACHE = new SmartCache<String, Integer>();
    final static public SmartCache<String, Integer> CACHE_2 = new SmartCache<String, Integer>(
            new Main()
    );

    private static int value = 0;

    public static void main(final String args[]) {

        final Main main = new Main();
        System.out.println(CACHE.get("1", main));
        System.out.println(CACHE.get("1", main));
        System.gc();
        System.out.println(CACHE.get("1", main));
        System.out.println(CACHE.get("2", main));

        System.out.println(CACHE_2.get("1", main));
        System.out.println(CACHE_2.get("1", main));
        System.gc();
        System.out.println(CACHE_2.get("1", main));
        System.out.println(CACHE_2.get("2", main));


    }

    @Override
    public Integer getValue(final String string) {
        System.out.println("UPDATING:" + string);
        return Integer.valueOf(string);
    }
}
