package ai.ilikeplaces.util.jndi.conf;

import ai.ilikeplaces.util.jndi.impl.JNDILookupFactory;
import ai.ilikeplaces.util.jndi.impl.impl.JNDILookupImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryProvider;
import com.google.inject.name.Named;

import javax.naming.Context;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 12/19/10
 * Time: 7:41 AM
 */
abstract public class AbstractJNDILookupModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JNDILookupFactory.class).toProvider(FactoryProvider.newFactory(JNDILookupFactory.class, JNDILookupImpl.class));
    }


    @Provides
    @Named(value = Context.INITIAL_CONTEXT_FACTORY)
    protected abstract String initialContextFactoryNameProvider();
}
