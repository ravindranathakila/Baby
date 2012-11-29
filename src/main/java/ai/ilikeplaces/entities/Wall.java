package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import ai.ilikeplaces.util.jpa.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 25, 2010
 * Time: 1:01:22 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "Wall", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class Wall implements Clearance, Refreshable<Wall>, Serializable {
// ------------------------------ FIELDS ------------------------------

    final static public int wallTypeMISC = 0;
    final static public int wallTypeHuman = 1;
    final static public int wallTypePrivateEvent = 2;
    final static public int wallTypeTribe = 3;
    final static public int wallTypePrivatePhoto = 4;

//    final static public int WALL_LENGTH = 10240;

    private static final Refresh<Wall> REFRESH = new Refresh<Wall>();

    @Id
    @Column(name = "wallId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long wallId = null;
    public static final String wallIdCOL = "wallId";

    @Column(name = "clearance")
    public Long clearance = 0L;

    @Column(name = "wallContent")
    public String wallContent = null;

    @RefreshId("wallMsgs")
    @DOCUMENTATION(
            NOTE = @NOTE("Wall msgs are fetched lazy because sometimes private event is required to be loaded fast, and fetches wall, thereby wall msgs.")
    )
    @FIXME(issue = "Find out how to limit resultset to say, last 20, in order to limit the results fetched")
    @_unidirectional
    @TODO(task = "Move DESC ASC TO SOME STATIC CLASS FOR REUSE")
    @OrderBy(Msg.msgIdCOL + " DESC")
    @OneToMany(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
            fetch = FetchType.LAZY)
    public List<Msg> wallMsgs = null;

    @RefreshId("wallMutes")
    @_unidirectional
    @OneToMany(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = Wall.wallIdCOL)
    public List<Mute> wallMutes = null;

    @Column(name = "wallType")
    public Integer wallType = null;

    @Column(name = "wallMetadata")
    public String wallMetadata = null;

// --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public Long getClearance() {
        return this.clearance;
    }

    @Override
    public void setClearance(final Long clearance) {
        this.clearance = clearance;
    }

    public Long getWallId() {
        return wallId;
    }

    public void setWallId(Long wallId) {
        this.wallId = wallId;
    }

    public String getWallMetadata() {
        return wallMetadata;
    }

    public void setWallMetadata(final String wallMetadata) {
        this.wallMetadata = wallMetadata;
    }

    public List<Msg> getWallMsgs() {
        return wallMsgs;
    }

    public void setWallMsgs(final List<Msg> wallMsgs) {
        this.wallMsgs = wallMsgs;
    }

    public List<Mute> getWallMutes() {
        return wallMutes;
    }

    public void setWallMutes(final List<Mute> wallMutes) {
        this.wallMutes = wallMutes;
    }

    public Integer getWallType() {
        return wallType;
    }

    public void setWallType(final Integer wallType) {
        this.wallType = wallType;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Wall wall = (Wall) o;

        if (wallId != null ? !wallId.equals(wall.wallId) : wall.wallId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return wallId != null ? wallId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Wall{" +
                "wallId=" + wallId +
                ", clearance=" + clearance +
                ", wallType=" + wallType +
                ", wallMetadata='" + wallMetadata + '\'' +
                '}';
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Refreshable ---------------------

    @Override
    public Wall refresh(final RefreshSpec refreshSpec) throws RefreshException {
        REFRESH.refresh(this, refreshSpec);
        return this;
    }

// -------------------------- OTHER METHODS --------------------------

    @Transient
    public String metadataValueFor(final WallMetadataKey keyEnum) {
        final String key = keyEnum.toString();
        final String wallMetadata = this.getWallMetadata();
        final String returnVal;
        if (wallMetadata == null || wallMetadata.isEmpty()) {
            returnVal = null;
        } else {
            final String[] pairs = wallMetadata.split(",");
            String value = null;
            for (final String pairString : pairs) {
                final String[] pair = pairString.split("=");
                if (key.equals(pair[0])) {
                    value = pair[1];
                }
            }
            returnVal = value;
        }
        return returnVal;
    }

//    @Column(length = WALL_LENGTH)
//    public String getWallContent() {
//        return wallContent;
//    }
//
//    public void setWallContent(String wallContent) {
//        this.wallContent = wallContent;
//    }

    @Transient
    public Wall setWallContentR(String wallContent) {
        this.wallContent = wallContent;
        return this;
    }

    public Wall setWallTypeR(final Integer wallType) {
        this.wallType = wallType;
        return this;
    }

// -------------------------- ENUMERATIONS --------------------------

    public static enum WallMetadataKey {
        HUMAN,
        PRIVATE_PHOTO,
        PRIVATE_EVENT,
        TRIBE,
    }
}
