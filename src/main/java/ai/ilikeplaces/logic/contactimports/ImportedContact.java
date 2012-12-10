package ai.ilikeplaces.logic.contactimports;

import ai.ilikeplaces.entities.etc.HumanEquals;
import ai.util.HumanId;

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

    public ImportedContact setEmail(final String email) {
        this.email = email;
        return this;
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

    public ImportedContact setFullName(final String fullName) {
        this.fullName = fullName;
        return this;
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
