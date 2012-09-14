package ai.ilikeplaces.logic.modules;

import ai.ilikeplaces.rbs.RBGet;
import upcoming.yahoo.api.conf.AbstractYahooUpcomingAPIClientModule;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 9/10/12
 * Time: 8:16 PM
 */
public class YahooUpcomingClientModule extends AbstractYahooUpcomingAPIClientModule {

    public static final String YAHOO_KEY = RBGet.getGlobalConfigKey("YAHOO_K3Y");

    @Override
    protected String appKey() {
        return YAHOO_KEY;
    }
}
