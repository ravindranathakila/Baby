package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.logic.Listeners.widgets.Photo$Description;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractListener;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.pd_photo;
import static ai.ilikeplaces.servlets.Controller.Page.pd_photo_permalink;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerPhoto implements ItsNatServletRequestListener {

    final private String permaLink = null;

    final static private Logger logger = LoggerFactory.getLogger(ListenerPhoto.class.getName());

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {
        final String publicPhotoURLPath = (String) request__.getServletRequest().getAttribute("photoURL");

        new AbstractListener(request__) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {

                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor + JSCodeToSend.FnLocationId + JSCodeToSend.FnLocationName + JSCodeToSend.FnSetTitle);

                new Photo$Description(itsNatDocument__, $(Controller.Page.Main_center_main)) {

                    @Override
                    protected void init(final Object... initArgs) {

                        $$(pd_photo_permalink).setAttribute("href", permaLink + "|" + location);
                        $$(pd_photo).setAttribute("src", publicPhotoURLPath);
                    }
                };

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
