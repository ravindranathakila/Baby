package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.*;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
public class HumansPublicPhoto implements HumanIdFace, Serializable {

    private static final long serialVersionUID = 1L;
    private String humanId;
    private Human human;
    private List<PublicPhoto> publicPhotos;

    @Id
    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public Human getHuman() {
        return human;
    }

    public void setHuman(final Human human) {
        this.human = human;
    }

    @FIXME(issue="Fetch type should be lazy, check if callers can do list.size() to refresh list." +
    "The List of photos can be huge so eager isn't practical")
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
    public List<PublicPhoto> getPublicPhotos() {
        return publicPhotos;
    }

    public void setPublicPhotos(List<PublicPhoto> publicPhotos) {
        this.publicPhotos = publicPhotos;
    }

    @Override
    public String toString() {
        return "HumansPublicPhoto{" +
                "humanId='" + humanId + '\'' +
                '}';
    }
}
