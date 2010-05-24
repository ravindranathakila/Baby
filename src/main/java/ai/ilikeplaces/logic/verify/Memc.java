package ai.ilikeplaces.logic.verify;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.Loggers;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Apr 29, 2010
 * Time: 4:10:12 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class Memc extends AbstractSLBCallbacks implements MemcLocal {
    @Resource
    private TimerService timerService;
    private static final int interval = 10000;
    final static Runtime RUNTIME = Runtime.getRuntime();
    private static final String MEMC_MILLIS_FREE = "MEMC{millis,free}:";
    private static final String STRING_OPEN_BRACE = "{";
    private static final String STRING_CLOSE_BRACE = "}";
    private static final String STRING_COMMA = ",";

    @Override
    public void logTime() {
        Loggers.INFO.info("HELLO, GOING AHEAD WITH CREATING THE TIMER.");
        Loggers.INFO.info("HELLO, STARTING TIMER NOW, WITH TIMER INTERVAL:" + interval);
        timerService.createTimer(0, interval, null);
        Loggers.INFO.info("HELLO, DONE CREATING THE TIMER.");
    }

    /**
     * Simple check to see and log if Out of Memory Occurs
     *
     * @param timer
     */
    @Timeout
    public void outOfMemoryCheck(final Timer timer) {
        final long free = RUNTIME.freeMemory();
        if (free / 1000 > 20) {
            Loggers.STATUS.info(MEMC_MILLIS_FREE + STRING_OPEN_BRACE + System.currentTimeMillis() + STRING_COMMA + free + STRING_CLOSE_BRACE);
        } else if (free / 1000 > 10) {
            Loggers.STATUS.warn(MEMC_MILLIS_FREE + STRING_OPEN_BRACE + System.currentTimeMillis() + STRING_COMMA + free + STRING_CLOSE_BRACE);
        } else {
            Loggers.STATUS.error(MEMC_MILLIS_FREE + STRING_OPEN_BRACE + System.currentTimeMillis() + STRING_COMMA + free + STRING_CLOSE_BRACE);
        }

    }
}

