package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.logic.Listeners.widgets.DownTownFlow;
import ai.ilikeplaces.logic.Listeners.widgets.DownTownFlowCriteria;
import ai.ilikeplaces.logic.Listeners.widgets.DownTownHeatMap;
import ai.ilikeplaces.logic.Listeners.widgets.SignInOn;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractListener;
import ai.ilikeplaces.util.Loggers;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.html.HTMLDocument;

import java.util.ResourceBundle;

import static ai.ilikeplaces.servlets.Controller.Page.Skeleton_login_widget;

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
//        new AbstractSkeletonListener(request__) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
                final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");


                new SignInOn(request__, $(Controller.Page.AarrrHeader), new HumanId(getUsername()), request__.getServletRequest()) {
                };

                new DownTownHeatMap(request__, $(Controller.Page.AarrrDownTownHeatMap), $(Controller.Page.AarrrWOEID), getUsername());

                if (getUsername() != null) {
//                    new DownTownFlow(
//                            request__,
//                            new DownTownFlowCriteria()
//                                    .setDownTownFlowDisplayComponent(DownTownFlowCriteria.DownTownFlowDisplayComponent.TALKS)
//                                    .setHumanId(new HumanId(getUsernameAsValid()))
//                                    .setHumanUserLocal(getHumanUserAsValid()),
//                            $(Controller.Page.AarrrColumn1));
//                    new DownTownFlow(
//                            request__,
//                            new DownTownFlowCriteria()
//                                    .setDownTownFlowDisplayComponent(DownTownFlowCriteria.DownTownFlowDisplayComponent.TRIBES)
//                                    .setHumanId(new HumanId(getUsernameAsValid()))
//                                    .setHumanUserLocal(getHumanUserAsValid()),
//                            $(Controller.Page.AarrrColumn2));
//                    new DownTownFlow(
//                            request__,
//                            new DownTownFlowCriteria()
//                                    .setDownTownFlowDisplayComponent(DownTownFlowCriteria.DownTownFlowDisplayComponent.MOMENTS)
//                                    .setHumanId(new HumanId(getUsernameAsValid()))
//                                    .setHumanUserLocal(getHumanUserAsValid()),
//                            $(Controller.Page.AarrrColumn3));
//                    new DownTownFlow(
//                            request__,
//                            new DownTownFlowCriteria()
//                                    .setDownTownFlowDisplayComponent(DownTownFlowCriteria.DownTownFlowDisplayComponent.TALKS)
//                                    .setHumanId(new HumanId(getUsernameAsValid()))
//                                    .setHumanUserLocal(getHumanUserAsValid()),
//                            $(Controller.Page.AarrrColumn4));
                }


                sl.complete(Loggers.LEVEL.SERVER_STATUS, Loggers.DONE);
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
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
        String changeLog = toString() + "\n";
        return showChangeLog__ ? changeLog : toString();
    }
}