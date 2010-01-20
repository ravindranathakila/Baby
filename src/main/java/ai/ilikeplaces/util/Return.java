package ai.ilikeplaces.util;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 1, 2010
 * Time: 4:26:30 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface Return<T> {

    public int returnStatus();

    public T returnValue();

    public Throwable returnError();

    public String returnMsg();

    @Override
    public String toString();
}

