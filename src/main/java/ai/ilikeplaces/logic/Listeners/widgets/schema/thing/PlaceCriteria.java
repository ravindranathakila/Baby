package ai.ilikeplaces.logic.Listeners.widgets.schema.thing;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 6/3/12
 * Time: 12:07 PM
 */
public class PlaceCriteria {

    //Place
    private String placeNamePre;
    private String placeName;
    private String placeNamePost;
    private String placeLng;
    private String placeLat;
    //Parent Place
    private String placeSuperNamePre;
    private String placeSuperName;
    private String placeSuperNamePost;
    private String placeSuperLng;
    private String placeSuperLat;
    //Parent WOEID
    private String placeSuperWOEID;

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

    public String getPlaceSuperNamePre() {
        return placeSuperNamePre;
    }

    public PlaceCriteria setPlaceSuperNamePre(String placeSuperNamePre) {
        this.placeSuperNamePre = placeSuperNamePre;
        return this;
    }

    public String getPlaceSuperName() {
        return placeSuperName;
    }

    public PlaceCriteria setPlaceSuperName(String placeSuperName) {
        this.placeSuperName = placeSuperName;
        return this;
    }

    public String getPlaceSuperNamePost() {
        return placeSuperNamePost;
    }

    public PlaceCriteria setPlaceSuperNamePost(String placeSuperNamePost) {
        this.placeSuperNamePost = placeSuperNamePost;
        return this;
    }

    public String getPlaceSuperLng() {
        return placeSuperLng;
    }

    public PlaceCriteria setPlaceSuperLng(String placeSuperLng) {
        this.placeSuperLng = placeSuperLng;
        return this;
    }

    public String getPlaceSuperLat() {
        return placeSuperLat;
    }

    public PlaceCriteria setPlaceSuperLat(String placeSuperLat) {
        this.placeSuperLat = placeSuperLat;
        return this;
    }

    public String getPlaceSuperWOEID() {
        return placeSuperWOEID;
    }

    public PlaceCriteria setPlaceSuperWOEID(String placeSuperWOEID) {
        this.placeSuperWOEID = placeSuperWOEID;
        return this;
    }
}
