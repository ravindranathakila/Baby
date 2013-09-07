package ai.baby.copy.http.json.conf;

import ai.baby.copy.http.json.impl.ClientFactory;
import ai.baby.copy.http.json.impl.impl.SampleAPIClient;
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
abstract public class AbstractSampleAPIClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClientFactory.class).toProvider(
                FactoryProvider.newFactory(
                        ClientFactory.class,
                        SampleAPIClient.class));
    }

    /**
     * Override this method and return your sample Api Key
     *
     * @return api_key, which will be used by constructor
     */
    @Provides
    @Named(value = "api_key")
    protected abstract String appKey();
}
