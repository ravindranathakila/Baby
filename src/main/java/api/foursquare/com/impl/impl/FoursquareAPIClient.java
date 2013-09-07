package api.foursquare.com.impl.impl;

import ai.baby.util.Loggers;
import api.foursquare.com.impl.Client;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
public class FoursquareAPIClient implements Client {


    final private String api_key;

    final private String jsonEndpoint;

    @Inject
    public FoursquareAPIClient(@Named(value = "api_key") final String api_key, @Assisted final String jsonEndpoint) {
        this.api_key = api_key;
        this.jsonEndpoint = jsonEndpoint;
    }

    public String getJsonEndpoint() {
        return jsonEndpoint;
    }

    public JSONObject get(final String endpointEndValue, final Map<String, String> parameters) {
        final StringBuilder sb = new StringBuilder("");
        for (final String key : parameters.keySet()) {
            sb.append("&").append(key).append("=").append(parameters.get(key));
        }

        JSONObject jsonObject;

        String _json = null;

        try {
            _json = getJson(endpointEndValue, sb.toString());
            jsonObject = new JSONObject(_json);
        } catch (JSONException e) {
            //throw new RuntimeException(e);
            jsonObject = new JSONObject();
            Loggers.ERROR.error("Error parsing this to JSON:" + _json);
        }

        return jsonObject;
    }

    private String getJson(final String endpointEndValue, final String... optionalAppend) {
        final HttpClient httpClient = new HttpClient();

        final String toBeCalled = jsonEndpoint + endpointEndValue
                + "?"
                + "api_key" + "=" + api_key
                + ((optionalAppend != null && optionalAppend.length != 0) ? Arrays.toString(optionalAppend).replace("[", "").replace("]", "") : "");

        final GetMethod getMethod = new GetMethod(toBeCalled);

        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(getMethod);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        if (statusCode != HttpStatus.SC_OK) {
            throw new RuntimeException("Got error code:" + statusCode);
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
