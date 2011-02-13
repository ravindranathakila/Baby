package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.exception.DBFetchDataException;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jul 6, 2010
 * Time: 2:35:56 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface RefreshData<T>{

    /**
     * Calling this method will refresh any lazily fetched lists in this entity making them availabe for use.
     *
     * @throws DBFetchDataException in case the entity fails to refresh something inside it
     * @return T
     */
    @Deprecated
    public T refresh() throws DBFetchDataException;
}
