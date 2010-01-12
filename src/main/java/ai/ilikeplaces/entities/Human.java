package ai.ilikeplaces.entities;

import ai.ilikeplaces.util.EntityLifeCyleListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Ravindranath Akila
 */
@Entity
@EntityListeners(EntityLifeCyleListener.class)
public class Human implements HumanIdFace, Serializable {

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


    @Override
    public String toString() {
        return "Human{" +
                "humanId='" + humanId + '\'' +
                ", humanAlive=" + humanAlive +
                '}';
    }
}
