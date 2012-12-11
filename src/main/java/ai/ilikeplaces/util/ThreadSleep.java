package ai.ilikeplaces.util;

import ai.scribble.License;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Mar 13, 2010
 * Time: 9:13:21 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ThreadSleep {
    private static final String THREAD_SLEEP_FAILED = "Thread sleep failed.";

    static public void sleep(final long durationToSleepMillis) {
        try {
            Thread.sleep(durationToSleepMillis);
        } catch (final InterruptedException e) {
            Loggers.WARN.warn(THREAD_SLEEP_FAILED);
        }
    }
}
