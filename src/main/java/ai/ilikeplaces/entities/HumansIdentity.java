package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import ai.ilikeplaces.util.ExceptionCache;

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
@DOCUMENTATION(
        FIXME = @FIXME("Performance Issue: " +
                "Takes upto 16ms" +
                "7087328  adimpression_ilikeplaces_war_1.6-SNAPSHOTPU  TRACE  [http-8080-3] openjpa.jdbc.SQL - <t 29370034, conn 0> executing prepstmnt 3799741 " +
                "SELECT t1.humanId, t1.clearance, t1.humanAlive, t2.humanId, t2.displayName, t3.humanId, t3.clearance, t3.humanAlive, t0.humansIdentityDateOfBirth, t0.humansIdentityEmail, t0.humansIdentityFirstName, t0.humansIdentityGUIPreferences, t0.humansIdentityGenderCode, t0.humansIdentityLastName, t0.humansIdentityProfilePhoto, t4.url, t4.metadata, t4.type FROM HumansIdentity t0 " +
                "LEFT OUTER JOIN Human t1 ON t0.humanId = t1.humanId " +
                "LEFT OUTER JOIN Url t4 ON t0.URL_URL = t4.url " +
                "LEFT OUTER JOIN HumansNet t2 ON t1.humanId = t2.humanId " +
                "LEFT OUTER JOIN Human t3 ON t2.humanId = t3.humanId WHERE t0.humanId = ? [params=(String) home1003@ilikeplaces.com] " +
                "Observed Problems:" +
                "1. Too many joins.")
)
@Entity
@NamedQueries({
        @NamedQuery(name = "FindPaginatedHumansByEmails",
                query = "SELECT hi FROM HumansIdentity hi WHERE hi.humanId IN(:humansIdentityEmails)")})

@EntityListeners({EntityLifeCycleListener.class})
public class HumansIdentity extends HumanEquals implements HumanPkJoinFace, Serializable {

    final static public String FindPaginatedHumansByEmails = "FindPaginatedHumansByEmails";
    final static public String HumansIdentityEmails = "humansIdentityEmails";
    private static final String HUMANS_IDENTITY = "HumansIdentity{";
    private static final String HUMAN_ID = "humanId='";
    private static final char CLOSING_BRACE = '}';

    public String humanId;
    public Human human;
    public Date humansIdentityDateOfBirth;
    public String humansIdentityProfilePhoto;
    public Url url;

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


    @Basic(fetch = FetchType.LAZY)
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getHumansIdentityDateOfBirth() {
        return humansIdentityDateOfBirth;
    }

    public void setHumansIdentityDateOfBirth(final Date humansIdentityDateOfBirth) {
        this.humansIdentityDateOfBirth = humansIdentityDateOfBirth;
    }


    public String getHumansIdentityProfilePhoto() {
        return humansIdentityProfilePhoto;
    }

    public void setHumansIdentityProfilePhoto(final String humansIdentityProfilePhoto) {
        this.humansIdentityProfilePhoto = humansIdentityProfilePhoto;
    }


    @OneToOne(cascade = CascadeType.ALL)
    public Url getUrl() {
        return url;
    }

    public void setUrl(final Url url) {
        this.url = url;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final HumansIdentity that = (HumansIdentity) o;

        return !(humanId != null ? !humanId.equals(that.humanId) : that.humanId != null);

    }

    @Override
    public int hashCode() {
        return humanId != null ? humanId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return HUMANS_IDENTITY +
                HUMAN_ID + humanId + '\'' +
                CLOSING_BRACE;
    }
}
