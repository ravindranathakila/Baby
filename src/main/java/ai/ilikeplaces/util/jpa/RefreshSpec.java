package ai.ilikeplaces.util.jpa;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Type parameter signifies for which bean this spec will be used.
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 2/6/11
 * Time: 9:24 PM
 */
public class RefreshSpec  implements Serializable{

    final public List<String> fields;

    /**
     * @param fieldsOfBean Fields which need to be refreshed. This list will be unmodifiable.
     */
    public RefreshSpec(final List<String> fieldsOfBean) {
        this.fields = Collections.unmodifiableList(fieldsOfBean);
    }

    /**
     * @param fieldsOfBean Fields which need to be refreshed. This list will be unmodifiable.
     */
    public RefreshSpec(final String... fieldsOfBean) {
        this.fields = Collections.unmodifiableList(Arrays.asList(fieldsOfBean));
    }

    /**
     * @param fieldsOfBean      Fields which need to be refreshed
     * @param allowModification Whether or not to allow modification of this list.
     */
    public RefreshSpec(final List<String> fieldsOfBean, final boolean allowModification) {
        this.fields = allowModification ? fieldsOfBean : Collections.unmodifiableList(fieldsOfBean);
    }

    /**
     * @param allowModification Whether or not to allow modification of this list.
     * @param fieldsOfBean      Fields which need to be refreshed
     */
    public RefreshSpec(final boolean allowModification, final String... fieldsOfBean) {
        this.fields = allowModification ? Arrays.asList(fieldsOfBean) : Collections.unmodifiableList(Arrays.asList(fieldsOfBean));
    }

    @Override
    public String toString() {
        return "RefreshSpec{" +
                "fields=" + Arrays.toString(fields.toArray()) +
                '}';
    }
}
