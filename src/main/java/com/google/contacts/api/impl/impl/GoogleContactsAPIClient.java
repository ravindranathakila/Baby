package com.google.contacts.api.impl.impl;

import com.google.contacts.api.impl.Client;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
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
public class GoogleContactsAPIClient implements Client {

    private static final String QUESTION_MARK = "?";
    private static final String GOT_ERROR_CODE = "Got error code:";
    private static final String DOT_JASON = ".json";
    private static final String EQUALS = "=";
    private static final String ACCESSTOKEN = "acessToken" + EQUALS;
    private static final String AMPERSAND = "&";
    private static final String OPEN_SQR_BRCKT = "[";
    private static final String CLOSE_SQR_BRCKT = "]";
    private static final String EMPTY = "";
    final private String Endpoint;
    final private String acessToken;

    @Inject
    public GoogleContactsAPIClient(@Named(value = "access_token") final String acessToken, @Assisted final String endPoint) {
        this.acessToken = acessToken;
        this.Endpoint = endPoint;
    }


    public String getEndpoint() {
        return Endpoint;
    }


    public URI get(final String endpointEndValue, final Map<String, String> parameters) {
        final StringBuilder sb = new StringBuilder(EMPTY);
        for (final String key : parameters.keySet()) {
            sb.append(AMPERSAND).append(key).append(EQUALS).append(parameters.get(key));
        }

        try {
            return new URI(get(endpointEndValue, sb.toString()));
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String get(final String endpointEndValue, final String... optionalAppend) {

        final String toBeCalled = Endpoint + endpointEndValue
                + DOT_JASON
                + QUESTION_MARK
                + AMPERSAND
                + ACCESSTOKEN + acessToken
                + ((optionalAppend != null && optionalAppend.length != 0) ? Arrays.toString(optionalAppend).replace(OPEN_SQR_BRCKT, EMPTY).replace(CLOSE_SQR_BRCKT, EMPTY) : EMPTY);


        return toBeCalled;
    }
}
