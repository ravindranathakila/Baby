package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.DisplayNameString;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.MarkupTag;
import ai.ilikeplaces.util.Return;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
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
public class DisplayName extends AbstractWidgetListener {
// ------------------------------ FIELDS ------------------------------

    private static final String CHANGED_DISPLAY_NAME_TO_SAME_AS_OLD_ONE = "changed display name to same as old one";

    private DisplayNameString displayname = null;
    private HumanId humanId = null;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * @param request__
     * @param appendToElement__
     * @param humanId
     * @param request
     */
    public DisplayName(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanId) {
        super(
                request__, Page.DisplayName, appendToElement__,
                humanId
        );
    }

    // ------------------------ OVERRIDING METHODS ------------------------
    @Override
    protected void init(final Object... initArgs) {
        registerUserNotifier($$(Page.DisplayNameNotice));

        humanId = (HumanId) initArgs[0];

        displayname = new DisplayNameString();

        @WARNING(warning = "You can change this to inside the IF clause. " +
                "How ever, upon name change with an invalid displayname, the exception thrown makes the next call to " +
                "the dirty read return a empty(or null, not sure) value.")
        final String currentDisplayName = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(humanId).getDisplayName();

        $$(Page.DisplayNameDisplay).setTextContent("Hello " + currentDisplayName);
    }

    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        itsNatHTMLDocument_.addEventListener((EventTarget) $$(Page.DisplayNameInput), EventType.BLUR.toString(), new EventListener() {
            private DisplayNameString mydisplayname = displayname;
            private DisplayNameString tempDisplayNameString = new DisplayNameString();

            @Override
            public void handleEvent(final Event evt_) {
                tempDisplayNameString.setObj($$(evt_).getAttribute(MarkupTag.INPUT.value()));
                if (tempDisplayNameString.validate() == 0) {
                    mydisplayname.setObjAsValid(tempDisplayNameString.getObjectAsValid());
                } else {
                    notifyUser("Something wrong with your name :D");
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.INPUT.value()));

        itsNatHTMLDocument_.addEventListener((EventTarget) $$(Page.DisplayNameSave), EventType.CLICK.toString(), new EventListener() {
            private DisplayNameString mydisplayname = displayname;
            private HumanId myhumanId = humanId;

            @Override
            public void handleEvent(final Event evt_) {
                if (mydisplayname.validate() == 0) {
                    final Return<Boolean> booleanReturn = DB.getHumanCRUDHumanLocal(true).doNTxUDisplayName(myhumanId, mydisplayname);
                    if (booleanReturn.returnValue()) {
                        $$(Page.DisplayNameDisplay).setTextContent("Hello " + mydisplayname.getObjectAsValid());
                    } else {
                        notifyUser("Something went wrong :-( Nothing we did! Something you did! Try trying again...");
                    }
                } else {
                    //the onblur of the display name will handle this
                }
            }
        }, false);
    }
}
