package ai.ilikeplaces.management;

import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.SmartLogger;
import ai.scribble.License;

/**
 * A class having static methods and static variables.
 * These variables will contain memory slots to be freed upon high memory consumption.
 * Used in conjunction with the MemoryManagementSystem
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jun 2, 2010
 * Time: 12:03:02 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class MemorySafe {

    public static final Runtime R = Runtime.getRuntime();

    public static final int MB = 1024 * 1024;//1 megabytes

    public static byte[] memlock_01 = null;//new byte[MB_10];
    public static byte[] memlock_02 = null;//new byte[MB_10];
    public static byte[] memlock_03 = null;//new byte[MB_10];
    public static byte[] memlock_04 = null;//new byte[MB_10];
    public static byte[] memlock_05 = null;//new byte[MB_10];
    public static byte[] memlock_06 = null;//new byte[MB_10];
    public static byte[] memlock_07 = null;//new byte[MB_10];
    public static byte[] memlock_08 = null;//new byte[MB_10];
    public static byte[] memlock_09 = null;//new byte[MB_10];;
    public static byte[] memlock_10 = null;//new byte[MB_10];
    private static final RuntimeException ALLOCATION_ON_LOW_MEMORY = new RuntimeException("SORRY! IGNORING MEMORY SAFE ALLOCATION ON LOW MEMORY!");


    public static void setMemlock_01(byte[] memlock_01) {
        MemorySafe.memlock_01 = memlock_01;
    }

    public static void setMemlock_02(byte[] memlock_02) {
        MemorySafe.memlock_02 = memlock_02;
    }

    public static void setMemlock_03(byte[] memlock_03) {
        MemorySafe.memlock_03 = memlock_03;
    }

    public static void setMemlock_04(byte[] memlock_04) {
        MemorySafe.memlock_04 = memlock_04;
    }

    public static void setMemlock_05(byte[] memlock_05) {
        MemorySafe.memlock_05 = memlock_05;
    }

    public static void setMemlock_06(byte[] memlock_06) {
        MemorySafe.memlock_06 = memlock_06;
    }

    public static void setMemlock_07(byte[] memlock_07) {
        MemorySafe.memlock_07 = memlock_07;
    }

    public static void setMemlock_08(byte[] memlock_08) {
        MemorySafe.memlock_08 = memlock_08;
    }

    public static void setMemlock_09(byte[] memlock_09) {
        MemorySafe.memlock_09 = memlock_09;
    }

    public static void setMemlock_10(byte[] memlock_10) {
        MemorySafe.memlock_10 = memlock_10;
    }

    public static byte[] getMemlock_01() {
        return memlock_01;
    }

    public static byte[] getMemlock_02() {
        return memlock_02;
    }

    public static byte[] getMemlock_03() {
        return memlock_03;
    }

    public static byte[] getMemlock_04() {
        return memlock_04;
    }

    public static byte[] getMemlock_05() {
        return memlock_05;
    }

    public static byte[] getMemlock_06() {
        return memlock_06;
    }

    public static byte[] getMemlock_07() {
        return memlock_07;
    }

    public static byte[] getMemlock_08() {
        return memlock_08;
    }

    public static byte[] getMemlock_09() {
        return memlock_09;
    }

    public static byte[] getMemlock_10() {
        return memlock_10;
    }


    private MemorySafe() {
        throw new UnsupportedOperationException("This class is not designed for construction of objects. It is static.");
    }

    public static void deallocate() {
        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, "FREEING MEMORY AND PERFORMING GC AT MEMORY(MB):" + (double) Runtime.getRuntime().freeMemory() / MemorySafe.MB, 0, null, true);
        setMemlock_01(null);
        setMemlock_03(null);
        setMemlock_03(null);
        setMemlock_04(null);
        setMemlock_05(null);
        setMemlock_06(null);
        setMemlock_07(null);
        setMemlock_08(null);
        setMemlock_09(null);
        setMemlock_10(null);
        System.gc();
        sl.complete(Loggers.DONE + " NOW FREE(MB):" + (double) Runtime.getRuntime().freeMemory() / MemorySafe.MB);
    }

    public static void allocate(final long free) {
        /**
         * 2 a magic number where we decide to reduce the threshold from free and divide it by two, to use it as buffer
         * 10 is the number of chunks.
         *
         * Remember that during startup, if this method is called manually(not listener) AND the memory is also above threshold,
         * then the chunkSize will get negative.
         *
         */
        final int chunkSize = (int) (((free - (long) (R.totalMemory() * (1 - MemoryWarningSystem.MemoryThreshold))) / 2) / 10);

        Loggers.DEBUG.debug("MEMORY SAFE FREE SIZE:" + free);
        Loggers.DEBUG.debug("MEMORY SAFE TOTAL SIZE:" + R.totalMemory());
        Loggers.DEBUG.debug("MEMORY SAFE THRESHOLD SIZE:" + MemoryWarningSystem.MemoryThreshold);
        Loggers.DEBUG.debug("MEMORY SAFE CHUNK SIZE:" + chunkSize);

        if (chunkSize < 0) {
            throw ALLOCATION_ON_LOW_MEMORY;
        }

        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, "STARTING TO BUFFER " + chunkSize * 10 / MB + "MB OF MEMORY AT MEMORY(MB):" + (double) Runtime.getRuntime().freeMemory() / MemorySafe.MB, 0, null, true);

        fillUpTenTimes(chunkSize);

        sl.complete("NOW FREE(MB):" + (double) Runtime.getRuntime().freeMemory() / MemorySafe.MB);
    }


    static private void fillUpTenTimes(final int chunkSize) {
        setMemlock_01(new byte[chunkSize]);
        setMemlock_03(new byte[chunkSize]);
        setMemlock_03(new byte[chunkSize]);
        setMemlock_04(new byte[chunkSize]);
        setMemlock_05(new byte[chunkSize]);
        setMemlock_06(new byte[chunkSize]);
        setMemlock_07(new byte[chunkSize]);
        setMemlock_08(new byte[chunkSize]);
        setMemlock_09(new byte[chunkSize]);
        setMemlock_10(new byte[chunkSize]);
    }
}

//        int null_count = 0;
//
//        null_count += getMemlock_01() == null? 0 : 1;
//        null_count += getMemlock_02() == null? 0 : 1;
//        null_count += getMemlock_03() == null? 0 : 1;
//        null_count += getMemlock_04() == null? 0 : 1;
//        null_count += getMemlock_05() == null? 0 : 1;
//        null_count += getMemlock_06() == null? 0 : 1;
//        null_count += getMemlock_07() == null? 0 : 1;
//        null_count += getMemlock_09() == null? 0 : 1;
//        null_count += getMemlock_10() == null? 0 : 1;

//        switch (10) {//New implementation uses all ten slots
//            case 1:
//                setMemlock_01(new byte[chunkSize]);
//                break;
//            case 2:
//                setMemlock_01(new byte[chunkSize]);
//                setMemlock_02(new byte[chunkSize]);
//                break;
//            case 3:
//                setMemlock_01(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                break;
//            case 4:
//                setMemlock_01(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_04(new byte[chunkSize]);
//                break;
//            case 5:
//                setMemlock_01(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_04(new byte[chunkSize]);
//                setMemlock_05(new byte[chunkSize]);
//                break;
//            case 6:
//                setMemlock_01(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_04(new byte[chunkSize]);
//                setMemlock_05(new byte[chunkSize]);
//                setMemlock_06(new byte[chunkSize]);
//                break;
//            case 7:
//                setMemlock_01(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_04(new byte[chunkSize]);
//                setMemlock_05(new byte[chunkSize]);
//                setMemlock_06(new byte[chunkSize]);
//                setMemlock_07(new byte[chunkSize]);
//                break;
//            case 8:
//                setMemlock_01(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_04(new byte[chunkSize]);
//                setMemlock_05(new byte[chunkSize]);
//                setMemlock_06(new byte[chunkSize]);
//                setMemlock_07(new byte[chunkSize]);
//                setMemlock_08(new byte[chunkSize]);
//                break;
//            case 9:
//                setMemlock_01(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_04(new byte[chunkSize]);
//                setMemlock_05(new byte[chunkSize]);
//                setMemlock_06(new byte[chunkSize]);
//                setMemlock_07(new byte[chunkSize]);
//                setMemlock_08(new byte[chunkSize]);
//                setMemlock_09(new byte[chunkSize]);
//                break;
//            default:
//                setMemlock_01(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_03(new byte[chunkSize]);
//                setMemlock_04(new byte[chunkSize]);
//                setMemlock_05(new byte[chunkSize]);
//                setMemlock_06(new byte[chunkSize]);
//                setMemlock_07(new byte[chunkSize]);
//                setMemlock_08(new byte[chunkSize]);
//                setMemlock_09(new byte[chunkSize]);
//                setMemlock_10(new byte[chunkSize]);
//                break;
//        }
