package ai.ilikeplaces.util;

import ai.doc.License;

/**
 * Simple Key Value Pair.
 * <p/>
 * <p/>
 * Example Usage:
 * <p/>
 * <p/>
 * <code>
 * System.out.println(new Pair<String, Integer>().setPair("Susan Boyle", 1).toString());
 * </code>
 * <p/>
 * <code>
 * System.out.println(new Pair<String, Integer>().setKey("Susan Boyle").setValue(1).toString());
 * </code>
 * <p/>
 * <code>
 * System.out.println(new Pair<String, Integer>("Susan Boyle", 1).toString());
 * </code>
 * <p/>
 * <p/>
 * <p/>
 * {@link #equals(Object)} checks only on key for equality
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Oct 10, 2010
 * Time: 5:36:16 PM
 */
@License(content = "This code is licensed under Apache License Version 2.0")
public class Pair<K, V> {
    private K key;
    private V value;
    private static final String OPEN_BRACE = "{";
    private static final String COMMA = ",";
    private static final String CLOSE_BRACE = "}";

    public Pair() {
    }

    /**
     * @param key   key to be set
     * @param value value to be set
     */
    public Pair(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @param key   key to be set
     * @param value value to be set
     * @return this
     */
    public Pair<K, V> setPair(final K key, final V value) {
        this.key = key;
        this.value = value;
        return this;
    }

    /**
     * @return key
     */
    public K getKey() {
        return key;
    }

    /**
     * @param key key to be set
     * @return this
     */
    public Pair<K, V> setKey(final K key) {
        this.key = key;
        return this;
    }

    /**
     * @return value
     */
    public V getValue() {
        return value;
    }

    /**
     * @param value value to be set
     * @return this
     */
    public Pair<K, V> setValue(final V value) {
        this.value = value;
        return this;
    }

    public String toString() {
        return OPEN_BRACE + key + COMMA + value + CLOSE_BRACE;
    }

    /**
     * @return {key,val} as string
     */
    public String toTuple() {
        return OPEN_BRACE + key + COMMA + value + CLOSE_BRACE;
    }

    public static void main(String args[]) {
        System.out.println(new Pair<String, Integer>().setPair("Susan Boyle", 1).toString());

        System.out.println(new Pair<String, Integer>().setKey("Susan Boyle").setValue(1).toString());

        System.out.println(new Pair<String, Integer>("Susan Boyle", 1).toString());
    }

    /**
     * Matches checked only on key
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (key != null ? !key.equals(pair.key) : pair.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
