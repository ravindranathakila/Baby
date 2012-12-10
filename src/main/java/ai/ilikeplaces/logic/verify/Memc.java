package ai.ilikeplaces.logic.verify;

import ai.doc.License;
import ai.doc._fix;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.mail.SendMailLocal;
import ai.ilikeplaces.management.MemorySafe;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.SmartLogger;
import ai.util.Return;

import javax.annotation.Resource;
import javax.ejb.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Apr 29, 2010
 * Time: 4:10:12 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class Memc extends AbstractSLBCallbacks implements MemcLocal {
    @Resource
    private TimerService timerService;

    @EJB
    private SendMailLocal sendMailLocal;

    private static final int interval = 10000;
    private final static Runtime RUNTIME = Runtime.getRuntime();
    private static final String MEMC_MILLIS_FREE = "MEMC{millis,free}:";
    private static final String STRING_OPEN_BRACE = "{";
    private static final String STRING_CLOSE_BRACE = "}";
    private static final String STRING_COMMA = ",";
    private long lastWarningMail = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
    private long lastStatusMail = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
    private static final long MB = 1024 * 1024;
    private static final String NOTIFY_MAIL = "ravindranathakila@gmail.com";

    @_fix(issue = "SimpleDateFormat is concurrent safe?")
    private final static SimpleDateFormat sdf = new SimpleDateFormat("G-yyyy-MM-dd-HH:mm(ss)");
    private static final int MINUTES_10 = Integer.parseInt(RBGet.globalConfig.getString("SERVER_MEMORY_MAIL"));
    private static final int HOURS_3 = Integer.parseInt(RBGet.globalConfig.getString("SERVER_LOW_MEMORY_MAIL"));
    private static final int LOW_MEMORY_LIMIT = Integer.parseInt(RBGet.globalConfig.getString("LOW_MEMORY_THRESHOLD"));
    private static final String STRING_PERCENTAGE = "%";

    /**
     * This method is called manually to start the timer
     */
    @Override
    public void logTime() {
        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, Loggers.CODE_MEMC +
                "HELLO, STARTING MEMC TIMER  WITH TIMER INTERVAL:" + interval,
                60000,
                null,
                true);
        timerService.createTimer(0, interval, null);
        sl.complete(Loggers.LEVEL.INFO, Loggers.DONE);
    }

    public static void sendAlertMail(final String status) {
        try {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    sendEmail:
                    {
                        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.INFO, Loggers.CODE_MEMC + "Mailing status report to " + NOTIFY_MAIL, 60000, null, true);
                        final Return<Boolean> r = SendMail.getSendMailLocal().sendAsSimpleText(NOTIFY_MAIL, status, getReport().toString());
                        if (r.returnStatus() == 0 && r.returnValue()) {
                            sl.complete(Loggers.LEVEL.INFO, "done.");
                        } else {
                            sl.complete(Loggers.LEVEL.ERROR, r.returnError());
                        }
                    }

                }
            }).start();
        } catch (final Exception t) {
            Loggers.STATUS.error("SORRY! AN ERROR OCCURRED WHILE TRYING TO SEND ALERT MAIL.", t);
        }
    }

    public static StringBuilder getReport() {
        final StringBuilder report = new StringBuilder("");
        report
                .append("Max Memory:").append(RUNTIME.maxMemory() / MB).append("\n")
                .append("Total Memory:").append(RUNTIME.totalMemory() / MB).append("\n")
                .append("Used Memory:").append(RUNTIME.totalMemory() - RUNTIME.freeMemory() / MB).append("\n")
                .append("Free Memory:").append(RUNTIME.freeMemory() / MB).append("\n").append("\n\n")
                .append("Report Generated At:").append(sdf.format(new Date(System.currentTimeMillis())));
        return report;
    }

    /**
     * Simple check to see and log if Out of Memory Occurs
     * synchronized to avoid concurrent calls with delayed timers
     *
     * @param timer
     */
    @Timeout
    synchronized public void outOfMemoryCheck(final Timer timer) {
        final long free = RUNTIME.freeMemory();
        final long total = RUNTIME.totalMemory();
        final long now = System.currentTimeMillis();

        if ((free / MB) < LOW_MEMORY_LIMIT) {
            warningMail:
            {
                if ((now - lastWarningMail) > MINUTES_10) {
                    lastWarningMail = now;
                    sendAlertMail("ILPREPORT - LOW MEMORY AT I LIKE PLACES");
                }
            }
            Loggers.STATUS.warn(MEMC_MILLIS_FREE + STRING_OPEN_BRACE
                    + (double) free / MemorySafe.MB + STRING_OPEN_BRACE + ((double) free / (double) total) * 100 + STRING_PERCENTAGE + STRING_CLOSE_BRACE + STRING_COMMA
                    + (double) total / MemorySafe.MB + STRING_CLOSE_BRACE);
        } else {
            Loggers.STATUS.info(MEMC_MILLIS_FREE + STRING_OPEN_BRACE
                    + (double) free / MemorySafe.MB + STRING_OPEN_BRACE + ((double) free / (double) total) * 100 + STRING_PERCENTAGE + STRING_CLOSE_BRACE + STRING_COMMA
                    + (double) total / MemorySafe.MB + STRING_CLOSE_BRACE);

        }

        statusMail:
        {
            if ((now - lastStatusMail) > HOURS_3) {
                lastStatusMail = now;
                sendAlertMail("ILPREPORT - Monitor Report of I Like Places");
            }
        }

    }
}

