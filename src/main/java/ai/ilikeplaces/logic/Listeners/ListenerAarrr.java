package ai.ilikeplaces.logic.Listeners;

import ai.doc._todo;
import ai.ilikeplaces.logic.Listeners.widgets.*;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractListener;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.SmartLogger;
import ai.reaver.HumanId;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;

import java.util.ResourceBundle;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_todo(task = "rename to listenerlocation. do a string search on listenermain to find usage first. current search shows no issues. refac delayed till next check")
public class ListenerAarrr implements ItsNatServletRequestListener {
// ------------------------------ FIELDS ------------------------------

    final static protected String LocationId = RBGet.globalConfig.getString("LOCATIONID");

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ItsNatServletRequestListener ---------------------

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {
        new AbstractListener(request__, response__) {
//        new AbstractSkeletonListener(request__) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
                final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");


                new SignInOn(request__, $(Controller.Page.AarrrHeader),
                        new SignInOnCriteria()
                                .setHumanId(new HumanId(getUsername()))
                                .setSignInOnDisplayComponent(SignInOnCriteria.SignInOnDisplayComponent.HOME)) {
                };

                new DownTownHeatMap(request__, $(Controller.Page.AarrrDownTownHeatMap), $(Controller.Page.AarrrWOEID), getUsername());

                if (getUsername() != null) {
                    displayNone($(Controller.Page.AarrrJuice));
                    displayBlock($(Controller.Page.AarrrDownTownHeatMap));
                } else {
                    new Juice(request__, new JuiceCriteria(), $(Controller.Page.AarrrJuice));
                    displayBlock($(Controller.Page.AarrrJuice));
                    displayNone($(Controller.Page.AarrrDownTownHeatMap));
                }


                SmartLogger.g().complete(Loggers.LEVEL.SERVER_STATUS, Loggers.DONE);
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {

            }
        };
    }
}
