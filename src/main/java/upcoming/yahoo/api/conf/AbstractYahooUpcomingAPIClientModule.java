package upcoming.yahoo.api.conf;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryProvider;
import com.google.inject.name.Named;
import upcoming.yahoo.api.impl.ClientFactory;
import upcoming.yahoo.api.impl.impl.YahooUpcomingAPIClient;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
abstract public class AbstractYahooUpcomingAPIClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClientFactory.class).toProvider(
                FactoryProvider.newFactory(
                        ClientFactory.class,
                        YahooUpcomingAPIClient.class));
    }

    /**
     * Override this method and return yor YahooUpcoming Api Key
     *
     * @return YahooUpcoming App Key
     */
    @Provides
    @Named(value = "api_key")
    protected abstract String appKey();
}
