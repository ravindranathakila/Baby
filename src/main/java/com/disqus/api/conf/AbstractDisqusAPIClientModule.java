package com.disqus.api.conf;

import com.disqus.api.impl.ClientFactory;
import com.disqus.api.impl.impl.DisqusAPIClient;
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
abstract public class AbstractDisqusAPIClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClientFactory.class).toProvider(
                FactoryProvider.newFactory(
                        ClientFactory.class,
                        DisqusAPIClient.class));
    }

    /**
     * Override this method and return yor Disqus Forum Simple Name
     *
     * @return Disqus App ID
     */
    @Provides
    @Named(value = "forum")
    protected abstract String forumShortName();

    /**
     * Override this method and return yor Disqus Api Secret
     *
     * @return Disqus App ID
     */
    @Provides
    @Named(value = "api_secret")
    protected abstract String appSecret();
}
