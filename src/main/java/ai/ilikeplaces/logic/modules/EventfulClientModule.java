package ai.ilikeplaces.logic.modules;

import api.eventful.com.conf.AbstractEventfulAPIClientModule;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 9/10/12
 * Time: 8:16 PM
 */
public class EventfulClientModule extends AbstractEventfulAPIClientModule {

    public static final String EVENTFUL_KEY = "SGL9PRwvR9W45qJK";

    @Override
    protected String appKey() {
        return EVENTFUL_KEY;
    }
}
