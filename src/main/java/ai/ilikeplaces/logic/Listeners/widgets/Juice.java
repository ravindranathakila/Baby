package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 2/17/12
 * Time: 6:47 PM
 */
public class Juice extends AbstractWidgetListener<JuiceCriteria>{
    public static enum JuiceIds implements WidgetIds{

    };

    /**
     * @param request__
     * @param appendToElement__
     */
    public Juice(final ItsNatServletRequest request__, final JuiceCriteria juiceCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.Juice, juiceCriteria, appendToElement__);
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
