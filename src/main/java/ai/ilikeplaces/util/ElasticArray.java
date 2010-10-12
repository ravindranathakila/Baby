package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Oct 10, 2010
 * Time: 7:45:55 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
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

    public void addEntries(final T entryToBeAdded) {
        synchronized (this) {
            array = Arrays.copyOf(array, array.length + 1);
            array[array.length - 1] = entryToBeAdded;
        }
    }
}
