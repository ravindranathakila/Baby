package ai.ilikeplaces.logic.cdn;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Source: http://www.java2s.com/Code/Java/2D-Graphics-GUI/Imagedemo.htm
 */
public class Sampler {
    private Frame mImageFrame;

    private SplitImageComponent mSplitImageComponent;

    private Hashtable mOps;

    public static void main(String[] args) {
        //String imageFile = "C:\\posterize\\22036_330336595855_726280855_5172426_6354785_n.jpg";
        //String imageFile = "C:\\posterize\\25510_419307415855_726280855_5734986_3496079_n.jpg";
        String imageFile = "C:\\posterize\\output.jpg";
        try {
            saveImage(new Sampler().run(imageFile), new File("C:\\posterize\\output.jpg"));
        } catch (final Exception e) {
            e.printStackTrace(System.out);
        }
    }


    public Sampler getInstance() throws Exception {
        return new Sampler();
    }

    public BufferedImage run(String imageFile) throws Exception {
        createOps();
        setMSplitImageComponent(imageFile);

//        create("Blur");
//        create("Blur");
//        create("Blur");
//        create("Blur");
//        create("Sharpen");

        return create("Posterize");
    }

    public static void saveImage(final BufferedImage img, final File ref) throws IOException {
        //final String format = (ref.endsWith(".png")) ? "png" : "jpg";
        //ImageIO.write(img, format, new File(ref));
        ImageIO.write(img, ref.getName().substring(ref.getName().lastIndexOf(".") + 1), ref);

    }

    private void setMSplitImageComponent(final String source) throws Exception {
        mSplitImageComponent = new SplitImageComponent(source);
    }

    private void createOps() {
        mOps = new Hashtable();
        createConvolutions();
        createTransformations();
        createLookups();
        createRescales();
        createColorOps();
    }

    private void createConvolutions() {
        float ninth = 1.0f / 9.0f;
        float[] blurKernel = {ninth, ninth, ninth, ninth, ninth, ninth, ninth,
                ninth, ninth};
        mOps.put("Blur", new ConvolveOp(new Kernel(3, 3, blurKernel),
                ConvolveOp.EDGE_NO_OP, null));

        float[] edge = {0f, -1f, 0f, -1f, 4f, -1f, 0f, -1f, 0f};
        mOps.put("Edge detector", new ConvolveOp(new Kernel(3, 3, edge),
                ConvolveOp.EDGE_NO_OP, null));

        float[] sharp = {0f, -1f, 0f, -1f, 5f, -1f, 0f, -1f, 0f};
        mOps.put("Sharpen", new ConvolveOp(new Kernel(3, 3, sharp)));
    }

    private void createTransformations() {
        AffineTransform at;
        at = AffineTransform.getRotateInstance(Math.PI / 6, 0, 285);
        mOps.put("Rotate nearest neighbor", new AffineTransformOp(at, null));

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        mOps.put("Rotate bilinear", new AffineTransformOp(at, rh));

        at = AffineTransform.getScaleInstance(.5, .5);
        mOps.put("Scale .5, .5", new AffineTransformOp(at, null));

        at = AffineTransform.getRotateInstance(Math.PI / 6);
        mOps.put("Rotate bilinear (origin)", new AffineTransformOp(at, rh));
    }

    private void createLookups() {
        short[] brighten = new short[256];
        short[] betterBrighten = new short[256];
        short[] posterize = new short[256];
        short[] invert = new short[256];
        short[] straight = new short[256];
        short[] zero = new short[256];
        for (int i = 0; i < 256; i++) {
            brighten[i] = (short) (128 + i / 2);
            betterBrighten[i] = (short) (Math.sqrt((double) i / 255.0) * 255.0);
            //posterize[i] = (short) (i - (i % 32));//Original
            posterize[i] = (short) (i - (i % 64));
            invert[i] = (short) (255 - i);
            straight[i] = (short) i;
            zero[i] = (short) 0;
        }
        mOps.put("Brighten", new LookupOp(new ShortLookupTable(0, brighten),
                null));
        mOps.put("Better Brighten", new LookupOp(new ShortLookupTable(0,
                betterBrighten), null));
        mOps.put("Posterize", new LookupOp(new ShortLookupTable(0, posterize),
                null));
        mOps.put("Invert", new LookupOp(new ShortLookupTable(0, invert), null));

        short[][] redOnly = {invert, straight, straight};
        short[][] greenOnly = {straight, invert, straight};
        short[][] blueOnly = {straight, straight, invert};
        mOps.put("Red invert", new LookupOp(new ShortLookupTable(0, redOnly),
                null));
        mOps.put("Green invert", new LookupOp(
                new ShortLookupTable(0, greenOnly), null));
        mOps.put("Blue invert", new LookupOp(new ShortLookupTable(0, blueOnly),
                null));

        short[][] redRemove = {zero, straight, straight};
        short[][] greenRemove = {straight, zero, straight};
        short[][] blueRemove = {straight, straight, zero};
        mOps.put("Red remove", new LookupOp(new ShortLookupTable(0, redRemove),
                null));
        mOps.put("Green remove", new LookupOp(new ShortLookupTable(0,
                greenRemove), null));
        mOps.put("Blue remove", new LookupOp(
                new ShortLookupTable(0, blueRemove), null));
    }

    private void createRescales() {
        mOps.put("Rescale .5, 0", new RescaleOp(.5f, 0, null));
        mOps.put("Rescale .5, 64", new RescaleOp(.5f, 64, null));
        mOps.put("Rescale 1.2, 0", new RescaleOp(1.2f, 0, null));
        mOps.put("Rescale 1.5, 0", new RescaleOp(1.5f, 0, null));
    }

    private void createColorOps() {
        mOps.put("Grayscale", new ColorConvertOp(ColorSpace
                .getInstance(ColorSpace.CS_GRAY), null));
    }

//    private void createImageFrame(String imageFile) {
//        // Create the image frame.
//        mSplitImageComponent = new SplitImageComponent(imageFile);
//        mImageFrame = new Frame(imageFile);
//        mImageFrame.setLayout(new BorderLayout());
//        mImageFrame.add(mSplitImageComponent, BorderLayout.CENTER);
////    Utilities.sizeContainerToComponent(mImageFrame, mSplitImageComponent);
//        //  Utilities.centerFrame(mImageFrame);
//        mImageFrame.setVisible(true);
//    }

//    private void createUI() {
//        setFont(new Font("Serif", Font.PLAIN, 12));
//        setLayout(new BorderLayout());
//        // Set our location to the left of the image frame.
//        setSize(200, 350);
//        Point pt = mImageFrame.getLocation();
//        setLocation(pt.x - getSize().width, pt.y);
//
//        final Checkbox accumulateCheckbox = new Checkbox("Accumulate", false);
//        final Label statusLabel = new Label("");
//
//        // Make a sorted list of the operators.
//        Enumeration e = mOps.keys();
//        Vector names = new Vector();
//        while (e.hasMoreElements()) {
//            names.addElement(e.nextElement());
//        }
//        Collections.sort(names);
//        final java.awt.List list = new java.awt.List();
//        for (int i = 0; i < names.size(); i++) {
//            list.add((String) names.elementAt(i));
//        }
//        add(list, BorderLayout.CENTER);
//
//        // When an item is selected, do the corresponding transformation.
//        list.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent ie) {
//                if (ie.getStateChange() != ItemEvent.SELECTED) {
//                    return;
//                }
//                String key = list.getSelectedItem();
//                BufferedImageOp op = (BufferedImageOp) mOps.get(key);
//                BufferedImage source = mSplitImageComponent.getSecondImage();
//                boolean accumulate = accumulateCheckbox.getState();
//                if (source == null || accumulate == false) {
//                    source = mSplitImageComponent.getImage();
//                }
//                String previous = mImageFrame.getTitle() + " + ";
//                if (accumulate == false) {
//                    previous = "";
//                }
//                mImageFrame.setTitle(previous + key);
//                statusLabel.setText("Performing " + key + "...");
//                list.setEnabled(false);
//                accumulateCheckbox.setEnabled(false);
//                BufferedImage destination = op.filter(source, null);
//                mSplitImageComponent.setSecondImage(destination);
//                mSplitImageComponent.setSize(mSplitImageComponent
//                        .getPreferredSize());
//                mImageFrame.setSize(mImageFrame.getPreferredSize());
//                list.setEnabled(true);
//                accumulateCheckbox.setEnabled(true);
//                statusLabel.setText("Performing " + key + "...done.");
//            }
//        });
//
//        Button loadButton = new Button("Load...");
//        loadButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent ae) {
//                FileDialog fd = new FileDialog(Sampler.this);
//                fd.show();
//                if (fd.getFile() == null) {
//                    return;
//                }
//                String path = fd.getDirectory() + fd.getFile();
//                mSplitImageComponent.setImage(path);
//                mSplitImageComponent.setSecondImage(null);
////        Utilities.sizeContainerToComponent(mImageFrame,
//                //          mSplitImageComponent);
//                mImageFrame.validate();
//                mImageFrame.repaint();
//            }
//        });
//
//        Panel bottom = new Panel(new GridLayout(2, 1));
//        Panel topBottom = new Panel();
//        topBottom.add(accumulateCheckbox);
//        topBottom.add(loadButton);
//        bottom.add(topBottom);
//        bottom.add(statusLabel);
//        add(bottom, BorderLayout.SOUTH);
//
//        addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                mImageFrame.dispose();
//                dispose();
//                System.exit(0);
//            }
//        });
//    }

    class SplitImageComponent {
        private BufferedImage mImage;

        private BufferedImage mSecondImage;

        private int mSplitX;

        public SplitImageComponent(String path) {
            setImage(path);
            setSecondImage(mImage);
        }

        public SplitImageComponent(BufferedImage image) {
            setImage(image);
        }

        public void setImage(String path) {
            Image image = blockingLoad(path);
            mImage = makeBufferedImage(image);
        }

        public void setImage(BufferedImage image) {
            mImage = image;
        }

        public void setSecondImage(BufferedImage image) {
            mSecondImage = image;
        }

        public BufferedImage getImage() {
            return mImage;
        }

        public BufferedImage getSecondImage() {
            return mSecondImage;
        }

    }

    public Image blockingLoad(String path) {
        Image image = Toolkit.getDefaultToolkit().getImage(path);
        if (waitForImage(image) == false) {
            return null;
        }
        return image;
    }

    private Component sComponent = new Component() {
    };

    private final MediaTracker sTracker = new MediaTracker(sComponent);

    private int sID = 0;


    public boolean waitForImage(Image image) {
        int id;
        synchronized (sComponent) {
            id = sID++;
        }
        sTracker.addImage(image, id);
        try {
            sTracker.waitForID(id);
        } catch (InterruptedException ie) {
            return false;
        }
        if (sTracker.isErrorID(id)) {
            return false;
        }
        return true;
    }

    public BufferedImage makeBufferedImage(Image image) {
        return makeBufferedImage(image, BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage makeBufferedImage(Image image, int imageType) {
        if (waitForImage(image) == false) {
            return null;
        }

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                image.getHeight(null), imageType);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);
        return bufferedImage;
    }

    private BufferedImage create(final String key) {

        BufferedImageOp op = (BufferedImageOp) mOps.get(key);
        BufferedImage source = mSplitImageComponent.getSecondImage();
        boolean accumulate = true;
        if (source == null || accumulate == false) {
            source = mSplitImageComponent.getImage();
        }

        BufferedImage destination = op.filter(source, null);
        mSplitImageComponent.setSecondImage(destination);
        return destination;
    }
}
