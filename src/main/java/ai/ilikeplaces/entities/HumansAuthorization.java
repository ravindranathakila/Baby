package ai.ilikeplaces.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 *
 * @author Ravindranath Akila
 */
@Entity
public class HumansAuthorization implements Serializable {

    private static final long serialVersionUID = 1L;

    public String humanIdentification;


    @Id
    public String getHumanIdentification() {
        return humanIdentification;
    }

    public void setHumanIdentification(final String humanIdentification__) {
        this.humanIdentification = humanIdentification__;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (humanIdentification != null ? humanIdentification.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HumansAuthorization)) {
            return false;
        }
        HumansAuthorization other = (HumansAuthorization) object;
        if ((this.humanIdentification == null && other.humanIdentification != null) || (this.humanIdentification != null && !this.humanIdentification.equals(other.humanIdentification))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ai.ilikeplaces.entities.HumansAuthorizations[id=" + humanIdentification + "]";
    }
}
