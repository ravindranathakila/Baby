package ai.ilikeplaces.logic.cdn;

import ai.doc.License;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.SmartLogger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import static ai.ilikeplaces.util.Loggers.EXCEPTION;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Sep 25, 2010
 * Time: 9:56:24 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class PosterizeTester {

    private static final String LOADING_IMAGE_AS_BUFFERED_IMAGE = "Loading Image As Buffered Image";
    private static final String SCALING_IMAGE = "Scaling Image";
    private static final String SAVING_SCALED_IMAGE = "Saving Scaled Image";


    public static void main(final String args[]) {

        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, "Uploading Profile Photo", 120000, null, true);
        Return<File> r;
        /**
         * Renaming the file to contain extension for image manipulation flexibility
         */
        File newFile = new File("C:\\posterize\\22036_330336595855_726280855_5172426_6354785_n.jpg");

        try {
            sl.appendToLogMSG(LOADING_IMAGE_AS_BUFFERED_IMAGE);
            BufferedImage bi = loadImage(newFile);

            BufferedImage dstImg = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.OPAQUE);
            ColorSpace css = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);

            ColorConvertOp theOp = new ColorConvertOp(css, null);

            theOp.filter(bi, dstImg);
//
//            //sl.appendToLogMSG(SCALING_IMAGE);
//            //bi = scaleImage(bi, 190); //Reducing size of image to blueprintcss span-5 just to save bandwidth for the user.
//
//            int numberLevels = 1;
//
//            int binSize = 256 / numberLevels;
//
//            short[] red = new short[256];
//            short[] green = new short[256];
//            short[] blue = new short[256];
//            short[] masterData = new short[256];
//            for (int cnt = 0; cnt < 256; cnt++) {
//                short value = (short) ((cnt / binSize) * binSize + binSize - 1);
//                //Clip the values at 0 and 255.
//                if (value >= 256) { value = 255;
//                }
//                if (value < 0) {
//                    value = 0;
//                }//Probably not possible.
//                masterData[cnt] = value;
//            }//end for loop
//
////            red = masterData;
////            green = masterData;
////            blue = masterData;
//
//            bi = processImageForThePage(bi, red, green, blue);
//
//
//            sl.appendToLogMSG(SAVING_SCALED_IMAGE);
            saveImage(dstImg, new File("C:\\posterize\\output.jpg"));
        } catch (final Exception e) {

            sl.complete(e.getMessage());
            throw new RuntimeException(e);

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
            Loggers.EXCEPTION.error(Loggers.EMBED, e);
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


    //Use the LookupOp class from the Java 2D API along
    // with three separate data arrays to process the
    // color values in the selected color bands.  The
    // alpha value is not modified.  This is a common method
    // that is called by the code that processes each
    // individual page in the tabbed pane.

    final static BufferedImage processImageForThePage(
            BufferedImage src,
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
        return filterObject.filter(src, null);
    }//end processImageForThePage
    //-----------------------------------------------------//

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

}
