package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.rbs.RBGet;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 1, 2010
 * Time: 4:26:30 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface Return<T> {

    final static public String YIKES_SOMETHING_WENT_WRONG = RBGet.gui().getString("YIKES_SOMETHING_WENT_WRONG");

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

    @Override
    public String toString();
}

