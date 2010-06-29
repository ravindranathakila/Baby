package ai.ilikeplaces.logic.cdn;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.exception.DBOperationException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import com.rackspacecloud.client.cloudfiles.FilesConstants;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.interceptor.Interceptors;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import java.awt.*;
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
 * User: Ravindranath Akila
 * Date: Apr 29, 2010
 * Time: 4:10:12 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class})
public class CDNAlbum extends CDN implements CDNAlbumLocal {

    public static final String CONTAINER = null;

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

        }else{
            sl.appendToLogMSG("Login Failed. Destroying Session Bean!");
            sl.complete(Loggers.FAILED);
            this.preDestroy();
        }
    }

    @PreDestroy
    public void preDestroy(){
    }

    @Override
    public Return<File> run(File file, final Map parameterMap, final String userFileExtension, final HttpSession session) {
        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, "Uploading Album Photo", 120000, null, true);
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
                final HumanId humanId = new HumanId(s.boundInstance.getHumanUserId());

                try {
                    sl.appendToLogMSG("Loading Image As Buffered Image");
                    BufferedImage bi = loadImage(newFile);

                    sl.appendToLogMSG("Scaling Image");
                    bi = scaleImage(bi, 190); //Reducing size of image to blueprintcss span-5 just to save bandwidth for the user.

                    sl.appendToLogMSG("Saving Scaled Image");
                    saveImage(bi, newFile);

                    try {
                        final String cdnFileName = newFile.getName();
                        sl.appendToLogMSG("Uploading Image");
                        final boolean uploaded = client.storeObjectAs(CONTAINER, newFile, FilesConstants.getMimetype(userFileExtension), cdnFileName);
                        if (uploaded) {
                            final boolean deleted = newFile.delete();
                            if (deleted) {
                                final Return<Album> dbr = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddEntryToAlbum(humanId, -1);
                                r = dbr.returnStatus() == 0 ?
                                        new ReturnImpl<File>(newFile, "Album Photo Upload Successful.")
                                        : new ReturnImpl<File>(new DBOperationException(dbr.returnError()), "Album Photo Upload Failed Due To I/O Issues!", true);
                            } else {
                                r = new ReturnImpl<File>(ExceptionCache.FILE_DELETE_FAILED, "Album Photo Upload Failed Due To Caching Issues!", true);
                            }
                        } else {
                            r = new ReturnImpl<File>(ExceptionCache.CDN_FILE_UPLOAD_FAILED, "Album Photo Upload Failed Due To I/O Issues!", true);
                        }
                    } catch (final IOException e) {
                        r = new ReturnImpl<File>(e, "Album Photo Upload Failed Due To I/O Issues!", true);
                    }
                } catch (final RuntimeException e) {//This is for the deleteObject's returnBadly from DB return
                    r = new ReturnImpl<File>(e, "Album Photo Upload Failed Due Failure In Deletion Of Old Album Image!", true);
                } catch (final Exception e) {
                    r = new ReturnImpl<File>(e, "Album Photo Upload Failed Due To Image Manipulation Issues!", true);
                }

            }
        } catch (final IOException e) {
            r = new ReturnImpl<File>(e, "Album Photo Upload Failed Due To Renaming Issues!", true);
        }
        return r;
    }
}