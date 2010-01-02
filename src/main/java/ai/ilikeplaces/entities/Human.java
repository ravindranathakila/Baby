package ai.ilikeplaces.entities;

import ai.ilikeplaces.util.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import org.slf4j.LoggerFactory;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author Ravindranath Akila
 */
@Entity
@EntityListeners(EntityLifeCyleListener.class)
public class Human implements Serializable {

//    public static final Integer genderCodeNeutral = 0;
//    public static final Integer genderCodeMale = 1;
//    public static final Integer genderCodeFemale = 2;
    private String humanId;
    /**
     * If the user is not active on the site, he is considered dead and the
     * account shall be false under humanAlive.
     * The motivation is to get all humansAlive to be true on ilikeplaces.com:-)
     * Important: This is also the switch for the privacy policy.
     */
    private Boolean humanAlive;
    private HumansAuthentication humansAuthentication;
    private HumansIdentity humansIdentity;
    private HumansPublicPhoto humansPublicPhoto;
    private HumansPrivatePhoto HumansPrivatePhoto;
    private HumansNet humansNet;

    @Id
    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    public Boolean getHumanAlive() {
        return humanAlive;
    }

    public void setHumanAlive(final Boolean humanAlive) {
        this.humanAlive = humanAlive;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public HumansAuthentication getHumansAuthentications() {
        return humansAuthentication;
    }

    public void setHumansAuthentications(final HumansAuthentication humansAuthentications) {
        this.humansAuthentication = humansAuthentications;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public HumansIdentity getHumansIdentity() {
        return humansIdentity;
    }

    public void setHumansIdentity(final HumansIdentity humansIdentity) {
        this.humansIdentity = humansIdentity;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public HumansPrivatePhoto getHumansPrivatePhoto() {
        return HumansPrivatePhoto;
    }

    public void setHumansPrivatePhoto(HumansPrivatePhoto HumansPrivatePhoto) {
        this.HumansPrivatePhoto = HumansPrivatePhoto;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public HumansPublicPhoto getHumansPublicPhoto() {
        return humansPublicPhoto;
    }

    public void setHumansPublicPhoto(HumansPublicPhoto humansPublicPhoto) {
        this.humansPublicPhoto = humansPublicPhoto;
    }
    
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public HumansNet getHumansNet() {
        return humansNet;
    }

    public void setHumansNet(final HumansNet humansNet) {
        this.humansNet = humansNet;
    }

    /**
     *
     * @return toString_
     */
    @Override
    public String toString() {
        String toString_ = new String(getClass().getName());
        try {
            final Field[] fields = {getClass().getDeclaredField("humanId")};

            for (final Field field : fields) {
                try {
                    toString_ += "\n{" + field.getName() + "," + field.get(this) + "}";
                } catch (IllegalArgumentException ex) {
                    LoggerFactory.getLogger(Location.class.getName()).error( null, ex);
                } catch (IllegalAccessException ex) {
                    LoggerFactory.getLogger(Location.class.getName()).error( null, ex);
                }
            }
        } catch (NoSuchFieldException ex) {
            LoggerFactory.getLogger(Location.class.getName()).error( null, ex);
        } catch (SecurityException ex) {
            LoggerFactory.getLogger(Location.class.getName()).error( null, ex);
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
        changeLog += "20090914 Initial \n";
        changeLog += "20090918 Added enum for user gender and many other variables \n";
        return showChangeLog__ ? changeLog : toString();
    }
}
