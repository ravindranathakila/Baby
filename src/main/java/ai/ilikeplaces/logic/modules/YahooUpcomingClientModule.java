package ai.ilikeplaces.logic.modules;

import upcoming.yahoo.api.conf.AbstractYahooUpcomingAPIClientModule;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 9/10/12
 * Time: 8:16 PM
 */
public class YahooUpcomingClientModule extends AbstractYahooUpcomingAPIClientModule {
    @Override
    protected String appKey() {
        return "WRONG_KEY";
    }
}
