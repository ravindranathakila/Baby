package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.rbs.RBGet;

/**
 * LogNull does not always log a null. instead, it outputs exceptions or dumps
 * so that you can figure out where exactly the null occurred.
 * <p/>
 * Nulls can always be checked by assigning return values to reference and
 * doing a == null check. Remember, having too many references in Java is very
 * bad practice (even for readability) unless you have a huge heap and an ultra
 * efficient GC.
 * <p/>
 * Even in an if else control structure, references are required.
 * So to use these methods, do ternary as
 * return returnVal =! null ? returnVal : (CAST)LogNull.logThrow();
 * which will either log and proceed(log()) or block(logThrow())
 *
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class LogNull {

    @NOTE(note = "ASSIGNING VALUE TO STATIC STRING TO GAIN SPEED. THESE METHODS SHOULD WORK FAST TO FACILITATE THEIR PURPOSE.")
    final static private String MSG = RBGet.expMsgs.getString("ai.ilikeplaces.util.LogNull.0001");

    private LogNull() {
    }

    /**
     * Null is returned to the caller and will reflect elsewhere.
     * Here, the thread stack will be dumped to the console. if the caller
     * uses the returned null much later, calling this will not help you find
     * out the bug in a timely manner. Therefore, in such instances, and maybe
     * always, use logThrow()
     *
     * @return Object
     */
    static public Object log() {
        Thread.dumpStack();
        return null;
    }

    /**
     * This stops the thread execution by throwing a NPE immediately
     *
     * @return Object
     */
    static public Object logThrow() {
        throw new NullPointerException(MSG);
    }

    /**
     * This stops the thread execution by throwing a NPE immediately
     *
     * @return Object
     */
    static public Object logThrow(final String customMsg__) {
        throw new NullPointerException(MSG + "\n" + customMsg__);
    }

    /**
     * This stops the thread execution by throwing a NPE immediately if the
     * given parameter is null
     *
     * @return Object
     */
    static public Object logThrow(final Object obj) {
        if (obj == null) {
            throw new NullPointerException(MSG);
        } else {
            return obj;
        }
    }
    /**
     * This stops the thread execution by throwing a NPE immediately if the
     * given parameter is null
     *
     * @return Object
     */
    static public Object logThrow(final Object obj, final String causeOfNpe) {
        if (obj == null) {
            throw new NullPointerException(causeOfNpe);
        } else {
            return obj;
        }
    }
}
