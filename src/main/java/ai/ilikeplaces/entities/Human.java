package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.EntityLifeCyleListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners(EntityLifeCyleListener.class)
public class Human implements HumanIdFace, Serializable, Clearance {

    private String humanId;

    /**
     * If the user is not active on the site, he is considered dead and the
     * account shall be false under humanAlive.
     * The motivation is to get all humansAlive to be true on ilikeplaces.com:-)
     * Important: This is also the switch for the privacy policy.
     */
    private Boolean humanAlive;

    private Long clearance = 0L;


    private HumansAuthentication humansAuthentication;
    private HumansIdentity humansIdentity;
    private HumansPublicPhoto humansPublicPhoto;
    private HumansPrivatePhoto HumansPrivatePhoto;
    private HumansNet humansNet;
    private HumansPrivateLocation humansPrivateLocation;
    private HumansPrivateEvent humansPrivateEvent;
    private HumansAlbum humansAlbum;

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

    @Override
    public Long getClearance() {
        return clearance;
    }

    @Override
    public void setClearance(final Long clearance) {
        this.clearance = clearance;
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


    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public HumansPrivateLocation getHumansPrivateLocation() {
        return humansPrivateLocation;
    }

    public void setHumansPrivateLocation(final HumansPrivateLocation humansPrivateLocation) {
        this.humansPrivateLocation = humansPrivateLocation;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public HumansPrivateEvent getHumansPrivateEvent() {
        return humansPrivateEvent;
    }

    public void setHumansPrivateEvent(final HumansPrivateEvent humansPrivateEvent) {
        this.humansPrivateEvent = humansPrivateEvent;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public HumansAlbum getHumansAlbum() {
        return humansAlbum;
    }

    public void setHumansAlbum(HumansAlbum humansAlbum) {
        this.humansAlbum = humansAlbum;
    }

    @Override
    public String toString() {
        return "Human{" +
                "humanId='" + humanId + '\'' +
                ", clearance=" + clearance +
                ", humanAlive=" + humanAlive +
                '}';
    }
}
