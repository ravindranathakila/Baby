package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc._bidirectional;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "HumansPublicPhoto", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumansPublicPhoto implements HumanIdFace, Serializable {
// ------------------------------ FIELDS ------------------------------

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "humanId")
    public String humanId;
    public static final String humanIdCOL = "humanId";


    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL)
    //@PrimaryKeyJoinColumn
    public Human human;

    @FIXME(issue = "Fetch type should be lazy, check if callers can do list.size() to refresh list." +
            "The List of photos can be huge so eager isn't practical")
    @_bidirectional(ownerside = _bidirectional.OWNING.NOT)
    @OneToMany(mappedBy = PublicPhoto.humansPublicPhotoCOL, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = humanIdCOL)
    public List<PublicPhoto> publicPhotos;

// --------------------- GETTER / SETTER METHODS ---------------------

    public Human getHuman() {
        return human;
    }

    public void setHuman(final Human human) {
        this.human = human;
    }

    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    public List<PublicPhoto> getPublicPhotos() {
        return publicPhotos;
    }

    public void setPublicPhotos(List<PublicPhoto> publicPhotos) {
        this.publicPhotos = publicPhotos;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "HumansPublicPhoto{" +
                "humanId='" + humanId + '\'' +
                '}';
    }
}
