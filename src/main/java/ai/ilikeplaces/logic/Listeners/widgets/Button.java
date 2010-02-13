package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.Loggers;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.pc_close;
import static ai.ilikeplaces.servlets.Controller.Page.pc_delete;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class Button extends AbstractWidgetListener {

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public Button(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final String buttonText, final boolean doRefreshPageOnClick, final Object ... params) {
        super(itsNatDocument__, Page.GenericButton, appendToElement__, buttonText, doRefreshPageOnClick, params);
               
        $$(Controller.Page.GenericButtonText).setTextContent(buttonText);

        if (doRefreshPageOnClick) {
            ((ItsNatHTMLDocument) itsNatDocument_).addEventListener((EventTarget) $$(Controller.Page.GenericButtonLink), EventType.click.toString(), new EventListener() {
                @Override
                public void handleEvent(final Event evt_) {
                }
            }, false, JSCodeToSend.RefreshPage);
        }
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
    }
}