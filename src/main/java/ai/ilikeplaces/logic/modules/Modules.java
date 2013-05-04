package ai.ilikeplaces.logic.modules;

import ai.ilikeplaces.logic.modules.conf.JNDILookupModule;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.MethodParams;
import ai.ilikeplaces.util.MethodTimer;
import ai.ilikeplaces.util.ParamValidator;
import ai.ilikeplaces.util.jndi.impl.JNDILookupFactory;
import ai.scribble.License;
import api.eventful.com.impl.ClientFactory;
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

    private final ai.ilikeplaces.ygp.impl.ClientFactory yahooGeoPlanetFactory;

    private final com.disqus.api.impl.ClientFactory disqusApiFactory;

    private final com.google.places.api.impl.ClientFactory googlePlacesApiFactory;

    @Deprecated
    private final upcoming.yahoo.api.impl.ClientFactory yahooUplcomingFactory;

    private final ClientFactory eventulFactory;

    private final api.foursquare.com.impl.ClientFactory foursquareFactory;

    public Modules() {
        {
            final Injector yahooGeoPlanetFactoryInjector = Guice.createInjector(new YahooGeoPlanetClientModule());
            yahooGeoPlanetFactory = yahooGeoPlanetFactoryInjector.getInstance(ai.ilikeplaces.ygp.impl.ClientFactory.class);
        }
        {
            final Injector disqusApiFactoryInjector = Guice.createInjector(new DisqusAPIClientModule());
            disqusApiFactory = disqusApiFactoryInjector.getInstance(com.disqus.api.impl.ClientFactory.class);
        }
        {
            final Injector googlePlacesApiFactoryInjector = Guice.createInjector(new GooglePlacesAPIClientModule());
            googlePlacesApiFactory = googlePlacesApiFactoryInjector.getInstance(com.google.places.api.impl.ClientFactory.class);
        }
        {
            final Injector yahooUpcomingFactoryInjector = Guice.createInjector(new YahooUpcomingClientModule());
            yahooUplcomingFactory = yahooUpcomingFactoryInjector.getInstance(upcoming.yahoo.api.impl.ClientFactory.class);
        }
        {
            final Injector eventfulFactoryInjector = Guice.createInjector(new EventfulClientModule());
            eventulFactory = eventfulFactoryInjector.getInstance(api.eventful.com.impl.ClientFactory.class);
        }
        {
            final Injector foursquareFactoryInjector = Guice.createInjector(new FoursquareClientModule());
            foursquareFactory = foursquareFactoryInjector.getInstance(api.foursquare.com.impl.ClientFactory.class);
        }
    }

    public static ModulesLocal getModules() {
        try {
            return (ModulesLocal) (jndiLookupFactory.getInstance(ModulesLocal.NAME)).lookup();
        } catch (final NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public ai.ilikeplaces.ygp.impl.ClientFactory getYahooGeoPlanetFactory() {
        return yahooGeoPlanetFactory;
    }

    public upcoming.yahoo.api.impl.ClientFactory getYahooUplcomingFactory() {
        return yahooUplcomingFactory;
    }

    public com.disqus.api.impl.ClientFactory getDisqusAPIFactory() {
        return disqusApiFactory;
    }

    public com.google.places.api.impl.ClientFactory getGooglePlacesAPIFactory() {
        return googlePlacesApiFactory;
    }

    public api.eventful.com.impl.ClientFactory getEventulFactory() {
        return eventulFactory;
    }

    public api.foursquare.com.impl.ClientFactory getFoursquareFactory() {
        return foursquareFactory;
    }

    @PostConstruct
    public void postConstruct() {

    }

    @PreDestroy
    public void preDestroy() {

    }

}
