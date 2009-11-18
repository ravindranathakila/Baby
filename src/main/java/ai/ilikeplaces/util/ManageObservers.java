package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

import java.util.Observer;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface ManageObservers {

    /**
     * @param o
     */
    public void addObserver(Observer o);

    /**
     * @param o
     */
    public void deleteObserver(Observer o);
}
