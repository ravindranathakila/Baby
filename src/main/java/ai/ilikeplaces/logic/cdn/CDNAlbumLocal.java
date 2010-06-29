package ai.ilikeplaces.logic.cdn;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.FileUploadListenerFace;

import javax.ejb.Local;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Apr 29, 2010
 * Time: 4:08:49 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CDNAlbumLocal extends FileUploadListenerFace<File>{

    public static final String NAME = CDNAlbumLocal.class.getSimpleName();
}