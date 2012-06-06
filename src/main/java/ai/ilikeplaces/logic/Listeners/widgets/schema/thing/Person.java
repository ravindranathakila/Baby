package ai.ilikeplaces.logic.Listeners.widgets.schema.thing;

import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.MarkupTag;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 6/3/12
 * Time: 12:05 PM
 */
public class Person extends AbstractWidgetListener<PersonCriteria> {

    public enum PersonIds implements WidgetIds {
        personName,
        personPhoto
    }

    /**
     * @param request__
     * @param appendToElement__
     */
    public Person(final ItsNatServletRequest request__, final PersonCriteria personCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.Person, personCriteria, appendToElement__);

        $$(PersonIds.personName).setTextContent(criteria.getPersonName());
        $$(PersonIds.personPhoto).setAttribute(MarkupTag.IMG.src(), criteria.getPersonPhoto());
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
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
