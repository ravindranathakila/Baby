package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.exception.ConstructorInvokationException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.logic.Listeners.widgets.*;
import ai.ilikeplaces.util.AbstractListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.naming.NamingException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import ai.ilikeplaces.servlets.Controller;
import static ai.ilikeplaces.servlets.Controller.Page.*;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author Ravindranath Akila
 */
public class ListenerPhoto implements ItsNatServletRequestListener {

    final static protected String LocationId = "locationId";
    final protected static String JsCodeToSend = "document.monitor = new EventMonitor(); \n" +
            "document.getItsNatDoc().addEventMonitor(document.monitor); \n";
    final private String permaLink = null;
    final private Logger logger = LoggerFactory.getLogger(ListenerPhoto.class.getName());

    /**
     *
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

                itsNatDocument.addCodeToSend(JsCodeToSend);

                new Photo$Description(itsNatDocument__, $(Controller.Page.Main_center_main)) {

                    @Override
                    protected void init() {

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
     *
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = new String(toString() + "\n");
        return showChangeLog__ ? changeLog : toString();
    }
}
