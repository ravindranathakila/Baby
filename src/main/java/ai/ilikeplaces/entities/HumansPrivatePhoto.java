/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ai.ilikeplaces.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author Ravindranath Akila
 */
@Entity
public class HumansPrivatePhoto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String humanId;
    private Human human;
    private List<PrivatePhoto> privatePhotos;

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


    @OneToMany(cascade=CascadeType.ALL)
    public List<PrivatePhoto> getPrivatePhotos() {
        return privatePhotos;
    }

    public void setPrivatePhotos(List<PrivatePhoto> privatePhotos) {
        this.privatePhotos = privatePhotos;
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
//        if (!(object instanceof HumansPrivatePhoto)) {
//            return false;
//        }
//        HumansPrivatePhoto other = (HumansPrivatePhoto) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "ai.ilikeplaces.entities.HumansPrivatePhoto[id=" + id + "]";
//    }

}
