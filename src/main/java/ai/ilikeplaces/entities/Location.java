package ai.ilikeplaces.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Location implements Serializable {

    private Integer locationId;
    private String locationName;
    private String locationInfo;

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(final Integer locationId) {
        this.locationId = locationId;
    }

    @Id
    @Column(unique=true)
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(final String locationName) {
        this.locationName = locationName;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(final String locationInfo) {
        this.locationInfo = locationInfo;
    }
}
