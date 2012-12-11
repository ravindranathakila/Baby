package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.logic.Listeners.widgets.FindFriend;
import ai.ilikeplaces.util.Loggers;
import ai.scribble.License;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.Skeleton_center_skeleton;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerFriends implements ItsNatServletRequestListener {

    final private Logger logger = LoggerFactory.getLogger(ListenerFriends.class.getName());

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {

        new AbstractSkeletonListener(request__, response__) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
                super.init(itsNatHTMLDocument__, hTMLDocument__, itsNatDocument__, initArgs);

                if (getUsername() != null) {
                    try {
                        new FindFriend(request__, $(Skeleton_center_skeleton), new HumanId(getUsernameAsValid())) {
                        };
                    } catch (final Throwable t) {
                        Loggers.EXCEPTION.error("{}", t);
                    }
                }
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners
            (
                    final ItsNatHTMLDocument itsNatHTMLDocument__,
                    final HTMLDocument hTMLDocument__,
                    final ItsNatDocument itsNatDocument__) {
            }
        }

        ;
    }
}
