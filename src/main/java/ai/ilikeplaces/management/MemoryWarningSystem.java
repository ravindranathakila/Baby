package ai.ilikeplaces.management;

import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.ThreadSleep;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryNotificationInfo;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This memory warning system will call the listener when we
 * exceed the percentage of available memory specified.  There
 * should only be one instance of this object created, since the
 * usage threshold can only be set to one number.
 * <p/>
 * <p/>
 * <b>OutOfMemoryError Warning System</b>
 * <p/>
 * source:
 * 2004-07-20 The Java Specialists' Newsletter [Issue 092] - OutOfMemoryError Warning System
 * <p/>
 * Author: Dr. Heinz M. Kabutz
 * <p/>
 * Found by me at : http://www.roseindia.net/javatutorials/OutOfMemoryError_Warning_System.shtml
 * <p/>
 * <p/>
 * In Issue 061 of my newsletter, I asked readers whether their applications had ever caused an OutOfMemoryError. I then asked them to email me if they would like to know how to receive a warning, shortly before it was about to happen. Wow, the response! The requests kept on pouring in, and so far, I have had over 200 enquiries. At the time, my ideas for a warning system were sketchy at best, and would have been hopelessly inaccurate, in comparison to what JDK 1.5 offers us.
 * <p/>
 * JDK 1.5 has added some wonderful new management beans that make writing an OutOfMemoryError Warning System possible. The most difficult part was probably finding resources on the topic. Google turned up two resources: The JDK documentation and a website written in Japanese ;-)
 * <p/>
 * An OutOfMemoryError (OOME) is bad. It can happen at any time, with any thread. There is little that you can do about it, except to exit the program, change the -Xmx value, and restart the JVM. If you then make the -Xmx value too large, you slow down your application. The secret is to make the maximum heap value the right size, neither too small, nor too big. OOME can happen with any thread, and when it does, that thread typically dies. Often, there is not enough memory to build up a stack trace for the OOME, so you cannot even determine where it occurred, or why. You can use the exception catching mechanism of Issue 089, but that is then an after-the-fact measure, rather than preventative.
 * <p/>
 * In January this year, I was migrating a program from MySQL to MS SQL Server. The author of the original program had used some JDBC commands that caused memory leaks under the SQL Server JDBC driver. This meant that periodically, one of the application's threads would simply vanish, leaving parts of the system paralyzed. Eliminating the OOME was a major task, and only happened when I rewrote all the database access code!
 * <p/>
 * Back to the issue at hand - how can we know when OOME's are about to occur? The answer lies in the java.lang.management package of JDK 1.5. The ManagementFactory class returns all sorts of useful JMX beans that we can use to manage the JVM. One of these beans is the MemoryMXBean. Sun's implementation of the MemoryMXBean interface also implements the interface javax.management.NotificationEmitter. The recommended way of listening to notifications by the memory bean is by downcasting the MemoryMXBean to the NotificationEmitter interface. I can hardly believe it myself, you can verify this by looking at the documentation of the MemoryMXBean.
 * <p/>
 * Once you have downcast the MemoryMXBean to a NotificationEmitter, you can add a NotificationListener to the MemoryMXBean. You should verify that the notification is of type MEMORY_THRESHOLD_EXCEEDED. In my MemoryWarningSystem you add listeners that implement the MemoryWarningSystem.Listener interface, with one method memoryUsageLow(long usedMemory, long maxMemory) that will be called when the threshold is reached. In my experiments, the memory bean notifies us quite soon after the usage threshold has been exceeded, but I could not determine the granularity. Something to note is that the listener is being called by a special thread, called the Low Memory Detector thread, that is now part of the standard JVM.
 * <p/>
 * What is the threshold? And which of the many pools should we monitor? The only sensible pool to monitor is the Tenured Generation (Old Space). When you set the size of the memory with -Xmx256m, you are setting the maximum memory to be used in the Tenured Generation. I could not find a neat way of finding the tenured generation, except by looking through all the pools in my findTenuredGenPool() method, and returning the first one that was of type HEAP and where I was permitted to specify a usage threshold. I do not know whether a better approach would not have been to search for the name "Tenured Gen"?
 * <p/>
 * In my setPercentageUsageThreshold(double percentage) method, I specify when I would like to be notified. Note that this is a global setting since you can only have one usage threshold per Java Virtual Machine. The percentage is used to calculate the usage threshold, based on the maximum memory size of the Tenured Generation pool (not the Runtime.getRuntime().maxMemory() value!).
 */
public class MemoryWarningSystem {

    private final Collection<MemoryListener> memoryListeners = new ArrayList<MemoryListener>();

    public static double MemoryNormalValue = 0.2;
    public static double MemoryThreshold = 0.9;
    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("SORRY! FREE_MEMORY_THRESHOLD< ( 1 - MEMORY_THRESHOLD) IS NOT LOGICAL.");


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public interface MemoryListener {
        public void memoryUsageLow(final long usedMemory, final long maxMemory);

        public void memoryUsageNormal(final long usedMemory, final long maxMemory);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Tenured Space Pool can be determined by it being of type
     * HEAP and by it being possible to set the usage threshold.
     *
     * @return
     */
    private static MemoryPoolMXBean findTenuredGenPool() {
        for (final MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            // I don't know whether this approach is better, or whether
            // we should rather check for the pool name "Tenured Gen"?
            if (pool.getType() == MemoryType.HEAP && pool.isUsageThresholdSupported()) {
                return pool;
            }
        }
        throw new AssertionError("SORRY!COULD NOT FIND TENURED SPACE.");
    }

    private static final MemoryPoolMXBean tenuredGenPool = findTenuredGenPool();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public MemoryWarningSystem() {
        if (MemoryNormalValue + MemoryThreshold < 1) {
            throw ILLEGAL_STATE_EXCEPTION;
        }

        final NotificationEmitter emitter = (NotificationEmitter) ManagementFactory.getMemoryMXBean();

        emitter.addNotificationListener(new NotificationListener() {
            public void handleNotification(Notification n, Object hb) {
                if (n.getType().equals(MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED)) {
                    long maxMemory = tenuredGenPool.getUsage().getMax();
                    long usedMemory = tenuredGenPool.getUsage().getUsed();

                    //New thread to monitor free state of memory
                    final Thread r = new Thread(
                            new Runnable() {
                                private final Collection<MemoryListener> mylisteners = memoryListeners;
                                final MemoryPoolMXBean mytenuredGenPool = tenuredGenPool;
                                long timeout = 2000;
                                double free = 0;

                                public void run() {
                                    ThreadSleep.sleep(2000);
                                    Loggers.STATUS.info("Hello! I started the normal memory checker.");
                                    loop:
                                    while (true) {
                                        ThreadSleep.sleep(2000);
                                        //we don't want to create local variable since if the loop is set to go fast, it will consume memory
                                        free = ((double) (mytenuredGenPool.getUsage().getMax() - mytenuredGenPool.getUsage().getUsed()) / (double) mytenuredGenPool.getUsage().getMax());
                                        if (free > MemoryNormalValue) {
                                            Loggers.STATUS.info("Memory Back to Normal. Free Percentage:" + free);
                                            for (final MemoryListener listener : mylisteners) {
                                                listener.memoryUsageNormal(mytenuredGenPool.getUsage().getUsed(), mytenuredGenPool.getUsage().getMax());
                                            }
                                            break loop;
                                        }
                                    }
                                }
                            }
                    );
                    r.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            Loggers.WARN.error("SORRY! MEMC BACK TO NORMAL MONITOR THREAD POSSIBLY FAILED!", e);
                        }
                    });
                    r.setName("Normal Memory Detector");
                    r.start();

                    for (MemoryListener memoryListener : memoryListeners) {
                        memoryListener.memoryUsageLow(usedMemory, maxMemory);
                    }


                }
            }
        }, null, null);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean addListener(final MemoryListener memoryListener) {
        return memoryListeners.add(memoryListener);
    }

    public boolean removeListener(final MemoryListener memoryListener) {
        return memoryListeners.remove(memoryListener);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void setPercentageUsageThreshold(final double threshold) {
        if (threshold <= 0.0 || threshold > 1.0) {
            throw new IllegalArgumentException("Threshold value should be between " + 0.0 + " and " + 1.0);
        }
        final long maxMemory = tenuredGenPool.getUsage().getMax();
        final long warningThreshold = (long) (maxMemory * threshold);
        tenuredGenPool.setUsageThreshold(warningThreshold);
        MemoryThreshold = threshold;
    }

    public static void setPercentageUsageNormal(final double normalAgain) {
        if (normalAgain <= 0.0 || normalAgain > 1.0) {
            throw new IllegalArgumentException("Memory normal value should be between " + 0.0 + " and " + 1.0);
        }
        MemoryNormalValue = normalAgain;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
