package ai.ilikeplaces.entities;

import ai.ilikeplaces.entities.etc.EntityLifeCycleListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "PublicPhoto", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@_ok
@EntityListeners({EntityLifeCycleListener.class})
public class PublicPhoto implements Serializable {
// ------------------------------ FIELDS ------------------------------

    final static public String locationCOL = "location";

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "publicPhotoId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long publicPhotoId = null;

    @Column(name = "")
    public String publicPhotoFilePath;

    @_field_preamble(description = "The path should be very random as it will be exposed to the www." +
            "Also make sure this supports good SEO.")
    @Column(name = "publicPhotoURLPath")
    public String publicPhotoURLPath;

    @Column(name = "publicPhotoName")
    public String publicPhotoName;

    @Column(name = "publicPhotoDescription", length = 1000)
    public String publicPhotoDescription;

    @_field_preamble(description = "Required to calculate ranking")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "publicPhotoUploadDate")
    public Date publicPhotoUploadDate;

    @_field_preamble(description = "Required to calculate rank position")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "publicPhotoTakenDate")
    public Date publicPhotoTakenDate;

    @_field_preamble(description = "Required to calculate rank position")
    @Column(name = "publicPhotoRankUnits")
    public Long publicPhotoRankUnits;

    @_field_preamble(description = "Required to calculate rank position")
    @Column(name = "publicPhotoRankTurns")
    public Long publicPhotoRankTurns;

    @_field_preamble(description = "Required when rebuilding a database from scratch someday." +
            "Since the whole concept of ilikeplaces relies on content richness, preserving this in this table important.")

    @_bidirectional(ownerside = _bidirectional.OWNING.IS)
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = Location.locationIdCOL)
    public Location location;

    @_field_preamble(description = "Who uploaded this image? Will he request to delete it?")

    @WARNING(warning = "The cascade types of HumansPublicPhoto, Location and this method, are very CascadeType sensitive. Any mistake will trigger rollbacks.")
    @_bidirectional(ownerside = _bidirectional.OWNING.IS)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = HumansPublicPhoto.humanIdCOL)
    public HumansPublicPhoto humansPublicPhoto;
    public static final String humansPublicPhotoCOL = "humansPublicPhoto";

    @_note(note = "Pre persisted entities will have null ids. hence using pre persisted ids is not practical.")
    final private UUID uUID = UUID.randomUUID();

// --------------------- GETTER / SETTER METHODS ---------------------

    public HumansPublicPhoto getHumansPublicPhoto() {
        return humansPublicPhoto;
    }

    public void setHumansPublicPhoto(HumansPublicPhoto humansPublicPhoto) {
        this.humansPublicPhoto = humansPublicPhoto;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPublicPhotoDescription() {
        return publicPhotoDescription;
    }

    public void setPublicPhotoDescription(final String publicPhotoDescription) {
        this.publicPhotoDescription = publicPhotoDescription;
    }

    public String getPublicPhotoFilePath() {
        return publicPhotoFilePath;
    }

    public void setPublicPhotoFilePath(String publicPhotoFilePath) {
        this.publicPhotoFilePath = publicPhotoFilePath;
    }

    public Long getPublicPhotoId() {
        return publicPhotoId;
    }

    public void setPublicPhotoId(Long publicPhotoId) {
        this.publicPhotoId = publicPhotoId;
    }

    public String getPublicPhotoName() {
        return publicPhotoName;
    }

    public void setPublicPhotoName(String publicPhotoName) {
        this.publicPhotoName = publicPhotoName;
    }

    public Long getPublicPhotoRankTurns() {
        return publicPhotoRankTurns;
    }

    public void setPublicPhotoRankTurns(Long publicPhotoRankTurns) {
        this.publicPhotoRankTurns = publicPhotoRankTurns;
    }

    public Long getPublicPhotoRankUnits() {
        return publicPhotoRankUnits;
    }

    public void setPublicPhotoRankUnits(Long publicPhotoRankUnits) {
        this.publicPhotoRankUnits = publicPhotoRankUnits;
    }

    public Date getPublicPhotoTakenDate() {
        return publicPhotoTakenDate;
    }

    public void setPublicPhotoTakenDate(Date publicPhotoTakenDate) {
        this.publicPhotoTakenDate = publicPhotoTakenDate;
    }

    public String getPublicPhotoURLPath() {
        return publicPhotoURLPath;
    }

    public void setPublicPhotoURLPath(String publicPhotoURLPath) {
        this.publicPhotoURLPath = publicPhotoURLPath;
    }

    public Date getPublicPhotoUploadDate() {
        return publicPhotoUploadDate;
    }

    public void setPublicPhotoUploadDate(Date publicPhotoUploadDate) {
        this.publicPhotoUploadDate = publicPhotoUploadDate;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public boolean equals(Object obj) {
        boolean returnVal = false;
        equals:
        {
            if (obj == null) {
                returnVal = false;
                break equals;
            }
            if (getClass() != obj.getClass()) {
                returnVal = false;
                break equals;
            }
            final PublicPhoto other = (PublicPhoto) obj;
            /*Are both not null?*/
            if (this.getPublicPhotoId() != null && other.getPublicPhotoId() != null) {
                if (this.getPublicPhotoId().equals(other.getPublicPhotoId())) {
                    returnVal = true;
                    break equals;
                }
            } else if (this.getPublicPhotoId() == null && other.getPublicPhotoId() == null) {/*Are both null?*/
                /*i.e. Still not persisted. Lets compare UUIDs*/
                if (this.uUID.equals(other.uUID)) {
                    returnVal = true;
                    break equals;
                } else {
                    returnVal = false;
                    break equals;
                }
            } else {/*Only one is null*/
                returnVal = false;
                break equals;
            }
        }
        return returnVal;
    }

    @Override
    public int hashCode() {
        return (int) (this.getPublicPhotoId() != null ? this.getPublicPhotoId() : uUID.hashCode());
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return "PublicPhoto{" +
                ", publicPhotoId=" + publicPhotoId +
                '}';
    }
}
