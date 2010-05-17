package ai.ilikeplaces.logic.verify;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.Loggers;

import javax.annotation.Resource;
import javax.ejb.*;

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
        Loggers.INFO.info("MEMC:" + System.currentTimeMillis());
    }
}

