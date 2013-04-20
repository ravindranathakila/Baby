package ai.ilikeplaces.copy.http.json.impl;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
public interface Client {
    public String getJsonEndpoint();

    public JSONObject get(final String endpointEndValue, final Map<String, String> parameters);
}
