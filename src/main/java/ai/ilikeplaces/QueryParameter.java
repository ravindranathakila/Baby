package ai.ilikeplaces;

import java.util.*;

/**
 *
 * @author Ravindranath Akila
 */
final public class QueryParameter {

    final private Map parameters_;

    @SuppressWarnings("unchecked")
    private QueryParameter(final String name__, final Object value__) {
        this.parameters_ = new IdentityHashMap();
        this.parameters_.put(name__, value__);
    }

    /**
     *
     * @param name__
     * @param value__
     * @return QueryParameter
     */
    public static QueryParameter with(final String name__, final Object value__) {
        return new QueryParameter(name__, value__);
    }

    /**
     *
     * @param name__
     * @param value__
     * @return QueryParameter
     */
    @SuppressWarnings("unchecked")
    public QueryParameter and(final String name__, final Object value__) {
        this.parameters_.put(name__, value__);
        return this;
    }

    /**
     *
     * @return parameters_
     */
    public Map parameters() {
        return this.parameters_;
    }
}


