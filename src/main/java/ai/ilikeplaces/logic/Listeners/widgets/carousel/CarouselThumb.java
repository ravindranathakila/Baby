package ai.ilikeplaces.logic.Listeners.widgets.carousel;

import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.MarkupTag;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/1/12
 * Time: 12:22 PM
 */
public class CarouselThumb extends AbstractWidgetListener<CarouselThumbCriteria> {

    private static final String ALBUM__PHOTOS = "ALBUM_PHOTOS";


    public static enum CarouselThumbIds implements AbstractWidgetListener.WidgetIds {
        carouselThumbImage
    }

    /**
     * @param request__
     * @param appendToElement__
     */
    public CarouselThumb(final ItsNatServletRequest request__, final CarouselThumbCriteria carouselThumbCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.CarouselThumb, carouselThumbCriteria, appendToElement__);
    }


    /**
     * @param carouselThumbCriteria
     */
    @Override
    protected void init(final CarouselThumbCriteria carouselThumbCriteria) {
        final String privatePhotoURLPath = carouselThumbCriteria.getPrivatePhoto().getPrivatePhotoURLPath();
        final String imageURL = RBGet.globalConfig.getString(ALBUM__PHOTOS) + privatePhotoURLPath;
        $$(CarouselThumbIds.carouselThumbImage).setAttribute(MarkupTag.IMG.title(), imageURL);
        $$(CarouselThumbIds.carouselThumbImage).setAttribute(MarkupTag.IMG.alt(), privatePhotoURLPath.replace("\\.jpg",""));
    }

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the constructor
     *
     * @param itsNatHTMLDocument_
     * @param hTMLDocument_
     */
    @Override
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {
    }

}
