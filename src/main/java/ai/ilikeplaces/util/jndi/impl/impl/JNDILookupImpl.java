package ai.ilikeplaces.util.jndi.impl.impl;

import ai.ilikeplaces.util.jndi.impl.JNDILookup;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 12/19/10
 * Time: 7:25 AM
 */
public class JNDILookupImpl implements JNDILookup {

    final private Properties properties_;
    final private Context context_;
    final private String lookupValue_;

    @Inject
    public JNDILookupImpl(@Named(value = Context.INITIAL_CONTEXT_FACTORY) final String initialContextFactory, @Assisted String lookupValue) throws NamingException {
        properties_ = new Properties();
        properties_.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        context_ = new InitialContext(properties_);
        this.lookupValue_ = lookupValue;
    }

    public Object lookup() throws NamingException {
        return context_.lookup(lookupValue_);
    }
}
