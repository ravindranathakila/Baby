package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.logic.Listeners.widgets.*;
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
                            new SignInOn(itsNatDocument__, $(Skeleton_login_widget), new HumanId(getUsername())) {
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
                                usersName.setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0004") + getUsername());
                                $(Skeleton_othersidebar_identity).appendChild(usersName);
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

                handleRequestType:
                {
                    if (getUsername() != null) {
                        try {
                            /*Be fault tolerant with the user. Just parse category here. Later parse other values when needed*/
                            int category = 0;
                            try {
                                final String tolerantCategory = request__.getServletRequest().getParameter(Controller.Page.DocOrganizeCategory);
                                category = Integer.parseInt(tolerantCategory != null ? tolerantCategory : "0");
                            } catch (final NumberFormatException nfe_) {
                                Loggers.USER_EXCEPTION.error("", nfe_);
                            }
                            switch (category) {
                                case Controller.Page.DocOrganizeModeOrganize:
                                    try {
                                        new PrivateLocationCreate(itsNatDocument__, $(Skeleton_center_skeleton), getUsername()) {
                                        };
                                        for (final PrivateLocation prvLoc : DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPrivateLocation(new HumanId(getUsername())).returnValue().getPrivateLocationsViewed()) {
                                            new PrivateLocationView(itsNatDocument__, $(Skeleton_center_skeleton), getUsername(), prvLoc.getPrivateLocationId()) {
                                            };
                                            new Button(itsNatDocument__, $(Skeleton_center_skeleton), "Visit", false, prvLoc) {
                                                PrivateLocation privateLocation = null;

                                                @Override
                                                protected void init(final Object... initArgs) {
                                                    privateLocation = (PrivateLocation) (((Object[]) initArgs[2])[0]);
                                                    setLink:
                                                    {
                                                        $$(Controller.Page.GenericButtonLink).setAttribute(MarkupTag.A.href(), Controller.Page.Organize.getURL() + "?"
                                                                + Controller.Page.DocOrganizeCategory + "=" + 2 + "&"
                                                                + Controller.Page.DocOrganizeLocation + "=" + prvLoc.getPrivateLocationId());
                                                    }
                                                    setImage:
                                                    {
                                                        $$(Controller.Page.GenericButtonImage).setAttribute(MarkupTag.IMG.src(), RBGet.config.getString(RBGet.url_CDN_STATIC) + "arrow-left.gif");
                                                    }
                                                }

                                                @Override
                                                protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
                                                }
                                            };
                                        }
                                    } catch (final Throwable t) {
                                        Loggers.EXCEPTION.error("{}", t);
                                    }
                                    break;
                                case Controller.Page.DocOrganizeModeLocation:
                                    final int location = Integer.parseInt(request__.getServletRequest().getParameter(Controller.Page.DocOrganizeLocation));
                                    final Element intro = $(MarkupTag.P);
                                    final Location userLoc = DB.getHumanCRUDLocationLocal(true).dirtyRLocation(location);
                                    intro.setTextContent(userLoc.getLocationName());

                                    break;
                                case Controller.Page.DocOrganizeModePrivateLocation:
                                    final long requestedPrivateLocation = Long.parseLong(request__.getServletRequest().getParameter(Controller.Page.DocOrganizeLocation));
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
                                        UseCaseIntroduction:
                                        {
                                            new PrivateLocationView(itsNatDocument__, $(Skeleton_center_skeleton), getUsername(), requestedPrivateLocation) {
                                            };
                                        }
                                        if (DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationIsOwner(getUsername(), requestedPrivateLocation).returnValue())
                                            proceedsOnlyIfOwner:
                                                    {
                                                        final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).rDirtyPrivateLocation(getUsername(), requestedPrivateLocation);
                                                        if (r.returnStatus() == 0) {
                                                            this.owners = r.returnValue().getPrivateLocationOwners();
                                                            final HumansNetPeople user = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(new HumanId(getUsername()));
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

                                                                            final PrivateLocation privateLocation = r.returnValue();

                                                                            @Override
                                                                            public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                                                                                return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationAddOwner(humanId, privateLocation.getPrivateLocationId(), humansFriend);
                                                                            }
                                                                        },
                                                                        new Save<Return<PrivateLocation>>() {

                                                                            final PrivateLocation privateLocation = r.returnValue();

                                                                            @Override
                                                                            public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                                                                                return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationRemoveOwner(humanId, privateLocation.getPrivateLocationId(), humansFriend);
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

                                                                            final PrivateLocation privateLocation = r.returnValue();

                                                                            @Override
                                                                            public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                                                                                return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationAddVisitor(humanId, privateLocation.getPrivateLocationId(), humansFriend);
                                                                            }
                                                                        },
                                                                        new Save<Return<PrivateLocation>>() {

                                                                            final PrivateLocation privateLocation = r.returnValue();

                                                                            @Override
                                                                            public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                                                                                return DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationRemoveVisitor(humanId, privateLocation.getPrivateLocationId(), humansFriend);
                                                                            }
                                                                        }) {
                                                                };
                                                            }
                                                        }
                                                        UseCaseCreatePrivateEvents:
                                                        {
                                                        }
                                                    }
                                        UseCaseViewPrivateEvents:
                                        {
                                        }

                                    } catch (
                                            final Throwable t) {
                                        Loggers.EXCEPTION.error("{}", t);
                                    }
                                    break;
                                case Controller.Page.DocOrganizeModeEvent:
                                    final int event = Integer.parseInt(request__.getServletRequest().getParameter(Controller.Page.DocOrganizeEvent));
                                    break;
                                default:
                                    throw new NumberFormatException("SORRY! INVALID CATEGORY VALUE.");
                            }
                        } catch (
                                final NumberFormatException e_) {
                            Loggers.USER_EXCEPTION.error("", e_);
                        }
                    }
                }
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__,
                                                  final HTMLDocument hTMLDocument__,
                                                  final ItsNatDocument itsNatDocument__) {
            }
        };
    }
}