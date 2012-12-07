package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc._doc;
import ai.ilikeplaces.doc._note;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.MarkupTag;
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
abstract public class Button extends AbstractWidgetListener<ButtonCriteria> {

    /**
     * Now supports image ONLY IF text is unavailable
     *
     * @param request__
     * @param appendToElement__
     * @param buttonText
     * @param doRefreshPageOnClick
     * @param params               [0] should be the img src, if left blank, realized text and will be hidden
     */
    @Deprecated
    @_doc(
            NOTE = @_note("Depricated because the varargs used here. Super advices that we use a bean instead.")
    )
    public Button(final ItsNatServletRequest request__, final Element appendToElement__, final String buttonText, final boolean doRefreshPageOnClick, final Object... params) {
        super(request__, Page.GenericButton, appendToElement__, buttonText, doRefreshPageOnClick, params);

        if (buttonText != null && !buttonText.isEmpty()) {
            $$displayNone($$(Page.GenericButtonImage));
            $$(Controller.Page.GenericButtonText).setAttribute(MarkupTag.GENERIC.title(), buttonText);
            $$(Controller.Page.GenericButtonText).setTextContent(buttonText);
        } else {
            $$displayNone($$(Controller.Page.GenericButtonText));
            $$(Page.GenericButtonImage).setAttribute(MarkupTag.IMG.src(), (String) params[0]);
        }


        if (doRefreshPageOnClick) {
            itsNatDocument_.addEventListener((EventTarget) $$(Controller.Page.GenericButtonLink), EventType.CLICK.toString(), new EventListener() {
                @Override
                public void handleEvent(final Event evt_) {
                }
            }, false, JSCodeToSend.RefreshPage);
        }
    }


    public Button(final ItsNatServletRequest request__, final Element appendToElement__, final ButtonCriteria buttonCriteria) {
        super(request__, Page.GenericButton, buttonCriteria, appendToElement__);

        if (buttonCriteria.getButtonText() != null || !buttonCriteria.getButtonText().isEmpty()) {
            $$displayNone($$(Page.GenericButtonImage));
            $$(Controller.Page.GenericButtonText).setAttribute(MarkupTag.GENERIC.title(), buttonCriteria.getButtonText());
            $$(Controller.Page.GenericButtonText).setTextContent(buttonCriteria.getButtonText());
        } else {
            $$displayNone($$(Controller.Page.GenericButtonText));
            $$(Page.GenericButtonImage).setAttribute(MarkupTag.IMG.src(), buttonCriteria.getButtonImage());
        }


        if (buttonCriteria.isDoRefreshPageOnClick()) {
            itsNatDocument_.addEventListener((EventTarget) $$(Controller.Page.GenericButtonLink), EventType.CLICK.toString(), new EventListener() {
                @Override
                public void handleEvent(final Event evt_) {
                }
            }, false, JSCodeToSend.RefreshPage);
        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
    }

    protected void setBluePrintCSSSpan(final int span, final int prepend, final int append, final boolean isLast) {
        $$(Controller.Page.GenericButtonWidth).setAttribute("class", $$(Controller.Page.GenericButtonWidth).getAttribute("class") + " use100");
    }

}
