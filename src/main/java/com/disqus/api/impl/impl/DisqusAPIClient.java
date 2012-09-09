package com.disqus.api.impl.impl;

import com.disqus.api.impl.Client;
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
public class DisqusAPIClient implements Client {

    private static final String QUESTION_MARK = "?";
    private static final String GOT_ERROR_CODE = "Got error code:";
    private static final String DOT_JASON = ".json";
    private static final String EQUALS = "=";
    private static final String API_SECRET = "api_secret" + EQUALS;
    private static final String FORUM = "forum" + EQUALS;
    private static final String AMPERSAND = "&";
    private static final String OPEN_SQR_BRCKT = "[";
    private static final String CLOSE_SQR_BRCKT = "]";
    private static final String EMPTY = "";
    final private String api_secret;
    final private String jsonEndpoint;
    final private String forum;

    @Inject
    public DisqusAPIClient(@Named(value = "forum") final String forum, @Named(value = "api_secret") final String api_secret, @Assisted final String jsonEndpoint) {
        this.api_secret = api_secret;
        this.forum = forum;
        this.jsonEndpoint = jsonEndpoint;
    }


    public String getJsonEndpoint() {
        return jsonEndpoint;
    }


    public JSONObject get(final String endpointEndValue) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(getJson(endpointEndValue));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }

    public JSONObject get(final String endpointEndValue, final Map<String, String> parameters) {
        final StringBuilder sb = new StringBuilder(EMPTY);
        for (final String key : parameters.keySet()) {
            sb.append(AMPERSAND).append(key).append(EQUALS).append(parameters.get(key));
        }

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(getJson(endpointEndValue, sb.toString()));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }

    private String getJson(final String endpointEndValue, final String... optionalAppend) {
        final HttpClient httpClient = new HttpClient();

        final String toBeCalled = jsonEndpoint + endpointEndValue
                + DOT_JASON
                + QUESTION_MARK
                + API_SECRET + api_secret
                + AMPERSAND
                + FORUM + forum
                + ((optionalAppend != null && optionalAppend.length != 0) ? Arrays.toString(optionalAppend).replace(OPEN_SQR_BRCKT, EMPTY).replace(CLOSE_SQR_BRCKT, EMPTY) : EMPTY);

        System.out.println(toBeCalled);

        final GetMethod getMethod = new GetMethod(toBeCalled);

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
        String accumulator = EMPTY;
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
