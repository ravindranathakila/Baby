package ai.ilikeplaces.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.CascadeType;


/**
 *
 * @author Ravindranath Akila
 */
@Entity
public class HumansAuthentication implements Serializable {

    private static final long serialVersionUID = 1L;
    private String humanId;
    private Human human;
    private String humanAuthenticationHash;
    private String humanAuthenticationSalt;

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
