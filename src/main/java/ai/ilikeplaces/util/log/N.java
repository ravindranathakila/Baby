package ai.ilikeplaces.util.log;

import ai.ilikeplaces.util.LogNull;

import java.lang.ref.SoftReference;

/**
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 21/11/12
 * Time: 11:57 PM
 */
public class N<OBJECT> extends LogNull {
    final private SoftReference<OBJECT> n;

    public N(final OBJECT objectToBeCheckedForNull) {
        this.n = new SoftReference<OBJECT>(objectToBeCheckedForNull);
        logThrow(this.n.get());
    }

    public OBJECT n() {
        return (OBJECT) logThrow(this.n.get());
    }
}
