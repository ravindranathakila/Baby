package where.yahooapis.com.v1;

import org.geojson.BBox;
import org.geojson.Coordinates;
import org.geojson.Type;

/**
 * Below is the GeoJson format as copied off from http://developer.yahoo.com/geo/geoplanet/guide/api_docs.html
 * <p/>
 * <code>
 * {
 * "place":{
 * "woeid":2507854,
 * "placeTypeName":"Town",
 * "placeTypeName attrs":{
 * "code":7 },
 * "name":"Trenton",
 * "country":"United States",
 * "country attrs":{
 * "type":"Country",
 * "code":"US" },
 * "admin1":"New Jersey",
 * "admin1 attrs":{
 * "type":"State",
 * "code":"US-NJ" },
 * "admin2":"Mercer",
 * "admin2 attrs":{
 * "type":"County",
 * "code":"" },
 * "admin3":"",
 * "locality1":"Trenton",
 * "locality1 attrs":{
 * "type":"Town" },
 * "locality2":"",
 * "postal":"",
 * "type":"Point",
 * "coordinates":[
 * -74.759377,
 * 40.217869 ],
 * "bbox":[
 * -74.819519,
 * 40.183868,
 * -74.728798,
 * 40.248291 ]
 * "uri":"http:\/\/where.yahooapis.com\/v1\/place\/2507854",
 * "lang":"en-us" }
 * }
 * </code>
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * /**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
public class Place {
    final long woeid;

    final String placeTypeName;

    final String name;
    final String country;

    final Type type;

    final Coordinates coordinates;

    final BBox bbox;

    final String uri;

    final String lang;

    public static enum attributes {
        woeid, placeTypeName, name, country, type, coordinates, bbox, uri, lang
    }

    public Place(long woeid, String placeTypeName, String name, String country, Type type, Coordinates coordinates, BBox bbox, String uri, String lang) {
        this.woeid = woeid;
        this.placeTypeName = placeTypeName;
        this.name = name;
        this.country = country;
        this.type = type;
        this.coordinates = coordinates;
        this.bbox = bbox;
        this.uri = uri;
        this.lang = lang;
    }

    public long getWoeid() {
        return woeid;
    }

    public String getPlaceTypeName() {
        return placeTypeName;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Type getType() {
        return type;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public BBox getBbox() {
        return bbox;
    }

    public String getUri() {
        return uri;
    }

    public String getLang() {
        return lang;
    }

    @Override
    public String toString() {
        return "Place{" +
                "woeid=" + woeid +
                ", placeTypeName='" + placeTypeName + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", type=" + type +
                ", coordinates=" + coordinates +
                ", bbox=" + bbox +
                ", uri='" + uri + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }
}



