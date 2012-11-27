package ai.ilikeplaces.util;

import java.lang.ref.WeakReference;

/**
 * Because it sucks to keep checking for nulls as
 * <p/>
 * <code>                                           <br/>
 * if (objectToBeCheckedForNull == null) {          <br/>
 * &nbsp;&nbsp;throw new NullPointerException(MSG);  <br/>
 * } else {                                         <br/>
 * &nbsp;&nbsp;return objectToBeCheckedForNull;      <br/>
 * }                                                <br/>
 * </code>                                          <br/>
 * <p/>
 * Why do we need to check for nulls as facilitated in this class? The reason is that a null should be detected as early
 * in the code as possible to figure out where it occurred. The later it is discovered, the harder it is to find the origin.
 * <p/>
 * This class was designed to be as less obstructive as possible to the utilizing code, while avoiding casting.
 * <p/>
 * Why a non-capitalized name? Well, we don't want instances of this class assigned to new variables.
 * <p/>
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 21/11/12
 * Time: 11:57 PM
 */
public class n<OBJECT> {
    public static final String MSG = "An object expected not to be null was found so. You will have to go through this stack trace to find out where. ";
    final private WeakReference<OBJECT> n;

    public n(final OBJECT objectToBeCheckedForNull) {
        this.n = new WeakReference<OBJECT>(objectToBeCheckedForNull);
        n(this.n.get());
    }

    public OBJECT n() {
        final OBJECT object = this.n.get();
        if (object == null) {
            throw new NullPointerException(MSG);
        }
        return object;
    }

    static public Object n(final Object objectToBeCheckedForNull) {
        if (objectToBeCheckedForNull == null) {
            throw new NullPointerException(MSG);
        } else {
            return objectToBeCheckedForNull;
        }
    }
}
