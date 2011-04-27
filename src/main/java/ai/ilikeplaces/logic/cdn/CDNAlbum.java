package ai.ilikeplaces.logic.cdn;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.exception.DBOperationException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import com.rackspacecloud.client.cloudfiles.FilesConstants;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Okay, this class is supposed to do the following.
 * 1. Upload the users profile photo
 * 2. Ignore the fact that multiple threads upload photos for the same user
 * 3. As in point 2, the contract is that there is only one profile photo shareable across the internet
 * 4. If a photo exists on the cdn, override it.
 * 5. The photo name will be email@example.org
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Apr 29, 2010
 * Time: 4:10:12 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class})
public class CDNAlbum extends CDN implements CDNAlbumLocal {

    public static final String CONTAINER = "ALBUM_PHOTOS";
    private static final String ALBUM_PIVATE_EVENT_ID = "AlbumPivateEventId";
    private static final String ALBUM_PHOTO_UPLOAD_FAILED_DUE_TO_CACHING_ISSUES = "Album Photo Upload Failed Due To Caching Issues!";
    private static final String ALBUM_PHOTO_UPLOAD_FAILED_DUE_TO_I_O_ISSUES = "Album Photo Upload Failed Due To I/O Issues!";
    private static final String ALBUM_PHOTO_UPLOAD_FAILED = "Album Photo Upload Failed";
    private static final String ALBUM_PHOTO_UPLOAD_FAILED_DUE_TO_IMAGE_MANIPULATION_ISSUES = "Album Photo Upload Failed Due To Image Manipulation Issues!";
    private static final String ALBUM_PHOTO_UPLOAD_FAILED_DUE_TO_RENAMING_ISSUES = "Album Photo Upload Failed Due To Renaming Issues!";
    private static final String ALBUM_PHOTO_UPLOAD_SUCCESSFUL = "Album Photo Upload Successful.";
    private static final String UPLOADING_IMAGE = "Uploading Image";
    private static final String UPLOADING_IMAGE_THUMB = "Uploading Image Thumbnail";
    private static final String SAVING_SCALED_IMAGE = "Saving Scaled Image";
    private static final String SCALING_IMAGE = "Scaling Image";
    private static final String LOADING_IMAGE_AS_BUFFERED_IMAGE = "Loading Image As Buffered Image";
    public static final String THUMBNAIL = "th_";

    public static CDNAlbumLocal getAlbumPhotoCDNLocal() {
        isOK();
        CDNAlbumLocal h = null;
        try {
            h = (CDNAlbumLocal) Context_.lookup(CDNAlbumLocal.NAME);
        } catch (NamingException ex) {
            Loggers.EXCEPTION.error("Naming Exception", ex);
        }
        return h != null ? h : (CDNAlbumLocal) LogNull.logThrow();
    }

    @PostConstruct
    public void postConstruct() {
        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, "Login to rackspace cloud", 60000, null, true);
        final boolean status = doLogin();
        if (status) {
            sl.complete(Loggers.DONE);

        } else {
            sl.appendToLogMSG("Login Failed. Destroying Session Bean!");
            sl.complete(Loggers.FAILED);
            throw LOGIN_EXCEPTION;
        }
    }

    @PreDestroy
    public void preDestroy() {
    }

    @Override
    public Return<File> run(File file, final Map parameterMap, final String userFileExtension, final HttpSession session) {
        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, "Uploading Album Photo", 60000, null, true);
        Return<File> r;
        /**
         * Renaming the file to contain extension for image manipulation flexibility
         */
        try {
            File newFile = new File(file.getCanonicalPath() + "." + userFileExtension);
            final boolean rename = file.renameTo(newFile);

            if (!rename) {
                sl.complete("Rename Error!");
                return new ReturnImpl<File>(new RuntimeException("Rename Error"), "Rename Error!", true);
            }


            final SessionBoundBadRefWrapper<HumanUserLocal> s = (SessionBoundBadRefWrapper<HumanUserLocal>) session.getAttribute(ServletLogin.HumanUser);

            if (!s.isAlive()) {
                sl.complete("No Login!");
                r = new ReturnImpl<File>(ExceptionCache.NO_LOGIN, "Please login!", true);
            } else {
                final HumanId humanId = new HumanId(s.boundInstance.getHumanUserId()).getSelfAsValid();

                try {
                    try {
                        final String cdnFileName = newFile.getName();
                        sl.appendToLogMSG(UPLOADING_IMAGE);
                        final boolean uploaded = client.storeObjectAs(CONTAINER, newFile, FilesConstants.getMimetype(userFileExtension), cdnFileName);

                        sl.appendToLogMSG(LOADING_IMAGE_AS_BUFFERED_IMAGE);
                        BufferedImage bi = loadImage(newFile);

                        sl.appendToLogMSG(SCALING_IMAGE);
                        bi = scaleImage(bi, 190); //Reducing size of image to blueprintcss span-5 just to save bandwidth for the user.

                        sl.appendToLogMSG(SAVING_SCALED_IMAGE);
                        saveImage(bi, newFile);

                        final String cdnThumbFileName = THUMBNAIL + newFile.getName();
                        sl.appendToLogMSG(UPLOADING_IMAGE_THUMB);
                        final boolean uploadedThumb = client.storeObjectAs(CONTAINER, newFile, FilesConstants.getMimetype(userFileExtension), cdnThumbFileName);

                        if (uploaded && uploadedThumb) {
                            final boolean deleted = newFile.delete();
                            if (deleted) {
                                final Return<Album> dbr = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddEntryToAlbum(humanId, Long.parseLong((String) parameterMap.get(ALBUM_PIVATE_EVENT_ID)), new Obj<String>(cdnFileName).getSelfAsValid());
                                if (dbr.returnStatus() == 0) {
                                    sl.complete(Loggers.DONE);
                                    r = new ReturnImpl<File>(newFile, ALBUM_PHOTO_UPLOAD_SUCCESSFUL);
                                } else {
                                    r = new ReturnImpl<File>(new DBOperationException(dbr.returnError()), ALBUM_PHOTO_UPLOAD_FAILED_DUE_TO_I_O_ISSUES, true);
                                }
                            } else {
                                r = new ReturnImpl<File>(ExceptionCache.FILE_DELETE_FAILED, ALBUM_PHOTO_UPLOAD_FAILED_DUE_TO_CACHING_ISSUES, true);
                            }
                        } else {
                            r = new ReturnImpl<File>(ExceptionCache.CDN_FILE_UPLOAD_FAILED, ALBUM_PHOTO_UPLOAD_FAILED_DUE_TO_I_O_ISSUES, true);
                        }
                    } catch (final IOException e) {
                        r = new ReturnImpl<File>(e, ALBUM_PHOTO_UPLOAD_FAILED_DUE_TO_I_O_ISSUES, true);
                    }
                } catch (final RuntimeException e) {//This is for the deleteObject's returnBadly from DB return
                    r = new ReturnImpl<File>(e, ALBUM_PHOTO_UPLOAD_FAILED, true);
                } catch (final Exception e) {
                    r = new ReturnImpl<File>(e, ALBUM_PHOTO_UPLOAD_FAILED_DUE_TO_IMAGE_MANIPULATION_ISSUES, true);
                }

            }
        } catch (final IOException e) {
            r = new ReturnImpl<File>(e, ALBUM_PHOTO_UPLOAD_FAILED_DUE_TO_RENAMING_ISSUES, true);
        }
        return r;
    }
}