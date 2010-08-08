package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MarkupTag;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class Button extends AbstractWidgetListener {

    /**
     *
     * @param request__
     * @param appendToElement__
     * @param buttonText
     * @param doRefreshPageOnClick
     * @param params
     */
    public Button(final ItsNatServletRequest request__,  final Element appendToElement__, final String buttonText, final boolean doRefreshPageOnClick, final Object... params) {
        super(request__, Page.GenericButton, appendToElement__, buttonText, doRefreshPageOnClick, params);

        $$(Controller.Page.GenericButtonText).setTextContent(buttonText);
        $$(Controller.Page.GenericButtonText).setAttribute(MarkupTag.GENERIC.classs(), "vtip");
        $$(Controller.Page.GenericButtonText).setAttribute(MarkupTag.GENERIC.title(), buttonText);


        if (doRefreshPageOnClick) {
            ((ItsNatHTMLDocument) itsNatDocument_).addEventListener((EventTarget) $$(Controller.Page.GenericButtonLink), EventType.CLICK.toString(), new EventListener() {
                @Override
                public void handleEvent(final Event evt_) {
                }

                @Override
                public void finalize() throws Throwable {
                    Loggers.finalized(this.getClass().getName());
                    super.finalize();
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

    protected void setBluePrintCSSSpan(final int span, final int prepend, final int append, final boolean isLast) {
        $$(Controller.Page.GenericButtonWidth).setAttribute("class",
                "span-" + span +
                        (prepend == 0 ? "" : " prepend-" + prepend) +
                        (append == 0 ? "" : " append-" + append) +
                        (isLast ? " last" : ""));
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}