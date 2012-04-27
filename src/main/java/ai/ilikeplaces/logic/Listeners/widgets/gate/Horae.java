package ai.ilikeplaces.logic.Listeners.widgets.gate;

import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * We have several approaches we can take on Horai.
 * <p/>
 * <p/>
 * However, the requir
 * ements that "might" spring up are as follows.
 * <p/>
 * 1. We might need to send JS to the client so extending {@link ai.ilikeplaces.util.AbstractWidgetListener} seems to be a wise thing to do
 * <p/>
 * 2. Using a subscribe/un-subscribe or notification mechanism seems cool.
 * <p/>
 * 3. We might be able to delegate weight on the user's stateful session to this(but why would we do that?)
 * <p/>
 * 4. ON the contrary, keeping The Horae simple makes it more intuitive and manageable.
 * <p/>
 * 5. The Horae might be able to help us on spam.
 * <p/>
 * <p/>
 * <p/>
 * en.wikipedia.org/wiki/Horae
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 4/6/12
 * Time: 10:13 AM
 */
public class Horae extends AbstractWidgetListener<HoraeCriteria> {
    /**
     * @param request__
     * @param page__
     * @param horaeCriteria
     * @param appendToElement__
     */
    public Horae(final ItsNatServletRequest request__, final Controller.Page page__, final HoraeCriteria horaeCriteria, final Element appendToElement__) {
        super(request__, page__, horaeCriteria, appendToElement__);
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
