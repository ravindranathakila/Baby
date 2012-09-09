package com.google.places.api.conf;

import com.google.places.api.impl.ClientFactory;
import com.google.places.api.impl.impl.GooglePlacesAPIClient;
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
abstract public class AbstractGooglePlacesAPIClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClientFactory.class).toProvider(
                FactoryProvider.newFactory(
                        ClientFactory.class,
                        GooglePlacesAPIClient.class));
    }

    /**
     * Override this method and return yor GooglePlaces Api Key
     *
     * @return GooglePlaces App Key
     */
    @Provides
    @Named(value = "api_key")
    protected abstract String appKey();
}
