package ai.baby.logic.modules.conf;

import ai.baby.rbs.RBGet;
import ai.baby.util.jndi.conf.AbstractJNDILookupModule;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 12/19/10
 * Time: 9:30 AM
 */
public class JNDILookupModule extends AbstractJNDILookupModule {

    private static final String INITIAL_CONTEXT_FACTORY = RBGet.globalConfig.getString("oejb.LICF");

    @Override
    protected String initialContextFactoryNameProvider() {
        return INITIAL_CONTEXT_FACTORY;
    }
}
