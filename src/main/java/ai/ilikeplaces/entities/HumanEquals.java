package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
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
 * User: Ravindranath Akila
 * Date: Jan 3, 2010
 * Time: 4:15:31 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public abstract class HumanEquals {

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
            throw new UnsupportedOperationException("SORRY! YOU ARE COMPARING OBECTS OF THE SAME CLASS IN THIS METHOD, WHICH IS NOT CORRECT.");
        }
        return other instanceof HumanEquals && this.getHumanId().equals(((HumanEquals) other).getHumanId());
    }
}