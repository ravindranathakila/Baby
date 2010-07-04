package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.Listeners.widgets.*;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventCreate;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventDelete;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventView;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import java.util.ResourceBundle;

import static ai.ilikeplaces.servlets.Controller.Page.*;
import static ai.ilikeplaces.util.Loggers.*;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerOrganize implements ItsNatServletRequestListener {

    final private Logger logger = LoggerFactory.getLogger(ListenerOrganize.class.getName());
    private static final NumberFormatException CATEGORY_NUMBER_FORMAT_EXCEPTION = new NumberFormatException("SORRY! INVALID CATEGORY VALUE.");
    public static final int ModeIntroduction = 123;
    public static final int ModeCreatePlace = 143;
    public static final String DISCOVER_AND_HAVE_FUN_IN_PLACES = "Discover and Have Fun in Places! ";

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {

        new AbstractListener(request__) {

            /**
             * Initialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor);

                final ResourceBundle gUI = RBGet.gui();

                final SmartLogger sl = SmartLogger.start(LEVEL.SERVER_STATUS, "Returning organize page", 60000, null, true);

                layoutNeededForAllPages:
                {
                    setLoginWidget:
                    {
                        try {
                            new SignInOn(itsNatDocument__, $(Skeleton_login_widget), new HumanId(getUsername()), request__.getServletRequest()) {
                            };
                        } catch (final Throwable t) {
                            EXCEPTION.error("{}", t);
                        }
                    }
                    SEO:
                    {
                        try {
                            setMainTitle:
                            {
                                $(skeletonTitle).setTextContent(
                                        DISCOVER_AND_HAVE_FUN_IN_PLACES +
                                                RBGet.globalConfig.getString("bn"));

                            }
                            setMetaDescription:
                            {
                                $(skeletonTitle).setAttribute(MarkupTag.META.namee(),
                                        DISCOVER_AND_HAVE_FUN_IN_PLACES +
                                                RBGet.globalConfig.getString("bn"));
                            }
                        }
                        catch (final Throwable t) {
                            Loggers.DEBUG.debug(t.getMessage());
                        }
                    }
                    signOnDisplayLink:
                    {
                        try {
                            if (getUsername() != null) {
                                final Element usersName = $(MarkupTag.P);
                                usersName.setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0004") + getUsernameAsValid());
                                //$(Skeleton_othersidebar_identity).appendChild(usersName);
                                new DisplayName(itsNatDocument__, $(Skeleton_othersidebar_identity), new HumanId(getUsernameAsValid()), request__.getServletRequest()) {
                                };
                            } else {
                                final Element locationElem = $(MarkupTag.P);
                                locationElem.setTextContent(gUI.getString("NoLogin"));
                                $(Skeleton_othersidebar_identity).appendChild(locationElem);
                            }
                        } catch (final Throwable t) {
                            EXCEPTION.error("{}", t);
                        }

                    }
                    setProfileLink:
                    {
                        try {
                            if (getUsername() != null) {
                                $(Skeleton_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.Profile.getURL());
                            } else {
                                $(Skeleton_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.signup.getURL());
                            }
                        } catch (final Throwable t) {
                            EXCEPTION.error("{}", t);

                        }
                    }
                    setProfilePhotoLink:
                    {
                        try {
                            if (getUsername() != null) {
                                /**
                                 * TODO check for db failure
                                 */
                                String url = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansProfilePhoto(new HumanId(getUsernameAsValid())).returnValueBadly();
                                url = url == null ? null : RBGet.globalConfig.getString("PROFILE_PHOTOS") + url;
                                if (url != null) {
                                    $(Skeleton_profile_photo).setAttribute(MarkupTag.IMG.src(), url);
                                }
                            }
                        } catch (final Throwable t) {
                            EXCEPTION.error("{}", t);

                        }
                    }
                }

                if (getUsername() != null)
                    handleRequestType:
                            {
                                try {
                                    /*Be fault tolerant with the user. Just parse category here. Later parse other values when needed*/
                                    int category = 0;
                                    try {
                                        final String tolerantCategory = request__.getServletRequest().getParameter(Controller.Page.DocOrganizeCategory);
                                        category = Integer.parseInt(tolerantCategory != null ? tolerantCategory : "123");
                                    } catch (final NumberFormatException nfe_) {
                                        USER_EXCEPTION.error("", nfe_);
                                    }
                                    switch (category) {
                                        case ModeIntroduction:
                                            UCEngage:
                                            {
                                                try {
                                                    UCIntroduction:
                                                    {
                                                        $(SkeletonCPageTitle).setTextContent(gUI.getString("organize.main.title"));
                                                        $(SkeletonCPageTitle).setAttribute(MarkupTag.GENERIC.title(), gUI.getString("organize.main.title.title"));
                                                        $(SkeletonCPageIntro).setTextContent(gUI.getString("organize.main.intro"));
                                                        $(SkeletonCPageTitle).setAttribute(MarkupTag.GENERIC.title(), gUI.getString("organize.main.intro.title"));
                                                    }
                                                    UCListOfActions:
                                                    {
                                                        for (final PrivateLocation prvLoc : DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPrivateLocation(new HumanId(getUsernameAsValid())).returnValue().getPrivateLocationsViewed()) {
//                                                            new PrivateLocationView(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), prvLoc.getPrivateLocationId()) {
//                                                            };
                                                            attachPrivateLocationAsRolesPermit(itsNatDocument__, $(Skeleton_center_skeleton), prvLoc);
                                                        }
                                                        new PrivateLocationCreate(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid()) {
                                                        };
                                                    }
                                                } catch (final Throwable t) {
                                                    EXCEPTION.error("{}", t);
                                                }
                                            }
                                            break;
                                        case Controller.Page.DocOrganizeModeOrganize:
                                            UCOrganize:
                                            {
                                                try {
                                                    new PrivateLocationCreate(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid()) {
                                                    };
                                                    for (final PrivateLocation prvLoc : DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPrivateLocation(new HumanId(getUsernameAsValid())).returnValue().getPrivateLocationsViewed()) {
//                                                        new PrivateLocationView(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), prvLoc.getPrivateLocationId()) {
//                                                        };
                                                        attachPrivateLocationAsRolesPermit(itsNatDocument__, $(Skeleton_center_skeleton), prvLoc);
                                                    }

                                                } catch (final Throwable t) {
                                                    EXCEPTION.error("{}", t);
                                                }
                                            }
                                            break;
                                        case ModeCreatePlace:
                                            UCCretePlace:
                                            {
                                                try {
                                                    new PrivateLocationCreate(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid()) {
                                                    };
                                                } catch (final Throwable t) {
                                                    EXCEPTION.error("{}", t);
                                                }
                                            }
                                            break;
                                        case Controller.Page.DocOrganizeModeLocation:
                                            UCLocation:
                                            {
                                                @NOTE(note = "Outside try as this is caught by the outermost common Number format exception.")
                                                final int location = Integer.parseInt(request__.getServletRequest().getParameter(Controller.Page.DocOrganizeLocation));
                                                try {
                                                    final Element intro = $(MarkupTag.P);
                                                    final Location userLoc = DB.getHumanCRUDLocationLocal(true).dirtyRLocation(location).returnValue();
                                                    intro.setTextContent(userLoc.getLocationName());
                                                } catch (final Throwable t) {
                                                    EXCEPTION.error("{}", t);
                                                }
                                            }
                                            break;

                                        case Controller.Page.DocOrganizeModePrivateLocation:
                                            UCPrivateLocation:
                                            {
                                                try {
                                                    @NOTE(note = "Outside try as this is caught by the outermost common Number format exception.")
                                                    final long requestedPrivateLocation = Long.parseLong(request__.getServletRequest().getParameter(Controller.Page.DocOrganizeLocation));
                                                    final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationAsAny(new HumanId().setObjAsValid(getUsername()), requestedPrivateLocation);
                                                    if (r.returnStatus() == 0)
                                                        UCPrivateLocationIsExistent:
                                                                {
                                                                    try {

                                                                        setBackButton:
                                                                        {
                                                                            new Button(itsNatDocument__, $(Skeleton_center_skeleton), "Locations", false) {
                                                                                @Override
                                                                                protected void init(final Object... initArgs) {
                                                                                    $$(Controller.Page.GenericButtonLink).setAttribute(MarkupTag.A.href(), Controller.Page.Organize.getURL() + "?"
                                                                                            + Controller.Page.DocOrganizeCategory + "=" + 0);
                                                                                    $$(Controller.Page.GenericButtonImage).setAttribute(MarkupTag.IMG.src(), RBGet.globalConfig.getString(RBGet.url_CDN_STATIC) + "arrow-left.gif");
                                                                                }
                                                                            };
                                                                        }
                                                                        attachPrivateLocationAsRolesPermit(itsNatDocument__, $(Skeleton_center_skeleton), r.returnValue());

//                                                                        checkAuthority:
//                                                                        {
////                                                                            final boolean isOwner = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationIsOwner(getUsernameAsValid(), requestedPrivateLocation).returnValue();
////                                                                            final boolean isViewer = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationIsViewer(getUsernameAsValid(), requestedPrivateLocation).returnValue();
//
//                                                                            final boolean isOwner = r.returnValue().getPrivateLocationOwners().contains(new HumanIdEq(getUsernameAsValid()));
//                                                                            final boolean isViewer = r.returnValue().getPrivateLocationViewers().contains(new HumanIdEq(getUsernameAsValid()));
//
//
//                                                                            if (isOwner || isViewer) {
//                                                                                USER.info(getUsernameAsValid() + " is granted access to view this private location.");
//                                                                                final long validPrivateLocationId = r.returnValue().getPrivateLocationId();
//
//                                                                                UseCaseIntroduction:
//                                                                                {
//                                                                                    if (isViewer) {
//                                                                                        USER.info(getUsernameAsValid() + " is accepted as a owner of this location.");
//                                                                                        new PrivateLocationView(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), requestedPrivateLocation) {
//                                                                                        };
//                                                                                    } else if (isOwner) {
//                                                                                        USER.info(getUsernameAsValid() + " is accepted as a viewer of this location.");
//                                                                                        new PrivateLocationDelete(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), requestedPrivateLocation) {
//                                                                                        };
//                                                                                        UseCaseCreatePrivateEvents:
//                                                                                        {
//                                                                                            new PrivateEventCreate(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), validPrivateLocationId) {
//                                                                                            };
//                                                                                        }
//                                                                                    }
//                                                                                    UseCaseViewPrivateEvents:
//                                                                                    {
//                                                                                        for (final PrivateEvent p : DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPrivateEvent(new HumanId(getUsernameAsValid())).returnValue().getPrivateEventsViewed())
//                                                                                            if (p.getPrivateLocation().getPrivateLocationId() == validPrivateLocationId) {
//                                                                                                new PrivateEventView(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), p.getPrivateEventId()) {
//                                                                                                };
//                                                                                            }
//                                                                                    }
//                                                                                }
///*                                                                                getAndDisplayAllThePhotos:
//                                                                                {
//                                                                                    List<PublicPhoto> listPublicPhoto = existingLocation_.getPublicPhotos();
//                                                                                    logger.info(RBGet.logMsgs.getString("NUMBER_OF_PHOTOS_FOR_LOCATION"), existingLocation_.getLocationName(), listPublicPhoto.size());
//
//                                                                                    int i = 0;
//                                                                                    for (final Iterator<PublicPhoto> it = listPublicPhoto.iterator(); it.hasNext(); i++) {
//                                                                                        try {
//                                                                                            final PublicPhoto publicPhoto = it.next();
//
//                                                                                            //old mode pasted end of class if needed
//                                                                                            newMode:
//                                                                                            {
//                                                                                                final Element image = $(IMG);
//                                                                                                image.setAttribute(IMG.src(), publicPhoto.getPublicPhotoURLPath());
//                                                                                                image.setAttribute(IMG.alt(), publicPhoto.getPublicPhotoDescription());
//                                                                                                image.setAttribute(IMG.style(), "width:110px;");
//
//                                                                                                final Element link = $(A);
//                                                                                                link.setAttribute(A.href(), publicPhoto.getPublicPhotoURLPath());
//
//                                                                                                link.appendChild(image);
//
//                                                                                                $(Main_yox).appendChild(link);
//                                                                                            }
//                                                                                        } catch (final Throwable t) {
//                                                                                            Loggers.EXCEPTION.error("", t);
//                                                                                        }
//                                                                                    }
//                                                                                }*/
//                                                                            } else {
//                                                                                USER.info(getUsernameAsValid() + " is denied rights to view this private location.");
//                                                                                $(Skeleton_notice).setTextContent(gUI.getString("NO_RIGHTS_TO_VIEW"));
//                                                                            }
//                                                                        }
                                                                    } catch (
                                                                            final Throwable t) {
                                                                        EXCEPTION.error("{}", t);
                                                                    }
                                                                }
                                                    else
                                                        NonExistentLocation:
                                                                {
                                                                    USER.warn(getUsernameAsValid() + " fails to access this location");
                                                                    $(Skeleton_notice).setTextContent(gUI.getString("NO_EXIST_OR_ERROR"));
                                                                }
                                                } catch (final Throwable t) {
                                                    EXCEPTION.error("", t);
                                                }
                                            }
                                            break;
                                        case Controller.Page.DocOrganizeModeEvent: {
                                            @NOTE(note = "Outside try as this is caught by the outermost common Number format exception.")
                                            final long requestedPrivateLocation = Long.parseLong(request__.getServletRequest().getParameter(Controller.Page.DocOrganizeLocation));

                                            @NOTE(note = "Outside try as this is caught by the outermost common Number format exception.")
                                            final long event = Long.parseLong(request__.getServletRequest().getParameter(Controller.Page.DocOrganizeEvent));

                                            try {
                                                SetBackButton:
                                                {
                                                    new Button(itsNatDocument__, $(Skeleton_center_skeleton), "Locations", false) {
                                                        @Override
                                                        protected void init(final Object... initArgs) {
                                                            $$(Controller.Page.GenericButtonLink).setAttribute(MarkupTag.A.href(), Controller.Page.Organize.getURL() + "?"
                                                                    + Controller.Page.DocOrganizeCategory + "=" + 0);
                                                            $$(Controller.Page.GenericButtonImage).setAttribute(MarkupTag.IMG.src(), RBGet.globalConfig.getString(RBGet.url_CDN_STATIC) + "arrow-left.gif");
                                                        }
                                                    };
                                                }
                                                ShowEvent:
                                                {
                                                    final Return<PrivateEvent> r = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEvent(getUsernameAsValid(), event);
                                                    if (r.returnStatus() == 0)
                                                        UCReadPrivateEventOK:
                                                                {
                                                                    attachPrivateEventAsRolesPermit(itsNatDocument__, $(Skeleton_center_skeleton), r.returnValue());
                                                                }
                                                    else
                                                        UCReadNonExistingPrivateEventEtc:
                                                                {
                                                                    userError(getUsernameAsValid(), r.returnMsg());
                                                                }
                                                }

                                            } catch (final Throwable t) {
                                                EXCEPTION.error("{}", t);
                                            }
                                        }
                                        break;
                                        default:
                                            throw CATEGORY_NUMBER_FORMAT_EXCEPTION;
                                    }
                                } catch (
                                        final NumberFormatException e_) {
                                    USER_EXCEPTION.error("", e_);
                                }
                            }
                sl.complete(LEVEL.DEBUG, Loggers.DONE);//Request completed within timeout. If not, goes to LEVEL.SERVER_STATUS
            }


            private void attachPrivateEventAsRolesPermit(final ItsNatDocument itsNatDocument__, final Element appendToElement, final PrivateEvent r) {
                final boolean isOwner = r.getPrivateEventOwners().contains(new HumanIdEq(getUsernameAsValid()));
                final boolean isViewer = r.getPrivateEventViewers().contains(new HumanIdEq(getUsernameAsValid()));

                if ((isOwner || isViewer)) {
                    USER.info(getUsernameAsValid() + " is granted access to view this private event.");
                    final long validPrivateLocationId = r.getPrivateEventId();
                    if (isOwner) {
                        new PrivateEventDelete(itsNatDocument__, appendToElement, getUsernameAsValid(), r.getPrivateEventId()) {
                        };
                    } else if (isViewer) {
                        new PrivateEventView(itsNatDocument__, appendToElement, getUsernameAsValid(), r.getPrivateEventId()) {
                        };
                    }
                } else {
                    USER.info(getUsernameAsValid() + " is denied rights to view this private event:" + r.getPrivateEventId());
                    $(Skeleton_notice).setTextContent(RBGet.gui().getString("NO_RIGHTS_TO_VIEW"));
                }
            }

            private void attachPrivateLocationAsRolesPermit(final ItsNatDocument itsNatDocument__, final Element appendToElement, final PrivateLocation r) {
                {

                    final long requestedPrivateLocation = r.getPrivateLocationId();

                    final boolean isOwner = r.getPrivateLocationOwners().contains(new HumanIdEq(getUsernameAsValid()));
                    final boolean isViewer = r.getPrivateLocationViewers().contains(new HumanIdEq(getUsernameAsValid()));


                    if ((isOwner || isViewer)) {
                        final long validPrivateLocationId = r.getPrivateLocationId();

                        UseCaseIntroduction:
                        {

                            if (isOwner) {
                                USER.info(getUsernameAsValid() + " is accepted as a viewer of this location:" + validPrivateLocationId);
                                new PrivateLocationDelete(itsNatDocument__, appendToElement, getUsernameAsValid(), requestedPrivateLocation) {
                                };
                                UseCaseCreatePrivateEvents:
                                {
                                    new PrivateEventCreate(itsNatDocument__, appendToElement, getUsernameAsValid(), validPrivateLocationId) {
                                    };
                                }
                            } else if (isViewer) {
                                USER.info(getUsernameAsValid() + " is accepted as a owner of this location:" + validPrivateLocationId);
                                new PrivateLocationView(itsNatDocument__, appendToElement, getUsernameAsValid(), requestedPrivateLocation) {
                                };
                            }

                            UseCaseViewPrivateEvents:
                            {
                                for (final PrivateEvent p : DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPrivateEvent(new HumanId(getUsernameAsValid())).returnValue().getPrivateEventsViewed())
                                    if (p.getPrivateLocation().getPrivateLocationId() == validPrivateLocationId) {
                                        new PrivateEventView(itsNatDocument__, appendToElement, getUsernameAsValid(), p.getPrivateEventId()) {
                                        };
                                    }
                            }
                        }
                    } else {
                        USER.info(getUsernameAsValid() + " is denied rights to view this private location:" + r.getPrivateLocationId());
                        $(Skeleton_notice).setTextContent(RBGet.gui().getString("NO_RIGHTS_TO_VIEW"));
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