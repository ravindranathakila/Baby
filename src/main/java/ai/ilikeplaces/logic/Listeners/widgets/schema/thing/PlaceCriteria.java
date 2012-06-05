package ai.ilikeplaces.logic.Listeners.widgets.schema.thing;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 6/3/12
 * Time: 12:07 PM
 */
public class PlaceCriteria {

    private String placeNamePre;
    private String placeName;
    private String placeNamePost;
    private String placeLng;
    private String placeLat;

    public String getPlaceNamePre() {
        return placeNamePre;
    }

    public PlaceCriteria setPlaceNamePre(String placeNamePre) {
        this.placeNamePre = placeNamePre;
        return this;
    }

    public String getPlaceName() {
        return placeName;
    }

    public PlaceCriteria setPlaceName(String placeName) {
        this.placeName = placeName;
        return this;
    }

    public String getPlaceNamePost() {
        return placeNamePost;
    }

    public PlaceCriteria setPlaceNamePost(String placeNamePost) {
        this.placeNamePost = placeNamePost;
        return this;
    }

    public String getPlaceLng() {
        return placeLng;
    }

    public PlaceCriteria setPlaceLng(String placeLng) {
        this.placeLng = placeLng;
        return this;
    }

    public String getPlaceLat() {
        return placeLat;
    }

    public PlaceCriteria setPlaceLat(String placeLat) {
        this.placeLat = placeLat;
        return this;
    }
}
