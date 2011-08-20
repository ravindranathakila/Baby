package ai.ilikeplaces.logic.contactimports;

import ai.ilikeplaces.entities.HumanEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 8/17/11
 * Time: 10:09 PM
 */
public class ImportedContact extends HumanEquals {

    String email;

    String fullName;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getHumanId() {
        return email;
    }
}
