//package ai.ilikeplaces.entities;
//
//import java.io.Serializable;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//
///**
// *
// * @author Ravindranath Akila
// */
//@Entity
//public class HumansAuthorizations implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    private String humanIdentification;
//
//
//    @Id
//    public String getHumanIdentification() {
//        return humanIdentification;
//    }
//
//    public void setHumanIdentification(final String humanIdentification__) {
//        this.humanIdentification = humanIdentification__;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (humanIdentification != null ? humanIdentification.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof HumansAuthorizations)) {
//            return false;
//        }
//        HumansAuthorizations other = (HumansAuthorizations) object;
//        if ((this.humanIdentification == null && other.humanIdentification != null) || (this.humanIdentification != null && !this.humanIdentification.equals(other.humanIdentification))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "ai.ilikeplaces.entities.HumansAuthorizations[id=" + humanIdentification + "]";
//    }
//}
