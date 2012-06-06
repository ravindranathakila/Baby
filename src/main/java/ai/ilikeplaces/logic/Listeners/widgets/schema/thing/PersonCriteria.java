package ai.ilikeplaces.logic.Listeners.widgets.schema.thing;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 6/3/12
 * Time: 12:06 PM
 */
public class PersonCriteria {

    private String personName;
    private String personPhoto;

    public String getPersonName() {
        return personName;
    }

    public PersonCriteria setPersonName(String personName) {
        this.personName = personName;
        return this;
    }

    public String getPersonPhoto() {
        return personPhoto;
    }

    public PersonCriteria setPersonPhoto(String personPhoto) {
        this.personPhoto = personPhoto;
        return this;
    }
}
