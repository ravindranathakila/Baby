package ai.ilikeplaces.logic.cdn;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.Loggers;
import com.rackspacecloud.client.cloudfiles.FilesClient;

import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static ai.ilikeplaces.util.Loggers.EXCEPTION;

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class CDN extends AbstractSLBCallbacks {//This classes is designed to be used with stateless beans connecting to containers

    protected FilesClient client = null;
    protected final static Properties P_ = new Properties();
    protected static Context Context_ = null;
    protected static boolean OK_ = false;
    protected final static String ICF = RBGet.globalConfig.getString("oejb.LICF");
    private static final String LOGIN_FAILED = "LOGIN TO RACKSPACE FOR FILE UPLOAD FAILED. THIS EXCEPTION WILL DESTROY THIS INSTANCE";
    protected static final RuntimeException LOGIN_EXCEPTION = new RuntimeException(LOGIN_FAILED);

    static {
        try {
            CDN.P_.put(Context.INITIAL_CONTEXT_FACTORY, CDN.ICF);
            CDN.Context_ = new InitialContext(P_);
            CDN.OK_ = true;
        } catch (NamingException ex) {
            CDN.OK_ = false;
            EXCEPTION.error("{}", ex);
        }
    }

    protected boolean doLogin() {

        try {
            client = new FilesClient("rakila", RBGet.globalConfig.getString("CL0UD_K3Y"), null);
            boolean result = client.login();

            if (result) {
                Loggers.log(Loggers.LEVEL.SERVER_STATUS, Loggers.EMBED, "Logged into Rackspace!");
            } else {
                Loggers.log(Loggers.LEVEL.SERVER_STATUS, Loggers.EMBED, "Login into Rackspace FAILED!");
            }

            return result;
        } catch (final IOException e) {
            Loggers.log(Loggers.LEVEL.ERROR, Loggers.EMBED, e);
            return false;
        }
    }

    static public void isOK() {
        if (!OK_) {
            throw new IllegalStateException("SORRY! MAIL SESSION BEAN INITIALIZATION FAILED!");
        }
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
            EXCEPTION.error(Loggers.EMBED, e);
            throw e;
        }
    }

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

    /**
     * http://www.javalobby.org/articles/ultimate-image/
     *
     * @param ref
     * @return null or buffered image
     * @throws Exception
     */
    public static BufferedImage loadImage(final File ref) throws Exception {
        try {
            Loggers.DEBUG.debug("Loading Image From:" + ref.getCanonicalPath());
            return ImageIO.read(ref);
        } catch (final Exception e) {
            EXCEPTION.error(Loggers.EMBED, e);
            throw e;
        }
    }




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