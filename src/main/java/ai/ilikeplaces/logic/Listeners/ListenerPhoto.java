package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.logic.Listeners.widgets.DownTownFlowCriteria;
import ai.ilikeplaces.logic.Listeners.widgets.Photo$Description;
import ai.ilikeplaces.logic.Listeners.widgets.SignInOnCriteria;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.SmartLogger;
import ai.reaver.HumanId;
import ai.reaver.Return;
import ai.scribble.License;
import ai.scribble._todo;
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

import static ai.ilikeplaces.servlets.Controller.Page.pd_photo;
import static ai.ilikeplaces.servlets.Controller.Page.pd_photo_permalink;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerPhoto implements ItsNatServletRequestListener {

    final private String permaLink = null;

    final static private Logger logger = LoggerFactory.getLogger(ListenerPhoto.class.getName());

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

                layoutNeededForAllPages:
                {
                    setLoginWidget:
                    {
                        setLoginWidget(request__, SignInOnCriteria.SignInOnDisplayComponent.SNAPS);
                    }

                    setTitle:
                    {
                        //set below
                    }
                    setProfileDataLink:
                    {
                        setProfileDataLink();
                    }
                    sideBarFriends:
                    {
                        setSideBarFriends(request__, DownTownFlowCriteria.DownTownFlowDisplayComponent.TALKS);
                    }
                    signinupActionNotice:
                    {
                        //We do nothing now
                    }
                    SetNotifications:
                    {
                        setNotifications();
                    }
                }


                if (super.getUsername() != null) {

                    final Return<List<PrivatePhoto>> r = DB.getHumanCRUDPrivatePhotoLocal(true).rPrivatePhotos(getUsernameAsValid());

                    if (r.returnStatus() == 0) {

                        for (final PrivatePhoto privatePhoto : r.returnValue()) {
                            new Photo$Description(request__, $(Controller.Page.Skeleton_center_content)) {

                                final HumanId myhumanId = new HumanId(getUsername());
                                final PrivatePhoto myprivatePhoto = privatePhoto;

                                @Override
                                protected void init(final Object... initArgs) {
                                    final String imageURL = RBGet.globalConfig.getString("ALBUM_PHOTOS") + privatePhoto.getPrivatePhotoURLPath();
                                    $$(pd_photo_permalink).setAttribute("href", imageURL);
                                    //$$(pd_photo).setAttribute("src", "_");
                                    $$(pd_photo).setAttribute("title", imageURL);

                                    displayBlock($$(Controller.Page.pd_photo_delete));
                                }

                                @Override
                                protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
                                    itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.pd_photo_delete), EventType.CLICK.toString(), new EventListener() {

                                        final HumanId mymyhumanId = new HumanId(getUsername());

                                        @_todo(task = "Expecting itsnat does not serialize these values. Bulky. Verify with Jose.")
                                        final PrivatePhoto mymyprivatePhoto = privatePhoto;

                                        @Override
                                        public void handleEvent(final Event evt_) {
                                            DB.getHumanCRUDPrivatePhotoLocal(true).dPrivatePhoto(mymyhumanId, mymyprivatePhoto.getPrivatePhotoId());
                                            $$sendJS(JSCodeToSend.jqueryHide($$getId(Controller.Page.pd)));
                                        }

                                    }, false);
                                }
                            };
                        }


                    } else {
                        $(Controller.Page.SkeletonCPageNotice).setTextContent(r.returnMsg());
                    }
                }
                SmartLogger.g().complete("Done loading photo page");

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
