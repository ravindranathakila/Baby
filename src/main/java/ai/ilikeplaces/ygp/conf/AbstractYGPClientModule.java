package ai.ilikeplaces.ygp.conf;

import ai.ilikeplaces.ygp.impl.ClientFactory;
import ai.ilikeplaces.ygp.impl.impl.YahooGeoPlanetClient;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryProvider;
import com.google.inject.name.Named;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
abstract public class AbstractYGPClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClientFactory.class).toProvider(
                FactoryProvider.newFactory(
                        ClientFactory.class,
                        YahooGeoPlanetClient.class));
    }

    /**
     * Override this method and return yor Yahoo App ID
     *
     * @return Yahoo App ID
     */
    @Provides
    @Named(value = "com.yahoo.appid")
    protected abstract String appidProvider();
}
