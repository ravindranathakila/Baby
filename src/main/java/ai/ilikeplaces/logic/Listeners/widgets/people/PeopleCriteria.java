package ai.ilikeplaces.logic.Listeners.widgets.people;

import ai.ilikeplaces.entities.HumanIdFace;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.logic.validators.unit.HumanId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/1/12
 * Time: 12:48 PM
 */
public class PeopleCriteria {
    private List<HumanIdFace> people = null;

    public PeopleCriteria setPeople(List<HumanIdFace> people) {
        this.people = people;
        return this;
    }

    public List<HumanIdFace> getPeople() {
        return people != null ? people : (people = new ArrayList<HumanIdFace>(0));
    }
}
