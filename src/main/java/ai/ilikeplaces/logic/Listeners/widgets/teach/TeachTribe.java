package ai.ilikeplaces.logic.Listeners.widgets.teach;

import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 11/27/11
 * Time: 8:07 AM
 */
public class TeachTribe extends AbstractWidgetListener<TeachTribeCriteria> {


    /**
     * @param request__
     * @param teachTribeCriteria
     * @param appendToElement__
     */
    public TeachTribe(final ItsNatServletRequest request__, final TeachTribeCriteria teachTribeCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.TeachTribe, teachTribeCriteria, appendToElement__);
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


    public static enum TeachTribeIds {
       teach_tribe_widget
    }
}
