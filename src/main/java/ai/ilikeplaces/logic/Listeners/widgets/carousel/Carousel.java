package ai.ilikeplaces.logic.Listeners.widgets.carousel;

import ai.doc._doc;
import ai.doc._note;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/1/12
 * Time: 12:21 PM
 */
@_doc(
        NOTE = @_note(
                "A Carousel cannot exist without an album since it does not know what to show."
        )
)
public class Carousel extends AbstractWidgetListener<CarouselCriteria> {


    public static enum CarouselIds implements AbstractWidgetListener.WidgetIds {
        CarouselImages
    }

    /**
     * @param request__
     * @param appendToElement__
     */
    public Carousel(final ItsNatServletRequest request__, final CarouselCriteria carouselCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.Carousel, carouselCriteria, appendToElement__);
    }

    /**
     * @param carouselCriteria
     */
    @Override
    protected void init(final CarouselCriteria carouselCriteria) {
        for (final PrivatePhoto privatePhoto : carouselCriteria.getAlbumPhotos()) {
            new CarouselThumb(request, new CarouselThumbCriteria().setPrivatePhoto(privatePhoto), $$(CarouselIds.CarouselImages));
        }
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
