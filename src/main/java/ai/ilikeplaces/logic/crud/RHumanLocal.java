package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Human;
import javax.ejb.Local;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RHumanLocal {

    public Human doRHuman(String humandId);

    public Human doDirtyRHuman(String humanId);
}