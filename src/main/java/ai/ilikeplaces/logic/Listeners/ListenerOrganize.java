package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.Listeners.widgets.*;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventCreate;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventDelete;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventView;
import ai.ilikeplaces.logic.Listeners.widgets.teach.TeachMoment;
import ai.ilikeplaces.logic.Listeners.widgets.teach.TeachMomentCriteria;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import ai.reaver.HumanId;
import ai.reaver.Return;
import ai.scribble.License;
import ai.scribble._doc;
import ai.scribble._note;
import ai.scribble._todo;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.*;
import static ai.ilikeplaces.util.Loggers.*;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerOrganize implements ItsNatServletRequestListener {

    private static final NumberFormatException CATEGORY_NUMBER_FORMAT_EXCEPTION = new NumberFormatException("SORRY! INVALID CATEGORY VALUE.");
    public static final int ModeIntroduction = 123;
    public static final int ModeCreatePlace = 143;
    public static final String DISCOVER_AND_HAVE_FUN_IN_PLACES = "Discover and Have Fun in Places! ";
    private static final String BN = "bn";
    private static final String AI_ILIKEPLACES_LOGIC_LISTENERS_LISTENER_MAIN_0004 = "ai.ilikeplaces.logic.Listeners.ListenerMain.0004";
    private static final String PROFILE_PHOTOS = "PROFILE_PHOTOS";
    private static final String ORGANIZE_MAIN_TITLE = "organize.main.title";
    private static final String ORGANIZE_MAIN_TITLE_TITLE = "organize.main.title.title";
    private static final String ORGANIZE_MAIN_INTRO = "organize.main.intro";
    private static final String ORGANIZE_MAIN_INTRO_TITLE = "organize.main.intro.title";
    private static final String EQUALS = "=";
    private static final String QMARK = "?";
    private static final String ARROW_LEFT_GIF = "arrow-left.gif";
    private static final String NO_RIGHTS_TO_VIEW = "NO_RIGHTS_TO_VIEW";
    private static final String IS_DENIED_RIGHTS_TO_VIEW_THIS_PRIVATE_EVENT = " is denied rights to view this private event:";
    private static final String IS_ACCEPTED_AS_A_VIEWER_OF_THIS_LOCATION = " is accepted as a viewer of this location:";
    private static final String IS_ACCEPTED_AS_A_OWNER_OF_THIS_LOCATION = " is accepted as a owner of this location:";
    private static final String IS_DENIED_RIGHTS_TO_VIEW_THIS_PRIVATE_LOCATION = " is denied rights to view this private location:";
    private static final String IS_GRANTED_ACCESS_TO_VIEW_THIS_PRIVATE_EVENT = " is granted access to view this private event.";
    private static final String FAILS_TO_ACCESS_THIS_LOCATION = " fails to access this location";
    private static final String FAILS_TO_ACCESS_THIS_EVENT = " fails to access this event";
    private static final String NO_EXIST_OR_ERROR = "NO_EXIST_OR_ERROR";
    private static final String NO_EXIST_OR_ERROR_EVENT = "NO_EXIST_OR_ERROR_EVENT";
    private static final String NO_LOGIN = "NoLogin";
    private static final String RETURNING_ORGANIZE_PAGE = "Returning organize page";

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {

        new AbstractSkeletonListener(request__, response__) {

            /**
             * Initialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {

                layoutNeededForAllPages:
                {
                    setLoginWidget:
                    {
                        setLoginWidget(request__, SignInOnCriteria.SignInOnDisplayComponent.MOMENTS);
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
                        setSideBarFriends(request__, DownTownFlowCriteria.DownTownFlowDisplayComponent.MOMENTS);
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

                if (getUsername() != null) {
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
                                                SmartLogger.g().appendToLogMSG("Mode: ModeIntroduction");

                                                /*//We are abandonning this to use an image instead
                                                $(SkeletonCPageTitle).setTextContent(GUI.getString(ORGANIZE_MAIN_TITLE));
                                                $(SkeletonCPageTitle).setAttribute(MarkupTag.GENERIC.title(), GUI.getString(ORGANIZE_MAIN_TITLE_TITLE));
                                                $(SkeletonCPageIntro).setTextContent(GUI.getString(ORGANIZE_MAIN_INTRO));
                                                $(SkeletonCPageTitle).setAttribute(MarkupTag.GENERIC.title(), GUI.getString(ORGANIZE_MAIN_INTRO_TITLE));
                                                */

                                                @_doc(
                                                        TODO = {
                                                                @_todo("Move this to a widget to ease themeing etc.")
                                                        }
                                                )
                                                final Node unnecessaryVariable = $(Skeleton_center_skeleton).appendChild(
                                                        ElementComposer.compose(
                                                                $(MarkupTag.IMG)
                                                        ).$ElementSetAttribute(MarkupTag.IMG.src(), "/images/create_moment.png")
                                                                .$ElementSetAttribute(MarkupTag.IMG.style(), "width:100%;").get()
                                                );

                                            }
                                            UCListOfActions:
                                            {
                                                new PrivateLocationCreate(request__, $(Skeleton_center_skeleton), getUsernameAsValid()) {
                                                };
                                                for (final PrivateLocation prvLoc : DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPrivateLocation(new HumanId(getUsernameAsValid())).returnValue().getPrivateLocationsViewed()) {
                                                    attachPrivateLocationAsRolesPermit(request__, $(Skeleton_center_skeleton), prvLoc, true, false);
                                                }
                                            }
                                        } catch (final Throwable t) {
                                            EXCEPTION.error("{}", t);
                                        }
                                    }
                                    break;
                                case Controller.Page.DocOrganizeModeOrganize:
                                    UCOrganize:
                                    {
                                        SmartLogger.g().appendToLogMSG("Mode: " + Controller.Page.DocOrganizeModeOrganize);

                                        try {
                                            new PrivateLocationCreate(request__, $(Skeleton_center_skeleton), getUsernameAsValid()) {
                                            };
                                            for (final PrivateLocation prvLoc : DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPrivateLocation(new HumanId(getUsernameAsValid())).returnValue().getPrivateLocationsViewed()) {
                                                attachPrivateLocationAsRolesPermit(request__, $(Skeleton_center_skeleton), prvLoc, true, false);
                                            }

                                        } catch (final Throwable t) {
                                            EXCEPTION.error("{}", t);
                                        }
                                    }
                                    break;
                                case ModeCreatePlace:
                                    UCCretePlace:
                                    {

                                        SmartLogger.g().appendToLogMSG("Mode: ModeCreatePlace");

                                        try {
                                            new PrivateLocationCreate(request__, $(Skeleton_center_skeleton), getUsernameAsValid()) {
                                            };
                                        } catch (final Throwable t) {
                                            EXCEPTION.error("{}", t);
                                        }
                                    }
                                    break;
                                case Controller.Page.DocOrganizeModeLocation:
                                    UCLocation:
                                    {

                                        SmartLogger.g().appendToLogMSG("Mode: " + Controller.Page.DocOrganizeModeLocation);

                                        @_note(note = "Outside try as this is caught by the outermost common Number format exception.")
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

                                        SmartLogger.g().appendToLogMSG("Mode: " + Controller.Page.DocOrganizeModePrivateLocation);

                                        try {
                                            @_note(note = "Outside try as this is caught by the outermost common Number format exception.")
                                            final long requestedPrivateLocation = Long.parseLong(request__.getServletRequest().getParameter(Controller.Page.DocOrganizeLocation));
                                            final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationAsAny(new HumanId().setObjAsValid(getUsername()), requestedPrivateLocation);
                                            if (r.returnStatus() == 0) {
                                                UCPrivateLocationIsExistent:
                                                {
                                                    try {
                                                        setTitle:
                                                        {
                                                            $(skeletonTitle).setTextContent(r.returnValue().getPrivateLocationName());
                                                        }
                                                        setBackButton:
                                                        {
                                                            //Planned for removal. The link makes the UI ugly. Browser has a back button. Left navigation links help. We also can have more intuitive navigation later.
                                                            /*new Button(request__, $(Skeleton_center_skeleton), LOCATIONS, false) {
                                                                @Override
                                                                protected void init(final Object... initArgs) {
                                                                    $$(Controller.Page.GenericButtonLink).setAttribute(MarkupTag.A.href(), Controller.Page.Organize.getURL() + QMARK
                                                                            + Controller.Page.DocOrganizeCategory + EQUALS + 0);
                                                                    $$(Controller.Page.GenericButtonImage).setAttribute(MarkupTag.IMG.src(), RBGet.globalConfig.getString(RBGet.url_CDN_STATIC) + ARROW_LEFT_GIF);
                                                                }
                                                            };*/
                                                        }
                                                        attachPrivateLocationAsRolesPermit(request__, $(Skeleton_center_skeleton), r.returnValue(), false, true);
                                                    } catch (
                                                            final Throwable t) {
                                                        EXCEPTION.error("{}", t);
                                                    }
                                                }
                                            } else {
                                                NonExistentLocation:
                                                {
                                                    USER.warn(getUsernameAsValid() + FAILS_TO_ACCESS_THIS_LOCATION);
                                                    $(Skeleton_notice).setTextContent(GUI.getString(NO_EXIST_OR_ERROR));
                                                }
                                            }
                                        } catch (final Throwable t) {
                                            EXCEPTION.error("", t);
                                        }
                                    }
                                    break;
                                case Controller.Page.DocOrganizeModeEvent: {

                                    SmartLogger.g().appendToLogMSG("Mode: " + Controller.Page.DocOrganizeModeEvent);

                                    @_note(note = "Outside try as this is caught by the outermost common Number format exception.")
                                    final long requestedPrivateLocation = Long.parseLong(request__.getServletRequest().getParameter(Controller.Page.DocOrganizeLocation));

                                    @_note(note = "Outside try as this is caught by the outermost common Number format exception.")
                                    final long event = Long.parseLong(request__.getServletRequest().getParameter(Controller.Page.DocOrganizeEvent));
                                    final Return<PrivateEvent> r = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventAsAny(getUsernameAsValid(), event);
                                    if (r.returnStatus() == 0) {
                                        try {
                                            UCUpdateUserLocation:
                                            {
                                                super.getHumanUserAsValid().storeAndUpdateWith(
                                                        HumanUserLocal.STORE_KEY.USER_LOCATION_TYPE,
                                                        Wall.wallTypePrivateEvent
                                                );
                                                super.getHumanUserAsValid().storeAndUpdateWith(
                                                        HumanUserLocal.STORE_KEY.USER_LOCATION_DETAILS,
                                                        r.returnValueBadly().getPrivateEventId()
                                                );
                                            }

                                            UCSetTitle:
                                            {
                                                $(skeletonTitle).setTextContent(r.returnValue().getPrivateEventName());
                                            }
                                            UCSetBackButton:
                                            {
                                                //Planned for removal. The link makes the UI ugly. Browser has a back button. Left navigation links help. We also can have more intuitive navigation later.
                                                /*new Button(request__, $(Skeleton_center_skeleton), LOCATIONS, false) {
                                                    @Override
                                                    protected void init(final Object... initArgs) {
                                                        $$(Controller.Page.GenericButtonLink).setAttribute(MarkupTag.A.href(), Controller.Page.Organize.getURL() + QMARK
                                                                + Controller.Page.DocOrganizeCategory + EQUALS + 0);
                                                        $$(Controller.Page.GenericButtonImage).setAttribute(MarkupTag.IMG.src(), RBGet.globalConfig.getString(RBGet.url_CDN_STATIC) + ARROW_LEFT_GIF);
                                                    }
                                                };*/
                                            }
                                            UCShowEvent:
                                            {
                                                if (r.returnStatus() == 0) {
                                                    UCReadPrivateEventOK:
                                                    {
                                                        attachPrivateEventAsRolesPermit(request__, $(Skeleton_center_skeleton), r.returnValue(), false, true);
                                                    }
                                                } else {
                                                    UCReadNonExistingPrivateEventEtc:
                                                    {
                                                        userError(getUsernameAsValid(), r.returnMsg());
                                                    }
                                                }
                                            }

                                        } catch (final Throwable t) {
                                            EXCEPTION.error("{}", t);
                                        }
                                    } else {
                                        UCNonExistentEvent:
                                        {
                                            USER.warn(getUsernameAsValid() + FAILS_TO_ACCESS_THIS_EVENT);
                                            $(Skeleton_notice).setTextContent(GUI.getString(NO_EXIST_OR_ERROR_EVENT));
                                        }
                                    }
                                }
                                break;
                                default:
                                    throw CATEGORY_NUMBER_FORMAT_EXCEPTION;
                            }
                        } catch (
                                final NumberFormatException e_) {
                            SmartLogger.g().appendToLogMSG(e_.getMessage());
                            USER_EXCEPTION.error("", e_);
                        }
                    }
                } else {
                    SmartLogger.g().appendToLogMSG("Appending TeachMoment");
                    new TeachMoment(request__, new TeachMomentCriteria(null), $(Controller.Page.Skeleton_center_content));
                }
                SmartLogger.g().complete(Loggers.DONE);//Request completed within timeout. If not, goes to LEVEL.SERVER_STATUS
            }


            private void attachPrivateEventAsRolesPermit(final ItsNatServletRequest request__, final Element appendToElement, final PrivateEvent r, final boolean favorViewership, final boolean detailedMode__) {
                final boolean isOwner = r.getPrivateEventOwners().contains(new HumanIdEq(getUsernameAsValid()));
                final boolean isViewer = r.getPrivateEventViewers().contains(new HumanIdEq(getUsernameAsValid()));

                if ((isOwner || isViewer)) {
                    USER.info(getUsernameAsValid() + IS_GRANTED_ACCESS_TO_VIEW_THIS_PRIVATE_EVENT);
                    final long validPrivateLocationId = r.getPrivateEventId();
                    if (isOwner) {
                        if (!favorViewership) {
                            new PrivateEventDelete(request__, appendToElement, getUsernameAsValid(), r.getPrivateEventId(), detailedMode__) {
                            };
                        } else if (isViewer) {
                            new PrivateEventView(request__, appendToElement, getUsernameAsValid(), r.getPrivateEventId(), detailedMode__) {
                            };
                        } else {
                            new PrivateEventDelete(request__, appendToElement, getUsernameAsValid(), r.getPrivateEventId(), detailedMode__) {
                            };
                        }

                    } else {
                        new PrivateEventView(request__, appendToElement, getUsernameAsValid(), r.getPrivateEventId(), detailedMode__) {
                        };
                    }
                } else {
                    USER.info(getUsernameAsValid() + IS_DENIED_RIGHTS_TO_VIEW_THIS_PRIVATE_EVENT + r.getPrivateEventId());
                    $(Skeleton_notice).setTextContent(RBGet.gui().getString(NO_RIGHTS_TO_VIEW));
                }
            }

            private void attachPrivateLocationAsRolesPermit(final ItsNatServletRequest request__, final Element appendToElement, PrivateLocation r, final boolean favorViewership, final boolean showEventCreate) {

                final long requestedPrivateLocation = r.getPrivateLocationId();

                final PrivateLocation pl = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationAsAny(new HumanId(getUsernameAsValid()).getSelfAsValid(), r.getPrivateLocationId()).returnValueBadly();

                final boolean isOwner = pl.getPrivateLocationOwners().contains(new HumanIdEq(getUsernameAsValid()));
                final boolean isViewer = pl.getPrivateLocationViewers().contains(new HumanIdEq(getUsernameAsValid()));


                if (isOwner || isViewer) {
                    final long validPrivateLocationId = pl.getPrivateLocationId();

                    UseCaseIntroduction:
                    {

                        if (isOwner) {
                            DEBUG.debug(getUsernameAsValid() + IS_ACCEPTED_AS_A_OWNER_OF_THIS_LOCATION + validPrivateLocationId);
                            if (!favorViewership) {
                                new PrivateLocationDelete(request__, appendToElement, getUsernameAsValid(), requestedPrivateLocation) {
                                };
                            } else if (isViewer) {//Because this can be false even though a user is an owner
                                new PrivateLocationView(request__, appendToElement, getUsernameAsValid(), requestedPrivateLocation) {
                                };
                            } else {
                                new PrivateLocationDelete(request__, appendToElement, getUsernameAsValid(), requestedPrivateLocation) {
                                };
                            }

                            UseCaseCreatePrivateEvents:
//Can be done only by owners
                            {
                                if (showEventCreate) {
                                    new PrivateEventCreate(request__, appendToElement, getUsernameAsValid(), validPrivateLocationId) {
                                    };
                                }
                            }
                        } else {
                            DEBUG.debug(getUsernameAsValid() + IS_ACCEPTED_AS_A_VIEWER_OF_THIS_LOCATION + validPrivateLocationId);
                            new PrivateLocationView(request__, appendToElement, getUsernameAsValid(), requestedPrivateLocation) {
                            };
                        }

//                        UseCaseViewPrivateEvents://Private events are showed in private location widgets so this is not necessary
//                        {
//                            for (final PrivateEvent p : DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPrivateEvent(new HumanId(getUsernameAsValid())).returnValue().getPrivateEventsViewed())
//                                if (p.getPrivateLocation().getPrivateLocationId() == validPrivateLocationId) {
//                                    new PrivateEventView(request__, appendToElement, getUsernameAsValid(), p.getPrivateEventId(), false) {
//                                    };
//                                }
//                        }
                    }
                } else {
                    USER.info(getUsernameAsValid() + IS_DENIED_RIGHTS_TO_VIEW_THIS_PRIVATE_LOCATION + r.getPrivateLocationId());
                    $(Skeleton_notice).setTextContent(RBGet.gui().getString(NO_RIGHTS_TO_VIEW));
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
