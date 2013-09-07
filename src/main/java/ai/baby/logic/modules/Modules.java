package ai.baby.logic.modules;

import ai.baby.logic.modules.conf.JNDILookupModule;
import ai.baby.util.AbstractSLBCallbacks;
import ai.baby.util.MethodParams;
import ai.baby.util.MethodTimer;
import ai.baby.util.ParamValidator;
import ai.baby.util.jndi.impl.JNDILookupFactory;
import ai.scribble.License;
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

    private final api.foursquare.com.impl.ClientFactory foursquareFactory;

    public Modules() {
        {
            final Injector foursquareFactoryInjector = Guice.createInjector(new FoursquareClientModule());
            foursquareFactory = foursquareFactoryInjector.getInstance(api.foursquare.com.impl.ClientFactory.class);
        }
    }

    public static ModulesLocal getModules() {
        try {
            return (ModulesLocal) (jndiLookupFactory.getInstance(NAME)).lookup();
        } catch (final NamingException e) {
            throw new RuntimeException(e);
        }
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
