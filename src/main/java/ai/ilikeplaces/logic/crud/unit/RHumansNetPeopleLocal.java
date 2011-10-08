package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansNetPeople;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */


@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RHumansNetPeopleLocal {

    public HumansNetPeople doDirtyRHumansNetPeople(String humanId);

    public HumansNetPeople doRHumansNetPeople(String humanId);

    public List<HumansNetPeople> doDirtyRHumansBefriends(String humanId);
}