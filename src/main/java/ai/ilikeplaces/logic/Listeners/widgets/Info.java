package ai.ilikeplaces.logic.Listeners.widgets;

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
 * Date: 4/15/12
 * Time: 10:35 AM
 */
public class Info extends AbstractWidgetListener<InfoCriteria> {

    public static enum InfoIds implements WidgetIds {
        InfoTitle,
        InfoAppend,
        InfoImage
    }

    /**
     * @param request__
     * @param appendToElement__
     */
    public Info(final ItsNatServletRequest request__, final InfoCriteria infoCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.Info, infoCriteria, appendToElement__);

        if (!infoCriteria.getTitle().isEmpty()) {
            $$(InfoIds.InfoTitle).setTextContent(infoCriteria.getTitle());
        }

        if (!infoCriteria.getImage().isEmpty()) {
            $$displayBlock(InfoIds.InfoImage);
            $$(InfoIds.InfoImage).setAttribute(MarkupTag.IMG.title(), infoCriteria.getImage());
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
