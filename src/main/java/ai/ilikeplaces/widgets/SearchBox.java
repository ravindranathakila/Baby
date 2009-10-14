package ai.ilikeplaces.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.servlets.Controller.Page;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class SearchBox extends AbstractWidgetListener {

    /**
     *
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public SearchBox(final ItsNatDocument itsNatDocument__, final Element appendToElement__) {
        super(itsNatDocument__, Page.Photo$Description, appendToElement__);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {
    }
}
