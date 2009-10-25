package ai.ilikeplaces.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import javax.lang.model.type.*;

/**
 * LogNull does not always log a null. instead, it outputs exceptions or dumps
 * so that you can figure out where exactly the null occurred.
 *
 * Nulls can always be checked by assigning return values to reference and
 * doing a == null check. Remember, having too many references in Java is very
 * bad practice (even for readability) unless you have a huge heap and an ultra
 * efficient GC.
 *
 * Even in an if else control structure, references are required.
 * So to use these methods, do ternary as
 * return returnVal =! null ? returnVal : (CAST)LogNull.logThrow();
 * which will either log and proceed(log()) or block(logThrow())
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class LogNull {

    final static private String MSG = "SORRY! A NPE OCCURRED. PLEASE CHECK THE CALLER OF THIS METHOD TO FIND OUT WHERE THE NPE ACTUALLY TOOK PLACE.";

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
    final static public NullType log() {
        Thread.dumpStack();
        return null;
    }

    /**
     * This stops the thread execution by throwing a NPE immediately
     *
     * @return Object
     */
    final static public NullType logThrow() {
        throw new NullPointerException(MSG);
    }
}
