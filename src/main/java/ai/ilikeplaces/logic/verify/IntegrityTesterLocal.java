package ai.ilikeplaces.logic.verify;

import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface IntegrityTesterLocal {
    public boolean gc();
}
