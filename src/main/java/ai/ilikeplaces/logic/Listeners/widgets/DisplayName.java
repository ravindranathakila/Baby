package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.DisplayNameString;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.Return;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import javax.servlet.ServletRequest;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class DisplayName extends AbstractWidgetListener {
    private static final String DISPLAYNAME = "displayname";
    private static final String CHANGED_DISPLAY_NAME_TO_SAME_AS_OLD_ONE = "changed display name to same as old one";

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     * @param humanId
     * @param request
     */
    public DisplayName(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final HumanId humanId, final ServletRequest request) {
        super(
                itsNatDocument__,
                Page.DisplayName, appendToElement__,
                humanId,
                request.getParameter(DISPLAYNAME)
        );
    }

    @Override
    protected void init(final Object... initArgs) {
        final HumanId humanId = (HumanId) initArgs[0];
        final String displayname = (String) initArgs[1];
        @WARNING(warning = "You can change this to inside the IF clause. " +
                "How ever, upon name change with an invalid displayname, the exception thrown makes the next call to " +
                "the dirty read return a empty(or null, not sure) value.")
        final String currentDisplayName = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(humanId).getDisplayName();
        if (displayname == null) {
            $$(Controller.Page.DisplayNameDisplay).setTextContent(currentDisplayName);
        } else {
            final DisplayNameString update = new DisplayNameString(displayname);
            if (!currentDisplayName.equals(displayname) && update.validate() == 0) {
                final Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doNTxUDisplayName(humanId, new DisplayNameString(displayname));
                $$(Controller.Page.DisplayNameDisplay).setTextContent(DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(humanId).getDisplayName());
            } else {
                Loggers.userError(humanId.getObj(), currentDisplayName.equals(displayname) ? CHANGED_DISPLAY_NAME_TO_SAME_AS_OLD_ONE : update.getViolationAsString());
                $$(Controller.Page.DisplayNameDisplay).setTextContent(currentDisplayName);
            }
        }
    }

    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {

    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}