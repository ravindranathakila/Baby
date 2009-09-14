package ai.ilikeplaces.entities;

import java.io.Serializable;
import java.lang.reflect.Field;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Ravindranath Akila
 */
@Entity
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long imageId;
    private String imageFilePath;
    private String imageURLPath;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(final Long imageId__) {
        this.imageId = imageId__;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(final String imageFilePath__) {
        this.imageFilePath = imageFilePath__;
    }

    public String getImageURLPath() {
        return imageURLPath;
    }

    public void setImageURLPath(final String imageURLPath__) {
        this.imageURLPath = imageURLPath__;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.imageId != null ? this.imageId.hashCode() : 0);
        hash = 37 * hash + (this.imageFilePath != null ? this.imageFilePath.hashCode() : 0);
        hash = 37 * hash + (this.imageURLPath != null ? this.imageURLPath.hashCode() : 0);
        return hash;
    }


    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Image)) {
//            return false;
//        }
//        Image other = (Image) object;
//        if ((this.imageId == null && other.imageId != null) || (this.imageId != null && !this.imageId.equals(other.imageId))) {
//            return false;
//        }
//        return true;
        throw new UnsupportedOperationException("PLEASE PROVIDE THE BODY OF THIS EQUALS METHOD!");
    }

    /**
     *
     * @return toString_
     */
    @Override
    public String toString() {
        String toString_ = new String(getClass().getName());
        try {
            final Field[] fields = {getClass().getDeclaredField("locationId"),
                getClass().getDeclaredField("locationName"),
                getClass().getDeclaredField("locationSuperSet")};

            for (final Field field : fields) {
                try {
                    toString_ += "\n{" + field.getName() + "," + field.get(this) + "}";
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
        }

        return toString_;
    }

    /**
     *
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = new String(toString() + "\n");
        changeLog += "20090914 Added this class \n";
        return showChangeLog__ ? changeLog : toString();
    }
}