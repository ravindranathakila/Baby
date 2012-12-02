package ai.ilikeplaces.ygp.impl;

import where.yahooapis.com.v1.Place;

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

    public Place getPlace(final String endpointEndValue);

    public Object getPlaceValue(String endpointEndValue, String key);
}
