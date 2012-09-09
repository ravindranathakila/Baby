package ai.ilikeplaces.ygp.impl.impl;

import ai.ilikeplaces.ygp.impl.Client;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.geojson.BBox;
import org.geojson.Coordinates;
import org.geojson.Type;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import where.yahooapis.com.v1.Place;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
public class YahooGeoPlanetClient implements Client {

    private static final String FORMAT_GEOJSON_APPID = "format=geojson&appid=";
    private static final String QUESTION_MARK = "?";
    private static final String GOT_ERROR_CODE = "Got error code:";
    final private String appid;
    final private String jsonEndpoint;

    @Inject
    public YahooGeoPlanetClient(@Named(value = "com.yahoo.appid") final String appid, @Assisted final String jsonEndpoint) {
        this.appid = appid;
        this.jsonEndpoint = jsonEndpoint;
    }


    public String getJsonEndpoint() {
        return jsonEndpoint;
    }

    public Object getPlaceValue(String endpointEndValue, String key) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(getJson(endpointEndValue));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {

            final JSONObject placeJson = jsonObject.getJSONObject("place");
            return placeJson.get(Place.attributes.valueOf(key).toString()).toString();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public Place getPlace(String endpointEndValue) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(getJson(endpointEndValue));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {

            final JSONObject placeJson = jsonObject.getJSONObject("place");
            System.out.println(placeJson);
            final JSONArray coordinates = new JSONArray(placeJson.get(Place.attributes.coordinates.toString()).toString());
            System.out.println(coordinates);
            final JSONArray bboxArray = new JSONArray(placeJson.get(Place.attributes.bbox.toString()).toString());
            System.out.println(bboxArray);
            final List<Coordinates> bbox = new ArrayList<Coordinates>(2);
            for(int i = 0 ; i < bboxArray.length()  ; i += 2){
                bbox.add(new Coordinates(Double.parseDouble(bboxArray.get(i).toString()), Double.parseDouble(bboxArray.get(i + 1).toString())));
                System.out.println(new Coordinates(Double.parseDouble(bboxArray.get(i).toString()), Double.parseDouble(bboxArray.get(i + 1).toString())));
            }

            return new Place(
                    Long.parseLong(placeJson.get(Place.attributes.woeid.toString()).toString()),
                    placeJson.get(Place.attributes.placeTypeName.toString()).toString(),
                    placeJson.get(Place.attributes.name.toString()).toString(),
                    placeJson.get(Place.attributes.country.toString()).toString(),
                    Type.Point,
                    new Coordinates(Double.parseDouble(coordinates.get(0).toString()), Double.parseDouble(coordinates.get(1).toString())),
                    new BBox(bbox),
                    placeJson.get(Place.attributes.uri.toString()).toString(),
                    placeJson.get(Place.attributes.lang.toString()).toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String getJson(final String endpointEndValue) {
        final HttpClient httpClient = new HttpClient();

        final GetMethod getMethod = new GetMethod(jsonEndpoint + endpointEndValue + QUESTION_MARK + FORMAT_GEOJSON_APPID + appid);

        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(getMethod);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        if (statusCode != HttpStatus.SC_OK) {
            throw new RuntimeException(GOT_ERROR_CODE + statusCode);
        }
        InputStream inputStream = null;

        try {
            inputStream = getMethod.getResponseBodyAsStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String accumulator = "";
        try {
            while ((line = br.readLine()) != null) {
                accumulator += line;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return accumulator;
    }
}
