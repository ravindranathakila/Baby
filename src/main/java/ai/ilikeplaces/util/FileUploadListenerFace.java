package ai.ilikeplaces.util;

import ai.doc.License;
import ai.util.Return;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Map;

/**
 * This interface receives a file upload request form a servlet
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jun 3, 2010
 * Time: 10:21:48 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface FileUploadListenerFace<T> {

    public Return<T> run(final File file, final Map parameterMap, final String userFileExtension, final HttpSession session);
}
