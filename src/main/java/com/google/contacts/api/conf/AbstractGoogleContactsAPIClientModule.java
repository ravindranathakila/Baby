package com.google.contacts.api.conf;

import com.google.contacts.api.impl.ClientFactory;
import com.google.contacts.api.impl.impl.GoogleContactsAPIClient;
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
abstract public class AbstractGoogleContactsAPIClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClientFactory.class).toProvider(
                FactoryProvider.newFactory(
                        ClientFactory.class,
                        GoogleContactsAPIClient.class));
    }

    /**
     * Override this method and return your GoogleContacts access_token
     *
     * @return GoogleContacts GoogleContacts access_token
     */
    @Provides
    @Named(value = "access_token")
    protected abstract String accessToken();


}
