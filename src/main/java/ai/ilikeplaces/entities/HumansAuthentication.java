package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;
import java.io.Serializable;


/**
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumansAuthentication implements HumanPkJoinFace, Serializable {

    private static final long serialVersionUID = 1L;
    public String humanId;
    public Human human;
    public String humanAuthenticationHash;
    public String humanAuthenticationSalt;

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

    public String getHumanAuthenticationHash() {
        return humanAuthenticationHash;
    }

    public void setHumanAuthenticationHash(final String humanAuthenticationHash__) {
        this.humanAuthenticationHash = humanAuthenticationHash__;
    }

    public String getHumanAuthenticationSalt() {
        return humanAuthenticationSalt;
    }

    public void setHumanAuthenticationSalt(final String humanAuthenticationSalt__) {
        this.humanAuthenticationSalt = humanAuthenticationSalt__;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (human != null ? human.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof HumansAuthentication)) {
//            return false;
//        }
//        HumansAuthentication other = (HumansAuthentication) object;
//        if ((this.human == null && other.human != null) || (this.human != null && !this.human.equals(other.human))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "ai.ilikeplaces.entities.human[id=" + human + "]";
//    }
}
