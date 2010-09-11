package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Sep 10, 2010
 * Time: 5:07:59 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class WOEIDGrabber extends AbstractWidgetListener {

    /**
     * @param request__
     * @param appendToElement__
     * @param elementToUpdateWithWOEID
     */
    public WOEIDGrabber(final ItsNatServletRequest request__, final Element appendToElement__, final Element elementToUpdateWithWOEID) {
        super(request__, Controller.Page.WOEIDGrabber, appendToElement__, elementToUpdateWithWOEID);
    }

    @Override
    protected void init(final Object... initArgs) {

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
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
