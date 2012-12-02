package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.Listeners.widgets.DisplayName;
import ai.ilikeplaces.logic.Listeners.widgets.ForgotPasswordManager;
import ai.ilikeplaces.logic.Listeners.widgets.PasswordManager;
import ai.ilikeplaces.logic.Listeners.widgets.Profile;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.Loggers;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpSession;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.Skeleton_center_content;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerProfile implements ItsNatServletRequestListener {


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
                        {
                            new DisplayName(request__, $(Skeleton_center_content), new HumanId(getUsernameAsValid()));

                            new Profile(request__, $(Skeleton_center_content), new HumanId(getUsernameAsValid()));

                            new PasswordManager(request__, $(Skeleton_center_content), new HumanId(getUsernameAsValid()), ((ItsNatHttpSession) request__.getItsNatSession()).getHttpSession()) {
                            };

                        }

                    } catch (final Throwable t) {
                        Loggers.EXCEPTION.error("{}", t);
                    }
                } else {
                    try {
                        {
                            new ForgotPasswordManager(request__, $(Skeleton_center_content), ((ItsNatHttpSession) request__.getItsNatSession()).getHttpSession()) {
                            };
                        }

                    } catch (final Throwable t) {
                        Loggers.EXCEPTION.error("{}", t);
                    }
                }
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(
                    final ItsNatHTMLDocument itsNatHTMLDocument__,
                    final HTMLDocument hTMLDocument__,
                    final ItsNatDocument itsNatDocument__) {
            }
        };
    }
}
