package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.logic.Listeners.widgets.Photo$Description;
import ai.ilikeplaces.logic.Listeners.widgets.legal.PrivacyPolicy;
import ai.ilikeplaces.logic.Listeners.widgets.legal.TermsOfServices;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MarkupTag;
import ai.ilikeplaces.util.Return;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.List;

import static ai.ilikeplaces.servlets.Controller.Page.Skeleton_center_content;
import static ai.ilikeplaces.servlets.Controller.Page.pd_photo;
import static ai.ilikeplaces.servlets.Controller.Page.pd_photo_permalink;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerLegal implements ItsNatServletRequestListener {

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {


        new AbstractSkeletonListener(request__) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
                super.init(itsNatHTMLDocument__, hTMLDocument__, itsNatDocument__, initArgs);

                new TermsOfServices(request__,$(Skeleton_center_content)){};
                new PrivacyPolicy(request__,$(Skeleton_center_content)){};

                sl.complete(Loggers.LEVEL.DEBUG, Loggers.DONE);//Request completed within timeout. If not, goes to LEVEL.SERVER_STATUS
            }

            @Override
            protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
            }
        };
    }

    /**
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = new String(toString() + "\n");
        return showChangeLog__ ? changeLog : toString();
    }
}
