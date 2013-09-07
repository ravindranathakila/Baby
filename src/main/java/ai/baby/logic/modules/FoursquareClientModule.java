package ai.baby.logic.modules;

import api.foursquare.com.conf.AbstractFoursquareAPIClientModule;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 9/10/12
 * Time: 8:16 PM
 */
public class FoursquareClientModule extends AbstractFoursquareAPIClientModule {

    @Override
    protected String appKey() {
        return "";
    }
}
