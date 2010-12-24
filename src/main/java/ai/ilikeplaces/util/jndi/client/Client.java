package ai.ilikeplaces.util.jndi.client;

import ai.ilikeplaces.util.jndi.conf.SampleJNDILookupModule;
import ai.ilikeplaces.util.jndi.impl.JNDILookupFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 12/19/10
 * Time: 7:52 AM
 */
public class Client {
    public static void main(final String[] args){
        final Injector injector = Guice.createInjector(new SampleJNDILookupModule());
        final JNDILookupFactory jndiLookupFactory = injector.getInstance(JNDILookupFactory.class);
        jndiLookupFactory.getInstance("Class Name");
    }
}
