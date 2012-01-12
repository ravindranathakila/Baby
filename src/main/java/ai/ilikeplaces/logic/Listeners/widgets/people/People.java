package ai.ilikeplaces.logic.Listeners.widgets.people;

import ai.ilikeplaces.doc.DOCUMENTATION;
import ai.ilikeplaces.doc.NOTE;
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
@DOCUMENTATION(
        NOTE = @NOTE(
                "A People cannot exist without an album since it does not know what to show."
        )
)
public class People extends AbstractWidgetListener<PeopleCriteria> {


    public static enum PeopleIds implements WidgetIds {
        PeopleImages
    }

    /**
     * @param request__
     * @param appendToElement__
     */
    public People(final ItsNatServletRequest request__, final PeopleCriteria peopleCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.People, peopleCriteria, appendToElement__);
    }

    /**
     * @param peopleCriteria
     */
    @Override
    protected void init(final PeopleCriteria peopleCriteria) {
        for (final PrivatePhoto privatePhoto : peopleCriteria.getAlbumPhotos()) {
            new PeopleThumb(request, new PeopleThumbCriteria().setPrivatePhoto(privatePhoto), $$(PeopleIds.PeopleImages));
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
