package ai.ilikeplaces.util.jndi.conf;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 12/19/10
 * Time: 7:54 AM
 */
public class SampleJNDILookupModule extends AbstractJNDILookupModule{
    @Override
    protected String initialContextFactoryNameProvider() {
        return this.getClass().getName();
    }
}
