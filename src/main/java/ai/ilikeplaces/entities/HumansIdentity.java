package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Social networking relies on a so called psychological aspect of "identity",
 * thus the class name. Unless a user is given the opportunity to express his
 * identity on a social network, it will not be successful.
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners(EntityLifeCycleListener.class)
@NamedQueries({
        @NamedQuery(name = "FindPaginatedHumansByEmails",
                query = "SELECT hi FROM HumansIdentity hi WHERE hi.humansIdentityEmail IN(:humansIdentityEmails)")})
public class HumansIdentity implements HumanPkJoinFace, Serializable {

    final static public String FindPaginatedHumansByEmails = "FindPaginatedHumansByEmails";
    final static public String HumansIdentityEmails = "humansIdentityEmails";

    public String humanId;
    public Human human;
    public String humansIdentityEmail;
    public Integer humansIdentityGenderCode;
    public Date humansIdentityDateOfBirth;
    public String humansIdentityFirstName;
    public String humansIdentityLastName;
    public String humansIdentityGUIPreferences;
    public String humansIdentityProfilePhoto;

    public static enum GENDER {
        Neutral,
        Male,
        Female;

        final static public int getGenderCode(final GENDER gender) {
            return getGenderCode(gender.toString());
        }

        final static public int getGenderCode(final String gender) {

            if (gender.equals("Male")) return 1;
            if (gender.equals("Female")) return 1;
            if (gender.equals("Neutral")) return 1;

            throw new IllegalArgumentException("SORRY! " + gender + " IS NOT A VALID ARGUMENT.");
        }
    }

    @Id
    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    @OneToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
    public Human getHuman() {
        return human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }

    public String getHumansIdentityFirstName() {
        return humansIdentityFirstName;
    }

    public void setHumansIdentityFirstName(String humansIdentityFirstName) {
        this.humansIdentityFirstName = humansIdentityFirstName;
    }

    public String getHumansIdentityLastName() {
        return humansIdentityLastName;
    }

    public void setHumansIdentityLastName(String humansIdentityLastName) {
        this.humansIdentityLastName = humansIdentityLastName;
    }

    public String getHumansIdentityEmail() {
        return humansIdentityEmail;
    }

    public void setHumansIdentityEmail(final String humansIdentityEmail) {
        this.humansIdentityEmail = humansIdentityEmail;
    }

    public Integer getHumansIdentityGenderCode() {
        return humansIdentityGenderCode;
    }

    public void setHumansIdentityGenderCode(final Integer humansIdentityGenderCode) {
        this.humansIdentityGenderCode = humansIdentityGenderCode;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getHumansIdentityDateOfBirth() {
        return humansIdentityDateOfBirth;
    }

    public void setHumansIdentityDateOfBirth(final Date humansIdentityDateOfBirth) {
        this.humansIdentityDateOfBirth = humansIdentityDateOfBirth;
    }

    public String getHumansIdentityGUIPreferences() {
        return humansIdentityGUIPreferences;
    }

    public void setHumansIdentityGUIPreferences(final String humansIdentityGUIPreferences) {
        this.humansIdentityGUIPreferences = humansIdentityGUIPreferences;
    }

    public String getHumansIdentityProfilePhoto() {
        return humansIdentityProfilePhoto;
    }

    public void setHumansIdentityProfilePhoto(final String humansIdentityProfilePhoto) {
        this.humansIdentityProfilePhoto = humansIdentityProfilePhoto;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final HumansIdentity that = (HumansIdentity) o;

        if (humanId != null ? !humanId.equals(that.humanId) : that.humanId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return humanId != null ? humanId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HumansIdentity{" +
                "humanId='" + humanId + '\'' +
                ", humansIdentityFirstName='" + humansIdentityFirstName + '\'' +
                ", humansIdentityLastName='" + humansIdentityLastName + '\'' +
                '}';
    }
}
