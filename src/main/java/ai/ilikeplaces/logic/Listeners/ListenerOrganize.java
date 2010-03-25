package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.*;
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

import java.util.List;
import java.util.ResourceBundle;

import static ai.ilikeplaces.servlets.Controller.Page.*;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerOrganize implements ItsNatServletRequestListener {

    final private Logger logger = LoggerFactory.getLogger(ListenerOrganize.class.getName());
    private static final NumberFormatException CATEGORY_NUMBER_FORMAT_EXCEPTION = new NumberFormatException("SORRY! INVALID CATEGORY VALUE.");
    public static final int ModeIntroduction = 123;
    public static final int ModeCreatePlace = 143;

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {

        new AbstractListener(request__) {

            List<? extends HumansFriend> owners = null;
            List<? extends HumansFriend> possibilities = null;

            /**
             * Initialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor);

                final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");

                layoutNeededForAllPages:
                {
                    setLoginWidget:
                    {
                        try {
                            new SignInOn(itsNatDocument__, $(Skeleton_login_widget), new HumanId(getUsername()), request__.getServletRequest()) {
                            };
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("{}", t);
                        }
                    }

                    setMainTitle:
                    {
                        try {
                            //$(skeletonTitle).setTextContent("Welcome to ilikeplaces!");
                        } catch (
                                @FIXME(issue = "This is very important for SEO. Contact ItsNat and find out why exception always occurs here. It says element not found.")
                                final Exception e__) {
                            logger.debug(e__.getMessage());
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
                            Loggers.EXCEPTION.error("{}", t);
                        }

                    }
                    setProfileLink:
                    {
                        try {
                            if (getUsername() != null) {
                                $(Skeleton_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.PhotoCRUD.getURL());
                            } else {
                                $(Skeleton_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.signup.getURL());
                            }
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("{}", t);

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
                                        Loggers.USER_EXCEPTION.error("", nfe_);
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
                                                            new PrivateLocationView(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), prvLoc.getPrivateLocationId()) {
                                                            };
                                                        }
                                                        new PrivateLocationCreate(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid()) {
                                                        };
                                                    }
                                                } catch (final Throwable t) {
                                                    Loggers.EXCEPTION.error("{}", t);
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
                                                        new PrivateLocationView(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), prvLoc.getPrivateLocationId()) {
                                                        };
                                                    }

                                                } catch (final Throwable t) {
                                                    Loggers.EXCEPTION.error("{}", t);
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
                                                    Loggers.EXCEPTION.error("{}", t);
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
                                                    Loggers.EXCEPTION.error("{}", t);
                                                }
                                            }
                                            break;

                                        case Controller.Page.DocOrganizeModePrivateLocation:
                                            UCPrivateLocation:
                                            {
                                                @NOTE(note = "Outside try as this is caught by the outermost common Number format exception.")
                                                final long requestedPrivateLocation = Long.parseLong(request__.getServletRequest().getParameter(Controller.Page.DocOrganizeLocation));
                                                final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocation(getUsername(), requestedPrivateLocation);
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
                                                                                $$(Controller.Page.GenericButtonImage).setAttribute(MarkupTag.IMG.src(), RBGet.config.getString(RBGet.url_CDN_STATIC) + "arrow-left.gif");
                                                                            }
                                                                        };
                                                                    }
                                                                    checkAuthority:
                                                                    {
                                                                        final boolean isOwner = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationIsOwner(getUsernameAsValid(), requestedPrivateLocation).returnValue();
                                                                        final boolean isViewer = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationIsViewer(getUsernameAsValid(), requestedPrivateLocation).returnValue();


                                                                        if ((isOwner || isViewer) && r.returnStatus() == 0) {
                                                                            final long validPrivateLocationId = r.returnValue().getPrivateLocationId();

                                                                            UseCaseIntroduction:
                                                                            {
                                                                                if (!isOwner) {
                                                                                    new PrivateLocationView(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), requestedPrivateLocation) {
                                                                                    };
                                                                                } else {
                                                                                    new PrivateLocationDelete(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), requestedPrivateLocation) {
                                                                                    };
                                                                                }

                                                                                if (isOwner)
                                                                                    proceedsOnlyIfOwner:
                                                                                            {
                                                                                                if (r.returnStatus() == 0) {
                                                                                                    this.owners = r.returnValue().getPrivateLocationOwners();
                                                                                                    final HumansNetPeople user = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(new HumanId(getUsernameAsValid()));
                                                                                                    this.possibilities = user.getHumansNetPeoples();

                                                                                                    AddRemoveOwners:
                                                                                                    {
                                                                                                        new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateLocation>>(
                                                                                                                itsNatDocument__,
                                                                                                                $(Skeleton_center_skeleton),
                                                                                                                user.getHumansNet(),
                                                                                                                possibilities,
                                                                                                                r.returnValue().getPrivateLocationOwners(),
                                                                                                                new Save<Return<PrivateLocation>>() {

                                                                                                                    final long myprivateLocationId = validPrivateLocationId;

                                                                                                                    @Override
                                                                                                                    public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                                                                                                                        return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationAddOwner(humanId, myprivateLocationId, humansFriend);
                                                                                                                    }
                                                                                                                },
                                                                                                                new Save<Return<PrivateLocation>>() {

                                                                                                                    final long myprivateLocationId = validPrivateLocationId;

                                                                                                                    @Override
                                                                                                                    public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                                                                                                                        return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationRemoveOwner(humanId, myprivateLocationId, humansFriend);
                                                                                                                    }
                                                                                                                }) {
                                                                                                        };
                                                                                                    }
                                                                                                    AddRemoveVisitors:
                                                                                                    {
                                                                                                        new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateLocation>>(
                                                                                                                itsNatDocument__,
                                                                                                                $(Skeleton_center_skeleton),
                                                                                                                user.getHumansNet(),
                                                                                                                possibilities,
                                                                                                                r.returnValue().getPrivateLocationViewers(),
                                                                                                                new Save<Return<PrivateLocation>>() {

                                                                                                                    final long myprivateLocationId = validPrivateLocationId;

                                                                                                                    @Override
                                                                                                                    public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                                                                                                                        return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationAddVisitor(humanId, myprivateLocationId, humansFriend);
                                                                                                                    }
                                                                                                                },
                                                                                                                new Save<Return<PrivateLocation>>() {

                                                                                                                    final long myprivateLocationId = validPrivateLocationId;

                                                                                                                    @Override
                                                                                                                    public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                                                                                                                        return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationRemoveVisitor(humanId, myprivateLocationId, humansFriend);
                                                                                                                    }
                                                                                                                }) {
                                                                                                        };
                                                                                                    }
                                                                                                    UseCaseCreatePrivateEvents:
                                                                                                    {
                                                                                                        new PrivateEventCreate(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), validPrivateLocationId) {
                                                                                                        };
                                                                                                    }

                                                                                                } else {
                                                                                                    //warn user about the failure
                                                                                                }
                                                                                            }

                                                                                UseCaseViewPrivateEvents:
                                                                                {
                                                                                    for (final PrivateEvent p : DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPrivateEvent(new HumanId(getUsernameAsValid())).returnValue().getPrivateEventsViewed())
                                                                                        if (p.getPrivateLocation().getPrivateLocationId() == validPrivateLocationId) {
                                                                                            new PrivateEventView(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), p.getPrivateEventId()) {
                                                                                            };
                                                                                        }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                } catch (
                                                                        final Throwable t) {
                                                                    Loggers.EXCEPTION.error("{}", t);
                                                                }
                                                            }
                                                else
                                                    NonExistentLocation:
                                                            {
                                                                $(Skeleton_notice).setTextContent("Either this place does not exist anymore or something went wrong while I looked for it.");
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
                                                            $$(Controller.Page.GenericButtonImage).setAttribute(MarkupTag.IMG.src(), RBGet.config.getString(RBGet.url_CDN_STATIC) + "arrow-left.gif");
                                                        }
                                                    };
                                                }
                                                ShowEvent:
                                                {
                                                    final Return<Boolean> r = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventIsViewer(getUsernameAsValid(), event);
                                                    if (r.returnStatus() == 0)
                                                        UCReadPrivateEventOK:
                                                                {
                                                                    if (!r.returnValue()) {
                                                                        new PrivateEventView(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), event) {
                                                                        };
                                                                    } else {
                                                                        new PrivateEventDelete(itsNatDocument__, $(Skeleton_center_skeleton), getUsernameAsValid(), event) {
                                                                        };
                                                                    }
                                                                }
                                                    else
                                                        UCReadNonExistingPrivateEventEtc:
                                                                {
                                                                    Loggers.userError(getUsernameAsValid(), r.returnMsg());
                                                                }
                                                }

                                            } catch (final Throwable t) {
                                                Loggers.EXCEPTION.error("{}", t);
                                            }
                                        }
                                        break;
                                        default:
                                            throw CATEGORY_NUMBER_FORMAT_EXCEPTION;
                                    }
                                } catch (
                                        final NumberFormatException e_) {
                                    Loggers.USER_EXCEPTION.error("", e_);
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