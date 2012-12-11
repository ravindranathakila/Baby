package ai.reaver;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryProvider;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
public class LoggerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LoggerClientFactory.class).toProvider(
                FactoryProvider.newFactory(
                        LoggerClientFactory.class,
                        LoggerClientImpl.class));
    }

}
