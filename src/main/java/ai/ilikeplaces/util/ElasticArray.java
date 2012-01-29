package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

import java.util.Arrays;

/**
 * Please let us know if you have a faster/memorable :P implementation.
 *
 * I doubt if this approach is consistent, but over time, there are no complains :)
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Oct 10, 2010
 * Time: 7:45:55 PM
 */
@License(content = "This code is licensed under Apache License Version 2.0")
public class ElasticArray<T> {
    public T[] array;

    public ElasticArray(final T[] array) {
        this.array = array;
    }

    public T[] getArray() {
        return array;
    }

    public ElasticArray<T> setArray(final T[] array) {
        this.array = array;
        return this;
    }

    /**
     * Not synchronized! Please use a synchronized block if you do concurrent operations, or use public void addEntriesInSync(final T entryToBeAdded)
     *
     * @param entryToBeAdded Entry To Be Added
     */
    public void addEntries(final T entryToBeAdded) {
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = entryToBeAdded;
    }

    /**
     * @param entryToBeAdded Entry To Be Added
     */
    public void addEntriesInSync(final T entryToBeAdded) {
        synchronized (this) {
            array = Arrays.copyOf(array, array.length + 1);
            array[array.length - 1] = entryToBeAdded;
        }
    }
}
