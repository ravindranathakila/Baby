package ai.ilikeplaces.logic.Listeners.widgets.people;

import ai.ilikeplaces.logic.Listeners.widgets.UserProperty;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.HTMLDocParser;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MarkupTag;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/1/12
 * Time: 12:22 PM
 */
public class PeopleThumb extends AbstractWidgetListener<PeopleThumbCriteria> {

    private static final String PROFILE_PHOTOS = "PROFILE_PHOTOS";


    public static enum PeopleThumbIds implements WidgetIds {
        PeopleThumbImage
    }

    /**
     * @param request__
     * @param appendToElement__
     */
    public PeopleThumb(final ItsNatServletRequest request__, final PeopleThumbCriteria peopleThumbCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.PeopleThumb, peopleThumbCriteria, appendToElement__);
    }


    /**
     * @param peopleThumbCriteria
     */
    @Override
    protected void init(final PeopleThumbCriteria peopleThumbCriteria) {
        final String profilePhotoURLPath = UserProperty.formatProfilePhotoUrl(peopleThumbCriteria.getProfilePhoto());
        $$(PeopleThumbIds.PeopleThumbImage).setAttribute(MarkupTag.IMG.title(), profilePhotoURLPath);

        PeopleThumb.this.fetchToEmail(profilePhotoURLPath);
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

    /**
     * @param args
     */
    protected void fetchToEmail(final Object... args) {
        try {
            final Document document = HTMLDocParser.getDocument(Controller.REAL_PATH + Controller.WEB_INF_PAGES + Controller.PEOPLE_EMAIL_XHTML);
            $$(PeopleThumbIds.PeopleThumbImage.name(), document).setAttribute(MarkupTag.IMG.src(), (String) args[0]);

            fetchToEmail = HTMLDocParser.convertNodeToHtml(document.getElementsByTagName(MarkupTag.BODY.toString()).item(0));
        } catch (final TransformerException e) {
            Loggers.EXCEPTION.error("", e);
        } catch (final SAXException e) {
            Loggers.EXCEPTION.error("", e);
        } catch (final IOException e) {
            Loggers.EXCEPTION.error("", e);
        }
    }

}
