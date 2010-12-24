package ai.ilikeplaces.logic.modules;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.modules.conf.JNDILookupModule;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.MethodParams;
import ai.ilikeplaces.util.MethodTimer;
import ai.ilikeplaces.util.ParamValidator;
import ai.ilikeplaces.util.jndi.impl.JNDILookupFactory;
import ai.ilikeplaces.ygd.conf.YahooGeoPlanetClientModule;
import ai.ilikeplaces.ygp.impl.ClientFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.interceptor.Interceptors;
import javax.naming.NamingException;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Singleton
@Startup
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class})
public class Modules extends AbstractSLBCallbacks implements ModulesLocal {


    /**
     * Only the jndi factory should be done statically. Do NOT initialize other modules like this.
     */
    final static Injector jndiLookupFactoryInjector = Guice.createInjector(new JNDILookupModule());

    /**
     * Only the jndi factory should be done statically. Do NOT initialize other modules like this.
     */
    final static JNDILookupFactory jndiLookupFactory = jndiLookupFactoryInjector.getInstance(JNDILookupFactory.class);

    private final ClientFactory yahooGeoPlanetFactory;

    public Modules() {
        {
            final Injector yahooGeoPlanetFactoryInjector = Guice.createInjector(new YahooGeoPlanetClientModule());
            yahooGeoPlanetFactory = yahooGeoPlanetFactoryInjector.getInstance(ClientFactory.class);
        }
    }

    public static ModulesLocal getModules(){
        try {
            return (ModulesLocal) (jndiLookupFactory.getInstance(ModulesLocal.NAME)).lookup();
        } catch (final NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public ClientFactory getYahooGeoPlanetFactory() {
        return yahooGeoPlanetFactory;
    }

    @PostConstruct
    public void postConstruct() {

    }

    @PreDestroy
    public void preDestroy() {

    }

}