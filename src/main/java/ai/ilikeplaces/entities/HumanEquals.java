package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.util.Loggers;

/**
 * This class is to be used with the equals method in objects.
 * Objects conforming to this class should LASTLY rely on this getter,
 * to check for equality.
 * <p/>
 * Remember that this class only compares Objects of different types for equality.
 * Comparing objects of the same type with this approach is wrong.
 * Therefore, use this if the objects are of different classes.
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 3, 2010
 * Time: 4:15:31 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public abstract class HumanEquals {
    private static final UnsupportedOperationException UNSUPPORTED_OPERATION_EXCEPTION = new UnsupportedOperationException("SORRY! YOU ARE COMPARING OBJECTS OF THE SAME CLASS IN THIS METHOD, WHICH IS NOT CORRECT.");
    private static final UnsupportedOperationException NOT_EXTEND = new UnsupportedOperationException("SORRY! THE OTHER OBJECT DOES NOT EXTEND HumanEquals.");

    abstract public String getHumanId();

    /**
     * This method is advisable to be called upon falsified conditions in the equals method.
     * <p/>
     * First check if the entities are of the same class, if true, check internal params.
     * <p/>
     * If false, check with this method.
     * <p/>
     * Always use this convention. Otherwise you might introduce bugs
     *
     * @param other
     * @return if HumanIds of the two objects match
     */
    public boolean matchHumanId(final Object other) {
        Loggers.DEBUG.debug(this.getClass().getSimpleName() + " " + other.getClass().getSimpleName());
        if (this.getClass() == other.getClass()) {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }
        //Warning: added later. check for sanity with existing code
        if (!(other instanceof HumanEquals)) {
            throw NOT_EXTEND;
        }
//        return other instanceof HumanEquals && this.getHumanId().equals(((HumanEquals) other).getHumanId());
        return this.getHumanId().equals(((HumanEquals) other).getHumanId());
    }

    /**
     * New approach on HumanEquals but not tested. Check intensively if you are using this static approach.
     * 
     * @param main
     * @param other
     * @return
     */
    @Deprecated
    @NOTE(note = "Use an interface based equals matching approach if you are using this. Logic is" +
            "If the two classes of of different types, but instanceof same interface, then match humanid." +
            "This approach avoids necessity to extend a class.")
    static public boolean staticMatchHumanId(final HumanEquals main, final Object other) {
        //Loggers.DEBUG.debug(main.getClass().getSimpleName() + " " + other.getClass().getSimpleName());
        if (main.getClass() == other.getClass()) {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }
        if (!(other instanceof HumanEquals)) {
            throw NOT_EXTEND;
        }
        return main.getHumanId().equals(((HumanEquals) other).getHumanId());
    }


}