package ai.ilikeplaces.entities.etc;

import ai.doc.License;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jul 6, 2010
 * Time: 2:35:56 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface RefreshData<T> {

    /**
     * Calling this method will refresh any lazily fetched lists in this entity making them availabe for use.
     *
     * @return T
     * @throws DBRefreshDataException in case the entity fails to refresh something inside it
     */
    @Deprecated
    public T refresh() throws DBRefreshDataException;
}
