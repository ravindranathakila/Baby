package ai.util;

import ai.doc.License;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 1, 2010
 * Time: 4:26:30 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface Return<T> {

    public int returnStatus();

    public long returnErrorSeqCode();

    public T returnValue();

    /**
     * Use to optimistically obtain return value assuming there were no errors. This will throw a runtime exception if
     * errors had occured.
     *
     * @return
     */
    public T returnValueBadly();

    public Throwable returnError();

    public String returnMsg();

    /**
     * @return true if value of {@link #returnStatus()} == 0
     */
    public boolean valid();

    @Override
    public String toString();
}

