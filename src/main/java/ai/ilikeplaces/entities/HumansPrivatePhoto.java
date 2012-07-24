/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumansPrivatePhoto implements HumanIdFace, Serializable {
    private static final long serialVersionUID = 1L;

    public String humanId;
    public Human human;
    public List<PrivatePhoto> privatePhotos;

    @Id
    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    @OneToOne(cascade=CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
    public Human getHuman() {
        return human;
    }

    public void setHuman(final Human human) {
        this.human = human;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
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
