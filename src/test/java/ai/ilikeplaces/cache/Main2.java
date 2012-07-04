package ai.ilikeplaces.cache;

import ai.ilikeplaces.util.cache.SmartCache2;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/9/11
 * Time: 11:04 AM
 */
public class Main2 implements SmartCache2.RecoverWith<String, Integer, String> {

    final static public SmartCache2<String, Integer,String> CACHE = new SmartCache2<String, Integer,String>();
    final static public SmartCache2<String, Integer,String> CACHE_2 = new SmartCache2<String, Integer,String>(
            new Main2()
    );

    private static int value = 0;

    public static void main(final String args[]) {

        final Main2 main = new Main2();
        System.out.println(CACHE.get("1", main));
        System.out.println(CACHE.get("1", main));
        System.gc();
        System.out.println(CACHE.get("1", main));
        System.out.println(CACHE.get("2", main));

        System.out.println(CACHE_2.get("1", "na"));
        System.out.println(CACHE_2.get("1", "na"));
        System.gc();
        System.out.println(CACHE_2.get("1", "na"));
        System.out.println(CACHE_2.get("2", "na"));
        CACHE_2.invalidate("2");
        System.out.println(CACHE_2.get("2", "na"));
        System.out.println(CACHE_2.get("2", "na"));
        System.out.println(CACHE_2.get("2", "na"));
        CACHE_2.invalidate("2");
        System.out.println(CACHE_2.get("2", "na"));



    }

    @Override
    public Integer getValue(final String string, final String runtime) {
        System.out.println("UPDATING:" + string);
        return Integer.valueOf(string);
    }
}
