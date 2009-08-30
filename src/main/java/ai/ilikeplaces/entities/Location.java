package ai.ilikeplaces.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Location implements Serializable {

    private String locationName;
    private String locationInfo;

    @Id
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
