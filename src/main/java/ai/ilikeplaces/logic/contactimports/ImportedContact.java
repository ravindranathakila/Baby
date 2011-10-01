package ai.ilikeplaces.logic.contactimports;

import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumanEquals;
import ai.ilikeplaces.logic.validators.unit.HumanId;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 8/17/11
 * Time: 10:09 PM
 */
public class ImportedContact extends HumanEquals {

    private static final String EMPTY = "";
    String email;

    String fullName;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
    public ImportedContact setEmailR(final String email) {
        this.email = email;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFullNameAsEmptyIfNull() {
        return fullName != null ? fullName : EMPTY;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }
    public ImportedContact setFullNameR(final String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Override
    public String getHumanId() {
        return email;
    }

    public HumanId getAsHumanId() {
        return new HumanId(email);
    }
}
