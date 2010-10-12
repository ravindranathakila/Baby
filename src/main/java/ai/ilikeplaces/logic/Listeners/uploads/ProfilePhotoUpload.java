package ai.ilikeplaces.logic.Listeners.uploads;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.FileUploadListenerFace;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.ReturnImpl;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jun 4, 2010
 * Time: 10:49:09 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Deprecated
public class ProfilePhotoUpload implements FileUploadListenerFace<File> {
    @Override
    public Return<File> run(final File file, final Map parameterMap, final String userFileExtension, final HttpSession session) {
        Loggers.DEBUG.debug("I need to upload this file to the CDN and then set the database values.");
        return new ReturnImpl<File>(file, "Upload not yet supported!");
    }
}
