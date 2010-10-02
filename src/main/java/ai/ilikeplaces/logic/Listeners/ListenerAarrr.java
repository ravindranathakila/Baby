package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.logic.Listeners.widgets.DownTownHeatMap;
import ai.ilikeplaces.rbs.RBGet;
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

import java.util.ResourceBundle;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@TODO(task = "rename to listenerlocation. do a string search on listenermain to find usage first. current search shows no issues. refac delayed till next check")
public class ListenerAarrr implements ItsNatServletRequestListener {


    final static private Logger logger = LoggerFactory.getLogger(ListenerAarrr.class);
    final static protected String LocationId = RBGet.globalConfig.getString("LOCATIONID");

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {

        new AbstractListener(request__) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
                final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");

                new DownTownHeatMap(request__, $(Controller.Page.AarrrDownTownHeatMap), $(Controller.Page.AarrrWOEID));

            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
//                itsNatHTMLDocument__.addEventListener((EventTarget) $(Controller.Page.AarrrFunTypes), EventType.BLUR.toString(), new EventListener() {
//
//                    final ValidatorFace v = ValidatorFace.impl.getInstance();
//
//                    @Override
//                    public void handleEvent(final Event evt_) {
//                        logger.debug("{}", v.isLessThan1000(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.INPUT.value())).toString());
//
//                        DB.getHumanCRUDMapLocal(true).createEntry("fun", v.isLessThan1000(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.INPUT.value())).returnStatus() == 0 ? ((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.INPUT.value()) : "Entry too big");
//
//                    }
//
//                    @Override
//                    public void finalize() throws Throwable {
//                        Loggers.finalized(this.getClass().getName());
//                        super.finalize();
//                    }
//                }, false, new NodePropertyTransport(MarkupTag.INPUT.value()));
//
//                itsNatHTMLDocument__.addEventListener((EventTarget) $(Controller.Page.AarrrEmail), EventType.BLUR.toString(), new EventListener() {
//
//                    final ValidatorFace v = ValidatorFace.impl.getInstance();
//
//                    @Override
//                    public void handleEvent(final Event evt_) {
//                        logger.debug("{}", v.isLessThan1000(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.INPUT.value())).toString());
//
//                        DB.getHumanCRUDMapLocal(true).createEntry("email", v.isLessThan1000(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.INPUT.value())).returnStatus() == 0 ? ((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.INPUT.value()) : "Entry too big");
//
//                    }
//
//                    @Override
//                    public void finalize() throws Throwable {
//                        Loggers.finalized(this.getClass().getName());
//                        super.finalize();
//                    }
//                }, false, new NodePropertyTransport(MarkupTag.INPUT.value()));
            }
        };
    }

    /**
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = toString() + "\n";
        return showChangeLog__ ? changeLog : toString();
    }
}