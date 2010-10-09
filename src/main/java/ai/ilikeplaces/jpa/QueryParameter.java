package ai.ilikeplaces.jpa;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class QueryParameter {

    final private Map parameters_;

    @SuppressWarnings("unchecked")
    private QueryParameter(final String name__, final Object value__) {
        this.parameters_ = new IdentityHashMap();
        this.parameters_.put(name__, value__);
    }

    private QueryParameter() {
        this.parameters_ = new IdentityHashMap();
    }

    /**
     * @param name__
     * @param value__
     * @return QueryParameter
     */
    @Deprecated
    @FIXME(issue = "Bug prone as a user might iteratively use with.with.with and forget he has to use and on consecutive calls. Use with() instead")
    public static QueryParameter with(final String name__, final Object value__) {
        return newInstance().and(name__, value__);
    }

    /**
     * @return QueryParameter
     */
    public static QueryParameter newInstance() {
        return new QueryParameter();
    }

    /**
     * @param name__
     * @param value__
     * @return QueryParameter
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public QueryParameter and(final String name__, final Object value__) {
        return add(name__, value__);
    }

    /**
     * @param name__
     * @param value__
     * @return QueryParameter
     */
    @SuppressWarnings("unchecked")
    public QueryParameter add(final String name__, final Object value__) {
        this.parameters_.put(name__, value__);
        return this;
    }

    /**
     * @return parameters_
     */
    public Map parameters() {
        return this.parameters_;
    }
}


