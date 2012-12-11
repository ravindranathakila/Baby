package ai.ilikeplaces.logic.Listeners.widgets.people;

import ai.ilikeplaces.entities.etc.HumanIdFace;
import ai.ilikeplaces.logic.Listeners.widgets.UserProperty;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.scribble._doc;
import ai.scribble._note;
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
        for (final HumanIdFace humanId : peopleCriteria.getPeople()) {
            People.this.fetchToEmail = People.this.fetchToEmail + new PeopleThumb(
                    request,
                    new PeopleThumbCriteria()
                            .setProfilePhoto(UserProperty.HUMANS_IDENTITY_CACHE.get(humanId.getHumanId(), "").getHumansIdentityProfilePhoto()),
                    $$(PeopleIds.PeopleImages)).fetchToEmail;
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
