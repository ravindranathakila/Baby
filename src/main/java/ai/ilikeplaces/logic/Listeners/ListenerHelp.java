package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.logic.Listeners.widgets.Help;
import ai.ilikeplaces.logic.Listeners.widgets.HelpCriteria;
import ai.ilikeplaces.servlets.Controller;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/22/11
 * Time: 10:09 AM
 */
public class ListenerHelp implements ItsNatServletRequestListener {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ItsNatServletRequestListener ---------------------

    @Override
    public void processRequest(final ItsNatServletRequest request, final ItsNatServletResponse itsNatServletResponse) {
        new AbstractSkeletonListener(request) {
            @Override
            protected void init(ItsNatHTMLDocument itsNatHTMLDocument__, HTMLDocument hTMLDocument__, ItsNatDocument itsNatDocument__, Object... initArgs) {
                new Help(request, new HelpCriteria(), $(Controller.Page.Skeleton_center_content));
            }
        };
    }
}
