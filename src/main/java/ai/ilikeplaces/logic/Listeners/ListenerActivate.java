package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.servlets.Controller;
import ai.scribble.License;
import ai.scribble.WARNING;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;

/**
 * @author Ravindranath Akila
 */

@WARNING(warning = "Remember, this shows profiles of other users to the current user. Might impose serious privacy issues if " +
        "not handled with utmost care")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerActivate implements ItsNatServletRequestListener {

    /**
     * @param request__
     * @param response__
     */
    @WARNING(warning = "Remember, this shows profiles of other users to the current user. Might impose serious privacy issues if " +
            "not handled with utmost care")
    @Override
    public void processRequest(final ItsNatServletRequest request__,
                               final ItsNatServletResponse response__) {

        new AbstractSkeletonListener(request__, response__) {

            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
                super.init(itsNatHTMLDocument__, hTMLDocument__, itsNatDocument__, initArgs);
                UCShowActivateMSG:
                {
                    if (getUsername() == null) {
                        $(Controller.Page.Skeleton_notice).setTextContent(GUI.getString("email.activate.your.account"));
                        displayBlock($(Controller.Page.Skeleton_notice_sh));
                    } else {
                        itsNatDocument.addCodeToSend(JSCodeToSend.redirectPageWithURL("/"));
                    }
                }

            }
        };
    }
}
