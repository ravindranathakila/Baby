package ai.ilikeplaces.logic.cdn;

import ai.doc.License;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.exception.DBOperationException;
import ai.ilikeplaces.logic.Listeners.widgets.UserProperty;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import com.rackspacecloud.client.cloudfiles.FilesConstants;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.interceptor.Interceptors;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
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
public class CDNProfilePhoto extends CDN implements CDNProfilePhotoLocal {
// ------------------------------ FIELDS ------------------------------

    public static final String CONTAINER = "PROFILE_PHOTOS";
    public static final String NAMING_EXCEPTION = "Naming Exception";
    private static final String DELETING_OLD_PROFILE_IMAGE = "Deleting Old Profile Image";
    private static final String DELETING_OLD_PROFILE_IMAGE_FAILED_BUT_PROCEEDING_REASON_FOR_FAILURE = "Deleting Old Profile Image Failed, But Proceeding. Reason for Failure:";
    private static final String LOADING_IMAGE_AS_BUFFERED_IMAGE = "Loading Image As Buffered Image";
    private static final String LOGIN_FAILED_DESTROYING_SESSION_BEAN = "Login Failed. Destroying Session Bean!";
    private static final String LOGIN_TO_RACKSPACE_CLOUD = "Login to rackspace cloud";
    private static final String NO_LOGIN = "No Login!";
    private static final String PLEASE_LOGIN = "Please login!";
    private static final String POSTERIZING = "Posterizing";
    private static final String PROFILE_PHOTO_UPLOAD_FAILED = "Profile Photo Upload Failed!";
    private static final String PROFILE_PHOTO_UPLOAD_FAILED_DUE_TO_CACHING_ISSUES = "Profile Photo Upload Failed Due To Caching Issues!";
    private static final String PROFILE_PHOTO_UPLOAD_FAILED_DUE_TO_I_O_ISSUES = "Profile Photo Upload Failed Due To I/O Issues!";
    private static final String PROFILE_PHOTO_UPLOAD_FAILED_DUE_TO_IMAGE_MANIPULATION_ISSUES = "Profile Photo Upload Failed Due To Image Manipulation Issues!";
    private static final String PROFILE_PHOTO_UPLOAD_FAILED_DUE_TO_RENAMING_ISSUES = "Profile Photo Upload Failed Due To Renaming Issues!";
    private static final String PROFILE_PHOTO_UPLOAD_SUCCESSFUL = "Profile Photo Upload Successful.";
    private static final String RENAME_ERROR = "Rename Error";
    private static final String SAVING_POSTERIZED_IMAGE = "Saving Posterized Image";
    private static final String SAVING_SCALED_IMAGE = "Saving Scaled Image";
    private static final String SCALING_IMAGE = "Scaling Image";
    private static final String UPLOADING_IMAGE = "Uploading Image";
    private static final String UPLOADING_PROFILE_PHOTO = "Uploading Profile Photo";

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface FileUploadListenerFace ---------------------

    @Override
    public Return<File> run(File file, final Map parameterMap, final String userFileExtension, final HttpSession session) {
        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, UPLOADING_PROFILE_PHOTO, 120000, null, true);
        Return<File> r;
        /**
         * Renaming the file to contain extension for image manipulation flexibility
         */
        try {
            File newFile = new File(file.getCanonicalPath() + "." + userFileExtension);
            final boolean rename = file.renameTo(newFile);

            if (!rename) {
                sl.complete(RENAME_ERROR);
                return new ReturnImpl<File>(new RuntimeException(RENAME_ERROR), RENAME_ERROR, true);
            }


            final SessionBoundBadRefWrapper<HumanUserLocal> s = (SessionBoundBadRefWrapper<HumanUserLocal>) session.getAttribute(ServletLogin.HumanUser);

            if (!s.isAlive()) {
                sl.complete(NO_LOGIN);
                r = new ReturnImpl<File>(ExceptionCache.NO_LOGIN, PLEASE_LOGIN, true);
            } else {
                final HumanId humanId = new HumanId(s.getBoundInstance().getHumanUserId());

                try {
                    sl.appendToLogMSG(LOADING_IMAGE_AS_BUFFERED_IMAGE);
                    BufferedImage bi = loadImage(newFile);

                    sl.appendToLogMSG(SCALING_IMAGE);
                    bi = scaleImage(bi, 300); //Reducing size of image to blueprintcss span-5 just to save bandwidth for the user.

                    sl.appendToLogMSG(SAVING_SCALED_IMAGE);
                    saveImage(bi, newFile);

                    /*sl.appendToLogMSG(POSTERIZING);
                    bi = new Sampler().run(newFile.getCanonicalPath());

                    sl.appendToLogMSG(SAVING_POSTERIZED_IMAGE);
                    saveImage(bi, newFile);*/

                    try {
                        final String cdnFileName = newFile.getName();
                        sl.appendToLogMSG(UPLOADING_IMAGE);
                        final boolean uploaded = client.storeObjectAs(CONTAINER, newFile, FilesConstants.getMimetype(userFileExtension), cdnFileName);
                        sl.appendToLogMSG(DELETING_OLD_PROFILE_IMAGE);
                        try {
                            client.deleteObject(CONTAINER, DB.getHumanCRUDHumanLocal(true).doDirtyRHumansProfilePhoto(humanId).returnValueBadly());
                        } catch (final Exception e) {
                            sl.appendToLogMSG(DELETING_OLD_PROFILE_IMAGE_FAILED_BUT_PROCEEDING_REASON_FOR_FAILURE + e.getMessage());
                        }
                        if (uploaded) {
                            final boolean deleted = newFile.delete();
                            if (!deleted) {
                                r = new ReturnImpl<File>(ExceptionCache.FILE_DELETE_FAILED, PROFILE_PHOTO_UPLOAD_FAILED_DUE_TO_CACHING_ISSUES, true);
                            } else {
                                final Return<HumansIdentity> dbr = DB.getHumanCRUDHumanLocal(true).doUHumansProfilePhoto(humanId, cdnFileName);
                                r = dbr.returnStatus() == 0 ?
                                        new ReturnImpl<File>(newFile, PROFILE_PHOTO_UPLOAD_SUCCESSFUL)
                                        : new ReturnImpl<File>(new DBOperationException(dbr.returnError()), PROFILE_PHOTO_UPLOAD_FAILED_DUE_TO_I_O_ISSUES, true);

                                if (dbr.returnStatus() == 0) {
                                    UserProperty.HUMANS_IDENTITY_CACHE.MAP.put(new String(dbr.returnValue().getHumanId()), dbr.returnValue());
                                }
                            }
                        } else {
                            r = new ReturnImpl<File>(ExceptionCache.CDN_FILE_UPLOAD_FAILED, PROFILE_PHOTO_UPLOAD_FAILED_DUE_TO_I_O_ISSUES, true);
                        }
                    } catch (final IOException e) {
                        r = new ReturnImpl<File>(e, PROFILE_PHOTO_UPLOAD_FAILED_DUE_TO_I_O_ISSUES, true);
                    }
                } catch (final RuntimeException e) {//This is for the deleteObject's returnBadly from DB return
                    r = new ReturnImpl<File>(e, PROFILE_PHOTO_UPLOAD_FAILED, true);
                } catch (final Exception e) {
                    r = new ReturnImpl<File>(e, PROFILE_PHOTO_UPLOAD_FAILED_DUE_TO_IMAGE_MANIPULATION_ISSUES, true);
                }
            }
        } catch (final IOException e) {
            r = new ReturnImpl<File>(e, PROFILE_PHOTO_UPLOAD_FAILED_DUE_TO_RENAMING_ISSUES, true);
        }
        return r;
    }

// ------------------------ CANONICAL METHODS ------------------------


// -------------------------- STATIC METHODS --------------------------

    /**
     * http://www.javalobby.org/articles/ultimate-image/
     *
     * @param img
     * @param newW
     * @param newH
     * @return
     */
    public static BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
        final int w = img.getWidth();
        final int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    /**
     * http://www.javalobby.org/articles/ultimate-image/
     * <p/>
     * modified by me to scale image as per web page requirement of 190px of width. Thats it.
     * Hence, this accepts only the width parameter and will scale the hight proportaionaltely
     *
     * @param img
     * @param newWidth
     * @return
     */
    public static BufferedImage scaleImage(final BufferedImage img, int newWidth) {
        final int oldWidth = img.getWidth();
        final int oldHeight = img.getHeight();

        final int newHeight = (int) Math.round(((double) newWidth / (double) oldWidth) * (double) oldHeight);

        final BufferedImage dimg = new BufferedImage(newWidth, newHeight, img.getType());
        final Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newWidth, newHeight, 0, 0, oldWidth, oldHeight, null);
        g.dispose();
        return dimg;
    }

    public static CDNProfilePhotoLocal getProfilePhotoCDNLocal() {
        isOK();
        CDNProfilePhotoLocal h = null;
        try {
            h = (CDNProfilePhotoLocal) Context_.lookup(CDNProfilePhotoLocal.NAME);
        } catch (NamingException ex) {
            Loggers.EXCEPTION.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (CDNProfilePhotoLocal) LogNull.logThrow();
    }

    /**
     * http://www.javalobby.org/articles/ultimate-image/
     * <p/>
     * Saves a BufferedImage to the given file, pathname must not have any
     * periods "." in it except for the one before the format, i.e. C:/images/fooimage.png
     * <p/>
     *
     * @param img
     * @param ref
     */
    public static void saveImage(final BufferedImage img, final File ref) throws IOException {
        try {
            //final String format = (ref.endsWith(".png")) ? "png" : "jpg";
            //ImageIO.write(img, format, new File(ref));
            ImageIO.write(img, ref.getName().substring(ref.getName().lastIndexOf(".") + 1), ref);
        } catch (final IOException e) {
            Loggers.EXCEPTION.error(Loggers.EMBED, e);
            throw e;
        }
    }

// -------------------------- OTHER METHODS --------------------------

    //Use the LookupOp class from the Java 2D API along
    // with three separate data arrays to process the
    // color values in the selected color bands.  The
    // alpha value is not modified.  This is a common method
    // that is called by the code that processes each
    // individual page in the tabbed pane.

    BufferedImage processImageForThePage(
            BufferedImage theImage,
            short[] red,
            short[] green,
            short[] blue) {
        //Create and populate a 2D array with data for the
        // lookup table.  Note that this is a 2D array, rather
        // than a 1D array, which is the case when a single
        // data array is used to process all three color bands.
        short[][] lookupData = new short[][]{red, green, blue};

        //Create the lookup table.  The first parameter is an
        // offset for extracting data from the array object.
        //In this case, all of the data is extracted from the
        // array object beginning at an index of 0.
        ShortLookupTable lookupTable =
                new ShortLookupTable(0, lookupData);

        //Create the filter object. The second parameter
        // provides the opportunity to use RenderingHints.
        BufferedImageOp filterObject =
                new LookupOp(lookupTable, null);


        //Apply the filter to the incoming image and return
        // a reference to the resulting BufferedImage object.
        // The second parameter can optionally specify an
        // existing BufferedImage object to serve as a
        // destination for the processed image.
        return filterObject.filter(theImage, null);
    }//end processImageForThePage

    @PostConstruct
    public void postConstruct() {
        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, LOGIN_TO_RACKSPACE_CLOUD, 60000, null, true);
        final boolean status = doLogin();
        if (status) {
            sl.complete(Loggers.DONE);
        } else {
            sl.appendToLogMSG(LOGIN_FAILED_DESTROYING_SESSION_BEAN);
            sl.complete(Loggers.FAILED);
            throw LOGIN_EXCEPTION;
        }
    }

    //-----------------------------------------------------//


//    /**
//     * http://www.javalobby.org/articles/ultimate-image/
//     *
//     * @param ref
//     * @return null or buffered image
//     * @throws Exception
//     */
//    public static BufferedImage loadImage(final File ref) throws Exception {
//        try {
//            Loggers.DEBUG.debug("Loading Image From:" + ref.getCanonicalPath());
//            return ImageIO.read(ref);
//        } catch (final Exception e) {
//            Loggers.EXCEPTION.error(Loggers.EMBED, e);
//            throw e;
//        }
//    }


//    /**
//     * http://www.javalobby.org/articles/ultimate-image/
//     *
//     * @param ref
//     * @param img
//     */
//    public static void saveOldWay(final String ref, final BufferedImage img) {
//        BufferedOutputStream out;
//        try {
//            out = new BufferedOutputStream(new FileOutputStream(ref));
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(img);
//            int quality = 5;
//            quality = Math.max(0, Math.min(quality, 100));
//            param.setQuality((float) quality / 100.0f, false);
//            encoder.setJPEGEncodeParam(param);
//            encoder.encode(img);
//            out.close();
//        } catch (final Exception e) {
//            Loggers.EXCEPTION.error(Loggers.EMBED, e);
//        }
//    }
}
