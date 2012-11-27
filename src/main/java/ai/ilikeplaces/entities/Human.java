package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.exception.DBException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import ai.ilikeplaces.util.Return;

import javax.persistence.*;
import java.io.*;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "Human", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class Human extends HumanEquals implements HumanIdFace, Serializable, Clearance, HumansFriend, Cloneable {
// ------------------------------ FIELDS ------------------------------

    @Id
    @Column(name = "humanId")
    public String humanId;

    /**
     * If the user is not active on the site, he is considered dead and the
     * account shall be false under humanAlive.
     * The motivation is to get all humansAlive to be true on ilikeplaces.com:-)
     * Important: This is also the switch for the privacy policy.
     */
    @Column(name = "humanAlive")
    public Boolean humanAlive;

    @Column(name = "clearance")
    public Long clearance = 0L;

    //@PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public HumansAuthentication humansAuthentication;

    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@PrimaryKeyJoinColumn
    public HumansIdentity humansIdentity;

    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@PrimaryKeyJoinColumn
    public HumansPublicPhoto humansPublicPhoto;

    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@PrimaryKeyJoinColumn
    public HumansPrivatePhoto HumansPrivatePhoto;

    @NOTE(note = "HumansNet is a simple entity with no List based getters and setters.")
    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@PrimaryKeyJoinColumn
    public HumansNet humansNet;

    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@PrimaryKeyJoinColumn
    public HumansPrivateLocation humansPrivateLocation;

    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@PrimaryKeyJoinColumn
    public HumansPrivateEvent humansPrivateEvent;


    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@PrimaryKeyJoinColumn
    public HumansAlbum humansAlbum;

    @WARNING(warning = "DO NOT fetch eager. Wall will pull all the damn messages eager.")
    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@PrimaryKeyJoinColumn
    public HumansWall humansWall;

// --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public Long getClearance() {
        return clearance;
    }

    @Override
    public void setClearance(final Long clearance) {
        this.clearance = clearance;
    }

    public Boolean getHumanAlive() {
        return humanAlive;
    }

    public void setHumanAlive(final Boolean humanAlive) {
        this.humanAlive = humanAlive;
    }

    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    public HumansAlbum getHumansAlbum() {
        return humansAlbum;
    }

    public void setHumansAlbum(HumansAlbum humansAlbum) {
        this.humansAlbum = humansAlbum;
    }

    public HumansIdentity getHumansIdentity() {
        return humansIdentity;
    }

    public void setHumansIdentity(final HumansIdentity humansIdentity) {
        this.humansIdentity = humansIdentity;
    }

    public HumansNet getHumansNet() {
        return humansNet;
    }

    public void setHumansNet(final HumansNet humansNet) {
        this.humansNet = humansNet;
    }

    public HumansPrivateEvent getHumansPrivateEvent() {
        return humansPrivateEvent;
    }

    public void setHumansPrivateEvent(final HumansPrivateEvent humansPrivateEvent) {
        this.humansPrivateEvent = humansPrivateEvent;
    }

    public HumansPrivateLocation getHumansPrivateLocation() {
        return humansPrivateLocation;
    }

    public void setHumansPrivateLocation(final HumansPrivateLocation humansPrivateLocation) {
        this.humansPrivateLocation = humansPrivateLocation;
    }

    public HumansPublicPhoto getHumansPublicPhoto() {
        return humansPublicPhoto;
    }

    public void setHumansPublicPhoto(HumansPublicPhoto humansPublicPhoto) {
        this.humansPublicPhoto = humansPublicPhoto;
    }

    public HumansWall getHumansWall() {
        return humansWall;
    }

    public void setHumansWall(final HumansWall humansWall) {
        this.humansWall = humansWall;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    @Transient
    protected Object clone() throws CloneNotSupportedException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Human) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null) return false;

        if (getClass() == o.getClass()) {
            final Human that = (Human) o;
            return (!(this.getHumanId() == null || that.getHumanId() == null)) && this.getHumanId().equals(that.getHumanId());
        } else {
            return matchHumanId(o);
        }
    }

    @Override
    public String toString() {
        return "Human{" +
                "humanId='" + humanId + '\'' +
                ", clearance=" + clearance +
                ", humanAlive=" + humanAlive +
                '}';
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface HumansFriend ---------------------

    @Transient
    @Override
    public String getDisplayName() {
        return getHumansNet().getDisplayName();
    }

    /**
     * Checks friendsHumanId is a friend of this human
     *
     * @param friendsHumanId
     * @return if friendsHumanId is a friend of this human
     */
    @Override
    @Transient
    public boolean ifFriend(final String friendsHumanId) {
        final Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doDirtyIsHumansNetPeople(new HumanId(this.humanId), new HumanId(friendsHumanId));
        if (r.returnStatus() != 0) {
            throw new DBException(r.returnError());
        }
        return r.returnValue();
    }

    /**
     * Checks friendsHumanId is NOT a friend of this human
     *
     * @param friendsHumanId
     * @return if friendsHumanId is NOT a friend of this human
     */
    @Override
    @Transient
    public boolean notFriend(final String friendsHumanId) {
        return !ifFriend(friendsHumanId);
    }

// -------------------------- OTHER METHODS --------------------------

    @Transient
    public Human getHuman() {
        return this;
    }

    public HumansAuthentication getHumansAuthentications() {
        return humansAuthentication;
    }

    public HumansPrivatePhoto getHumansPrivatePhoto() {
        return HumansPrivatePhoto;
    }

    public void setHumansAuthentications(final HumansAuthentication humansAuthentications) {
        this.humansAuthentication = humansAuthentications;
    }

    public void setHumansPrivatePhoto(HumansPrivatePhoto HumansPrivatePhoto) {
        this.HumansPrivatePhoto = HumansPrivatePhoto;
    }
}
