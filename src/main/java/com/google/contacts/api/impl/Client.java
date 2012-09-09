package com.google.contacts.api.impl;

import org.json.JSONObject;

import java.net.URI;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
public interface Client{
    public URI get(final String endpointEndValue, final Map<String, String> parameters);
}
