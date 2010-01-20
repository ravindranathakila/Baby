package ai.ilikeplaces.entities;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;

/**
 * Social networking relies on a so called psychological aspect of "identity",
 * thus the class name. Unless a user is given the opportunity to express his
 * identity on a social network, it will not be successful. 
 *
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
public class HumansIdentity implements HumanPkJoinFace, Serializable {

    final static Logger logger = LoggerFactory.getLogger(HumansIdentity.class.getName());
    private static final long serialVersionUID = 1L;
    private String humanId;
    private Human human;
    private String humansIdentityEmail;
    private Integer humansIdentityGenderCode;
    private Date humansIdentityDateOfBirth;
    private String humansIdentityGUIPreferences;

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

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof HumansIdentity)) {
//            return false;
//        }
//        HumansIdentity other = (HumansIdentity) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
    /**
     *
     * @return toString_
     */
    @Override
    public String toString() {
        String toString_ = new String(getClass().getName());
        try {
            final Field[] fields = {getClass().getDeclaredField("locationId")};

            for (final Field field : fields) {
                try {
                    toString_ += "\n{" + field.getName() + "," + field.get(this) + "}";
                } catch (IllegalArgumentException ex) {
                    logger.info( null, ex);
                } catch (IllegalAccessException ex) {
                    logger.info( null, ex);
                }
            }
        } catch (NoSuchFieldException ex) {
            logger.info( null, ex);
        } catch (SecurityException ex) {
            logger.info( null, ex);
        }

        return toString_;
    }

    /**
     *
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = new String(toString() + "\n");
        changeLog += "20090925 new \n";
        return showChangeLog__ ? changeLog : toString();
    }
}
