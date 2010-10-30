package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

import static ai.ilikeplaces.util.Loggers.LEVEL;

/**
 * Lets try make a smart logger.
 * <p/>
 * Intention:
 * Get a start message such as "Shutting down system"
 * Wait for a confirmation such as "Done"
 * If no confirmation within a specific time(sent with the message) print "Possible failure, See logs".
 * <p/>
 * Requirements:
 * Accept a logger
 * Accept a logger severity
 * <p/>
 * if you find any bugs, please notify somebody at ilikeplaces.com
 * <p/>
 * I did this for fun when noticing how Mandriva Linux(then again all linux) was shutting down.
 * The status messages goes as "Stopping SASL.... [FAILED]"... "Stopping MySQL... [done]".
 * This got me thinking, logging is not about just notifying. It is about reporting a process.
 * Hence, this smart logger is to facilitate a start and time out, completion or delayed completion
 * of a process
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: May 29, 2010
 * Time: 8:28:17 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class SmartLogger extends Thread {

    final Loggers.LEVEL level;
    String logmsg;
    final long sleep;
    long starTime = -1;//WARNING: DO NOT change this default value as it is used in IF/TERNARY conditions.

    boolean logged = false;

    boolean discarded = false;

    /*The following string constants might look odd but we do not want any delays in a logger. Macro optimization!!*/
    private static final String CAUSE_UNRESPONSIVE_IN_MILLIS = "{} <= cause UNRESPONSIVE in millis:";
    private static final String SORRY_I_FAILED_TO_LOG_THIS_MESSAGE = "SORRY! I FAILED TO LOG THIS MESSAGE:";
    private static final String CAUSE_RECOVERED_WITH_STATUS = " <= cause RECOVERED with status:";
    private static final String TIME_TAKEN = "[Time Taken:";
    private static final String CLOSE_SQUARE_BRACKET = "]";
    private static final String COLON = ":";
    private static final String EMPTY = "";
    private static final String PIPE = "|";
    protected static final String CALL_TO_A_DISCARDED_LOGGER = "Call to a discarded logger!";
    protected static final String LOGGER_DISCARDED_TWICE = "Logger Discarded Twice";

    /**
     * Leaving optional parameters as null will simply ignore them.
     * You can specify any number of optinal parameters as indicated by the underscore.
     * At the time of commenting, the optional value startMsg_calcExecTime means,
     * you can give a startMsg(you guessed right, a string) and a calcExecTime(boolean).
     * Leaving any null will make the program simply ignore the value.
     *
     * @param logLevel
     * @param logMessage
     * @param timeout
     * @param startMsg_calcExecTime
     */
    public SmartLogger(final LEVEL logLevel, final String logMessage, final long timeout, final Object... startMsg_calcExecTime) {
        if (startMsg_calcExecTime.length == 2) {  //Placing this IF above as we need to log time asap.
            if (startMsg_calcExecTime[1] != null) {
                if ((Boolean) startMsg_calcExecTime[1]) {
                    starTime = System.currentTimeMillis();
                }
            }
            if (startMsg_calcExecTime[0] != null) {
                Loggers.log(logLevel, Loggers.EMBED, startMsg_calcExecTime[0]);
            }
        } else if (startMsg_calcExecTime.length == 1) {
            if (startMsg_calcExecTime[0] != null) {
                Loggers.log(logLevel, Loggers.EMBED, startMsg_calcExecTime[0]);
            }
        }

        this.logmsg = logMessage;
        this.sleep = timeout;
        this.level = logLevel;

        this.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Loggers.log(LEVEL.ERROR, "SORRY! I POSSIBLY FAILED TO LOG:" + logMessage, e);
            }
        });

        start();
    }

    @Override
    public void run() {
        try {
            if (sleep != 0) {//Sleep 0 avoids timeout. This is when this object will wait till task "complete"
                Thread.sleep(sleep);
                if (!status() && !discarded) {
                    Loggers.log(level, CAUSE_UNRESPONSIVE_IN_MILLIS + sleep, logmsg);
                }
            }
        } catch (final InterruptedException e) {
            Loggers.log(LEVEL.ERROR, SORRY_I_FAILED_TO_LOG_THIS_MESSAGE + logmsg, e);
        }
    }

    /**
     * Discards this logger and ignores all future logging
     *
     * @return discard status
     */
    public boolean trash() {
        if (discarded ? discarded : !(discarded = true)) {
            Loggers.WARN.warn(LOGGER_DISCARDED_TWICE);
        }
        return discarded;
    }

    private synchronized boolean status() {
        return !(logged = !logged);//hehe, tricky. :D
    }

    /**
     * Returns the time taken to execute task, or empty string, if calculations disabled
     *
     * @return
     */
    private String timeTaken() {
        return (starTime == -1) ? EMPTY : TIME_TAKEN + (System.currentTimeMillis() - starTime + CLOSE_SQUARE_BRACKET);
    }

    /**
     * Leaving optional parameters as null will simply ignore them.
     * You can specify any number of optinal parameters as indicated by the underscore.
     * At the time of commenting, the optional value startMsg_calcExecTime means,
     * you can give a startMsg(you guessed right, a string) and a calcExecTime(boolean).
     * Leaving any null will make the program simply ignore the value.
     *
     * @param logLevel
     * @param logMessage
     * @param timeout
     * @param startMsg_calcExecTime
     * @return
     */
    static public SmartLogger start(final LEVEL logLevel, final String logMessage, final long timeout, final Object... startMsg_calcExecTime) {
        return new SmartLogger(logLevel, logMessage, timeout, startMsg_calcExecTime);
    }

    public void appendToLogMSG(final String stringToBeAppended) {
        if (discarded) {
            Loggers.WARN.warn(CALL_TO_A_DISCARDED_LOGGER);
            return;
        }

        logmsg += PIPE + stringToBeAppended;
    }

    static public void complete(final SmartLogger smartLogger, final LEVEL completeLevel, final String completeStatus) {

        smartLogger.complete(completeLevel, completeStatus);
    }

    static public void complete(final SmartLogger smartLogger, final String completeStatus) {
        smartLogger.complete(completeStatus);
    }

    /**
     * @param completeLevel  Log Level
     * @param completeStatus Throwable type needed in case of ERROR
     */
    public void complete(final LEVEL completeLevel, final Object completeStatus) {
        if (trash()) {
            return;
        }
        if (!status()) {
            Loggers.log(completeLevel, Loggers.EMBED + COLON + completeStatus + timeTaken(), logmsg);
        } else {
            Loggers.log(completeLevel, Loggers.EMBED + CAUSE_RECOVERED_WITH_STATUS + COLON + completeStatus + timeTaken(), logmsg);
        }
    }

    public void multiComplete(final LEVEL[] completeLevels, final Object completeStatus) {
        if (trash()) {
            return;
        }
        if (!status()) {
            for (final LEVEL completeLevel : completeLevels) {
                Loggers.log(completeLevel, Loggers.EMBED + COLON + completeStatus + timeTaken(), logmsg);
            }
        } else {
            for (final LEVEL completeLevel : completeLevels) {
                Loggers.log(completeLevel, Loggers.EMBED + CAUSE_RECOVERED_WITH_STATUS + COLON + completeStatus + timeTaken(), logmsg);
            }
        }
    }

    public void complete(final String completeStatus) {
        if (trash()) {
            return;
        }
        if (!status()) {
            Loggers.log(level, Loggers.EMBED + COLON + completeStatus + timeTaken(), logmsg);
        } else {
            Loggers.log(level, Loggers.EMBED + CAUSE_RECOVERED_WITH_STATUS + completeStatus + COLON + timeTaken(), logmsg);
        }
    }
}
