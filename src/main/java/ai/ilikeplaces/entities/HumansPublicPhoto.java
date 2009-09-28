/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ai.ilikeplaces.entities;

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
@Entity
public class HumansPublicPhoto implements Serializable {
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

    @OneToOne(cascade=CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public Human getHuman() {
        return human;
    }

    public void setHuman(final Human human) {
        this.human = human;
    }

    @OneToMany(fetch=FetchType.LAZY)
    public List<PublicPhoto> getPublicPhotos() {
        return publicPhotos;
    }

    public void setPublicPhotos(List<PublicPhoto> publicPhotos) {
        this.publicPhotos = publicPhotos;
    }

//
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
//        if (!(object instanceof HumansPublicPhoto)) {
//            return false;
//        }
//        HumansPublicPhoto other = (HumansPublicPhoto) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "ai.ilikeplaces.entities.HumansPublicPhoto[id=" + id + "]";
//    }

}
