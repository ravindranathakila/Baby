package ai.ilikeplaces.servlets;

import ai.ilikeplaces.logic.Listeners.*;
import ai.ilikeplaces.logic.Listeners.widgets.*;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.Loggers;
import ai.scribble.*;
import org.itsnat.core.*;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Ravindranath Akila
 */

@_todo(task = "Code to disable url calls with itsnat_doc_name=### type urls if possible")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class
        Controller extends HttpServletWrapper {
// ------------------------------ FIELDS ------------------------------

    public static final String WEB_INF_PAGES = "WEB-INF/pages/";

    public static final String EMAIL_FRAME = "ai/ilikeplaces/EmailFrame.xhtml";

    public static String USER_PROPERTY_EMAIL_XHTML = "ai/ilikeplaces/widgets/UserProperty_email.xhtml";

    /**
     * This Map is static as Id's in html documents should be universally identical, i.e. as htmldocname_elementId
     */
    public final static Map<String, String> GlobalHTMLIdRegistry = new IdentityHashMap<String, String>();

    /**
     * Retrievable list of all element Ids by Page
     */
    public final static Map<PageFace, HashSet<String>> GlobalPageIdRegistry = new IdentityHashMap<PageFace, HashSet<String>>();

    public static String REAL_PATH;//Weak implementation but suffices

    private final static Map<PageFace, String> PrettyURLMap_ = new IdentityHashMap<PageFace, String>();//Please read javadoc before making any changes to this implementation

    private static final String ITSNAT_DOC_NAME = "itsnat_doc_name";

    private static final String DT = "dt";

    private static final String DOWNTOWN = "downtown";

    private static final String UNDERSCORE = "_";

    private static final String HASH = "#";

    private static final String _SO = "/so/_so";

    private static final String _PHOTO_ = "_photo_";

    private static final String _ME = "_me";

    private static final String _ORG = "_org";

    private static final String _FRIENDS = "_friends";

    private static final String _BOOK = "_book";

    private static final String _TRIBES = "_tribes";

    private static final String _PROFILE = "_profile";

    private static final String _I = "_i";

    private static final String _ACTIVATE = "_activate";

    private static final String _SHARE = "_share";

    private static final String _GEOBUSINESS = "_geobusiness";

    private static final String _PUBLIC = "_public";

    private static final String _LEGAL = "_legal";

    private static final String _MUSTER = "_muster";

    private static final String _HELP = "_help";

    public static final String EMPTY = "";

    public static final String HTTP_SESSION_ATTR_LOCATION = "HttpSessionAttr.location";

    public static final String PHOTO_URL = "photoURL";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0005 = "ai.ilikeplaces.servlets.Controller.0005";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0006 = "ai.ilikeplaces.servlets.Controller.0006";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0007 = "ai.ilikeplaces.servlets.Controller.0007";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0008 = "ai.ilikeplaces.servlets.Controller.0008";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0009 = "ai.ilikeplaces.servlets.Controller.0009";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0012 = "ai.ilikeplaces.servlets.Controller.0012";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0013 = "ai.ilikeplaces.servlets.Controller.0013";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0014 = "ai.ilikeplaces.servlets.Controller.0014";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0015 = "ai.ilikeplaces.servlets.Controller.0015";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0016 = "ai.ilikeplaces.servlets.Controller.0016";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0017 = "ai.ilikeplaces.servlets.Controller.0017";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0018 = "ai.ilikeplaces.servlets.Controller.0018";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0019 = "ai.ilikeplaces.servlets.Controller.0019";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0020 = "ai.ilikeplaces.servlets.Controller.0020";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0021 = "ai.ilikeplaces.servlets.Controller.0021";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0022 = "ai.ilikeplaces.servlets.Controller.0022";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0023 = "ai.ilikeplaces.servlets.Controller.0023";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0024 = "ai.ilikeplaces.servlets.Controller.0024";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0026 = "ai.ilikeplaces.servlets.Controller.0026";

    public static final String AI_ILIKEPLACES_SERVLETS_CONTROLLER_0025 = "ai.ilikeplaces.servlets.Controller.0025";


    public static final String LOCATION = "location";

    public static final String PHOTO = "photo";

    public static final String PAGE_ORG = "/page/_org";

    public static final String HumanUser = "HumanUser";


    final PageFace aarrr = Page.Aarrr;

    final PageFace tribes = Page.Tribes;

    final PageFace skeleton = Page.Skeleton;

    final PageFace album = Page.Album;

    final PageFace subscribe = Page.Subscribe;

    final PageFace signInOn = Page.SignInOn;

    final PageFace userProperty = Page.UserProperty;

    final PageFace adaptableSignup = Page.AdaptableSignup;

    /**
     * See warnings!
     *
     * @return the absolute path of page files
     */
    @WARNING("REAL_PATH is not initialized until proper application startup")
    public static String getPagesPath() {
        return REAL_PATH + WEB_INF_PAGES;
    }

// -------------------------- ENUMERATIONS --------------------------

    @_note(note = "Inner Enums are static. Therefore, the lists shall be populated only once.")
    public enum Page implements PageFace {

        Global(
                Controller.EMPTY,
                Page.CPageType
        ) {
            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS THE ENUM FOR GLOBAL KEYS ONLY");
            }

            @Override
            public String toString() {
                throw new IllegalAccessError("SORRY! THIS IS THE ENUM FOR GLOBAL KEYS ONLY");
            }
        },

        Aarrr(
                "ai/ilikeplaces/AARRR.xhtml",
                Controller.Page.AarrrDownTownHeatMap,
                Controller.Page.AarrrJuice,
                Controller.Page.AarrrSubscribe,
                Controller.Page.AarrrWOEID,
                Controller.Page.AarrrHeader
        ) {
            @Override
            public String getURL() {
                return APP_ROOT + "page/";
            }

            @Override
            public String toString() {
                return DocAarrr;
            }
        },

        Subscribe(ai.ilikeplaces.logic.Listeners.widgets.subscribe.Subscribe.class,
                ai.ilikeplaces.logic.Listeners.widgets.subscribe.Subscribe.SubscribeIds.values()
        ) {
            @Override
            public String toString() {
                return "DocSubscribe";
            }

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }
        },

        Album("ai/ilikeplaces/widgets/Album.xhtml",
                AlbumManager.AlbumManagerIds.values()
        ) {
            @Override
            public String toString() {
                return "DocAlbum";
            }

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }
        },


        UserProperty("ai/ilikeplaces/widgets/UserProperty.xhtml",
                ai.ilikeplaces.logic.Listeners.widgets.UserProperty.UserPropertyWidgetIds.user_property_profile_photo,
                ai.ilikeplaces.logic.Listeners.widgets.UserProperty.UserPropertyWidgetIds.user_property_name,
                ai.ilikeplaces.logic.Listeners.widgets.UserProperty.UserPropertyWidgetIds.user_property_widget,
                ai.ilikeplaces.logic.Listeners.widgets.UserProperty.UserPropertyWidgetIds.user_property_content

        ) {
            @Override
            public String toString() {
                return "DocUserProperty";
            }

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }
        },

        Tribes(null
        ) {
            @Override
            public String getURL() {
                return APP_ROOT + "page/_tribes";
            }

            @Override
            public String toString() {
                return DocTribes;
            }
        },

        Skeleton("ai/ilikeplaces/Skeleton.xhtml",
                Controller.Page.skeletonTitle,
                Controller.Page.SkeletonCPageTitle,
                Controller.Page.SkeletonCPageIntro,
                Controller.Page.SkeletonCPageNotice,
                Controller.Page.Skeleton_center,
                Controller.Page.Skeleton_center_content,
                Controller.Page.Skeleton_center_skeleton,
                Controller.Page.Skeleton_left_column,
                Controller.Page.Skeleton_right_column,
                Controller.Page.Skeleton_login_widget,
                Controller.Page.Skeleton_notice,
                Controller.Page.Skeleton_notice_sh,
                Controller.Page.Skeleton_profile_photo,
                Controller.Page.Skeleton_othersidebar_identity,
                Controller.Page.Skeleton_sidebar,
                Controller.Page.Skeleton_othersidebar,
                Controller.Page.Skeleton_notifications

        ) {
            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocSkeleton;
            }
        },

        SignInOn(
                "ai/ilikeplaces/widgets/SignInOn.xhtml",
                ai.ilikeplaces.logic.Listeners.widgets.SignInOn.SignInOnIds.values()
        ) {
            @Override
            public String getURL() {
                return APP_ROOT + "page/main";
            }

            @Override
            public String toString() {
                return DocSignInOn;
            }
        },



        /**
         * As we will need sign up on almost all deployments.
         * As we will need sign a widget for cloning based on the embedded enum.
         */
        AdaptableSignup(
                "ai/ilikeplaces/widgets/AdaptableSignup.xhtml",
                ai.ilikeplaces.logic.Listeners.widgets.AdaptableSignup.AdaptableSignupIds.values()) {
            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocAdaptableSignup;
            }
        };

        private static final String APP_ROOT = RBGet.getGlobalConfigKey("AppRoot");

        final static public String GenericButtonWidth = "GenericButtonWidth";

        /*I Page*/
        final static public String DocI = "DocI";

        /*Photos Page*/
        final static public String DocPhotos = "DocPhotos";

        /*Actiavte Page*/
        final static public String DocActivate = "DocActivate";

        /*Share Page*/
        final static public String DocShare = "DocShare";

        /*GeoBusiness Page*/
        final static public String DocGeoBusiness = "DocGeoBusiness";

        /*GeoBusiness Page*/
        final static public String DocLegal = "DocLegal";

        /*Muster Page*/
        final static public String DocMuster = "DocMuster";

        /*Help Page*/
        final static public String DocHelpPage = "DocHelpPage";

        /*TemplateGeneric Pages*/
        final static public String DocPublic = "DocPublic";

        /*Organize Page*/
        final static public String DocOrganize = "DocOrganize";

        /*Organize Attributes*/
        final static public String DocOrganizeCategory = "category";

        final static public String DocOrganizeLocation = LOCATION;

        final static public String DocOrganizeEvent = "event";

        final static public String DocOrganizeAlbum = "album";

        /*FriendFind Page*/
        final static public String DocFriends = "DocFriends";

        /*Book Page*/
        final static public String DocBook = "DocBook";

        /*Tribes Page*/
        final static public String DocTribes = "DocTribes";

        /*Organize Attributes*/
        final static public String DocTribesMode = "mode";

        final static public int DocTribesModeCreate = 0;

        final static public int DocTribesModeView = 1;

        final static public String DocTribesWhich = "which";


        /*Skeleton Page*/
        final static public String DocSkeleton = "DocPrivateLocationCreate";

        /*Skeleton Create IDs*/
        final static public String skeletonTitle = "skeletonTitle";

        final static public String Skeleton_login_widget = "Skeleton_login_widget";

        final static public String Skeleton_othersidebar = "Skeleton_othersidebar";

        final static public String Skeleton_profile_photo = "Skeleton_profile_photo";

        final static public String Skeleton_othersidebar_identity = "Skeleton_othersidebar_identity";

        final static public String Skeleton_center = "Skeleton_center";

        final static public String Skeleton_center_skeleton = "Skeleton_center_skeleton";

        final static public String Skeleton_notice_sh = "Skeleton_notice_sh";

        final static public String Skeleton_notice = "Skeleton_notice";

        final static public String Skeleton_center_content = "Skeleton_center_content";

        final static public String Skeleton_left_column = "Skeleton_left_column";

        final static public String Skeleton_right_column = "Skeleton_right_column";

        final static public String Skeleton_sidebar = "Skeleton_sidebar";

        final static public String Skeleton_notifications = "Skeleton_notifications";

        /*Photo Descrition Specific IDs*/
        final static public String pd = "pd";

        final static public String close = "close";


        /*Aarrr Page*/
        final static public String DocAarrr = "Aarrr";

        /*Aarrr Specific IDs*/
        final static public String AarrrDownTownHeatMap = "AarrrDownTownHeatMap";

        final static public String AarrrJuice = "AarrrJuice";

        final static public String AarrrSubscribe = "AarrrSubscribe";

        final static public String AarrrWOEID = "AarrrWOEID";

        final static public String AarrrHeader = "AarrrHeader";


        /*DocLocation Page*/
        final static public String DocLocation = "DocLocation";

        /*Main Specific IDs*/
        final static public String body = "body";

        /*SignInOn Page*/
        final static public String DocSignInOn = "SignInOn";

        /*AdaptableSignup Page*/
        final static public String DocAdaptableSignup = "DocAdaptableSignup";


        /*Common IDs that should be present in any page*/
        final static public String CPageTitle = "PageTitle";

        final static public String CPageIntro = "PageIntro";

        final static public String CPageNotice = "PageNotice";

        final static public String CPageType = "pageType";


        final static public String SkeletonCPageTitle = CPageTitle;


        final static public String SkeletonCPageIntro = CPageIntro;


        final static public String SkeletonCPageNotice = CPageNotice;


        final static public String PrivateLocationCreateCNotice = "PrivateLocationCreateNotice";


/*End of common definitions*/


        private Page(final Class widget, final Enum[] ids) {
            try {
                Loggers.INFO.info("HELLO, USING CLASS APPROACH ON:" + this.name());
                Loggers.INFO.info("HELLO, ENUM VAL:" + widget.getName());
                Loggers.INFO.info("HELLO, ENUM PATH:" + widget);
                Loggers.INFO.info("HELLO, ENUM IDS:" + Arrays.toString(ids));
                Loggers.INFO.info("HELLO, ADDING ELEMENTS TO PRETTYURL MAP");
                final String path__ = widget.getName().replaceAll("\\.", "/") + ".xhtml";
                Loggers.INFO.info("HELLO, REALIZED PATH FOR WIDGET AS:" + path__);

                PrettyURLMap_.put(this, path__); //ai/ilikeplaces/widgets/DownTownFlow.xhtml
                Loggers.DEBUG.debug("HELLO, ADDING ELEMENTS TO ALLPAGEELEMENTS");
                PutAllPageElementIds(ids);
                Loggers.DEBUG.debug("HELLO, ADDING ELEMENTS TO ALLPAGEELEMENTS BY PAGE");
                PutAllPageElementIdsByPage(this, ids);
            } catch (
                    @WARNING(warning = "Don't remove this catch. You'll not see any duplicate id exceptions etc. anywhere if you do so.")
                    final Exception e) {
                Loggers.EXCEPTION.error("SORRY! SOMETHING WENT WRONG DURING INITIALIZATION. THIS SHOULD EXPECTED TO BE FATAL.", e);
            }
        }

        private Page(final String path__, final Object... ids__) {//NEVER USE VARARGS!
            try {
                Loggers.INFO.info("HELLO, USING STRING APPROACH ON:" + this.name());
                Loggers.INFO.info("HELLO, ENUM VAL:" + this.name());
                Loggers.INFO.info("HELLO, ENUM PATH:" + path__);
                Loggers.INFO.info("HELLO, ENUM IDS:" + Arrays.toString(ids__));
                Loggers.INFO.info("HELLO, ADDING ELEMENTS TO PRETTYURL MAP");
                PrettyURLMap_.put(this, path__); //ai/ilikeplaces/widgets/DownTownFlow.xhtml
                Loggers.DEBUG.debug("HELLO, ADDING ELEMENTS TO ALLPAGEELEMENTS");
                PutAllPageElementIds(ids__);
                Loggers.DEBUG.debug("HELLO, ADDING ELEMENTS TO ALLPAGEELEMENTS BY PAGE");
                PutAllPageElementIdsByPage(this, ids__);
            } catch (
                    @WARNING(warning = "Don't remove this catch. You'll not see any duplicate id exceptions etc. anywhere if you do so.")
                    final Exception e) {
                Loggers.EXCEPTION.error("SORRY! SOMETHING WENT WRONG DURING INITIALIZATION. THIS SHOULD EXPECTED TO BE FATAL.", e);
            }
        }

        abstract public String toString();

        abstract public String getURL();
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Servlet ---------------------

    /**
     * @param serveletConfig__
     * @throws ServletException
     */
    @Override
    public void init(final ServletConfig serveletConfig__) throws ServletException {
        super.init(serveletConfig__);

        final ItsNatHttpServlet inhs__ = getItsNatHttpServlet();

        final ItsNatServletConfig itsNatServletConfig = inhs__.getItsNatServletConfig();

        itsNatServletConfig.setDebugMode(false);
        itsNatServletConfig.setClientErrorMode(ClientErrorMode.NOT_SHOW_ERRORS);
        itsNatServletConfig.setLoadScriptInline(true);
        itsNatServletConfig.setUseGZip(UseGZip.MARKUP);
        itsNatServletConfig.setCommMode(CommMode.XHR_ASYNC_HOLD);
        itsNatServletConfig.setAutoCleanEventListeners(true);
        itsNatServletConfig.setDefaultEncoding("UTF-8");
        itsNatServletConfig.setReferrerEnabled(true);

        setDefaultLocale:
        {
            Loggers.INFO.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0001"));
            Locale.setDefault(new Locale("en", "US"));
            Loggers.INFO.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0002"), Locale.getDefault().toString());
        }

        Loggers.INFO.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0003"), Locale.getDefault().toString());


        /*Add a listener to convert pretty urls to proper urls*/
        inhs__.addItsNatServletRequestListener(new ItsNatServletRequestListener() {
            @Override
            public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {
                try {
                    final ItsNatDocument itsNatDocument__ = request__.getItsNatDocument();
                /*if(itsNatDocument != null && ((HttpServletRequest) request__.getServletRequest()).getPathInfo().contains("itsnat_doc_name")){
                throw new java.lang.RuntimeException("INVALID URL");//This code does not seem to work, please verify.
                }*/
                    if (itsNatDocument__ == null && request__.getServletRequest().getAttribute(ITSNAT_DOC_NAME) == null) {
                        final HttpServletRequest httpServletRequest = (HttpServletRequest) request__.getServletRequest();
                        pathResolver(request__, response__);
                        request__.getItsNatServlet().processRequest(httpServletRequest, response__.getServletResponse());
                    }
                } catch (final Throwable t) {
                    Loggers.error("FATAL ERROR. ITSNAT WAS UNABLE TO PROCESS THE REQUEST. DETAILS AS FOLLOWS.", t);
                    try {
                        request__.getServletRequest().getRequestDispatcher("/500.jsp").forward(request__.getServletRequest(), response__.getServletResponse());
                    } catch (ServletException e) {
                        Loggers.error("FATAL ERROR. ITSNAT WAS UNABLE TO PROCESS THE REQUEST. DETAILS AS FOLLOWS.", t);
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        Loggers.error("FATAL ERROR. ITSNAT WAS UNABLE TO PROCESS THE REQUEST. DETAILS AS FOLLOWS.", t);
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        final String realPath__ = getServletContext().getRealPath("/");
        REAL_PATH = realPath__;
        /*final String pathPrefix__ = realPath__ + "WEB-INF/pages/";*/

        final String pathPrefix__ = RBGet.getGlobalConfigKey("PAGEFILES") != null ? RBGet.getGlobalConfigKey("PAGEFILES") : realPath__ + WEB_INF_PAGES;

        registerDocumentTemplates:
        {
            inhs__.registerItsNatDocumentTemplate(aarrr.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(aarrr)).addItsNatServletRequestListener(new ListenerAarrr());

            inhs__.registerItsNatDocumentTemplate(tribes.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(skeleton)).addItsNatServletRequestListener(new ListenerTribes());

        }

        registerDocumentFragmentTemplatesAKAWidgets:
        {
            inhs__.registerItsNatDocFragmentTemplate(album.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(album));

            inhs__.registerItsNatDocFragmentTemplate(signInOn.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(signInOn));

            inhs__.registerItsNatDocFragmentTemplate(userProperty.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(userProperty));

            inhs__.registerItsNatDocFragmentTemplate(Page.Subscribe.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(Page.Subscribe));

            inhs__.registerItsNatDocFragmentTemplate(adaptableSignup.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(adaptableSignup));
        }
    }

// ------------------------ CANONICAL METHODS ------------------------


// -------------------------- STATIC METHODS --------------------------

    private static void PutAllPageElementIdsByPage(final PageFace page__, final Object... ids__) {
        final HashSet<String> ids_ = new HashSet<String>();

        for (final Object id__ : ids__) {
            ids_.add(id__.toString());
        }

        GlobalPageIdRegistry.put(page__, ids_);
    }


    /**
     * We have a URL of type say www.ilikeplaces.com/page/Egypt
     * where we are supposed to receive the /Egypt part. Most requests WILL be
     * <p/>
     * location requests so we go optimistic on that first.
     *
     * @param request__
     * @param response__
     */
    private static void pathResolver(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {
        final String pathInfo = ((HttpServletRequest) request__.getServletRequest()).getPathInfo();
        String URL__ = pathInfo == null ? Controller.EMPTY : ((HttpServletRequest) request__.getServletRequest()).getPathInfo().substring(1);//Removes preceding slash

        URL__ = URL__.split("\\?")[0].split("#")[0];

        if (isHomePage(URL__)) {
            Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0012));
            Loggers.DEBUG.debug(((HttpServletRequest) request__.getServletRequest()).getRequestURL().toString()
                    + (((HttpServletRequest) request__.getServletRequest()).getQueryString() != null
                    ? ((HttpServletRequest) request__.getServletRequest()).getQueryString()
                    : Controller.EMPTY));
            request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Page.DocAarrr);/*Framework specific*/

//            final ItsNatHttpSession itsNatHttpSession = (ItsNatHttpSession) request__.getItsNatSession();
//            final Object attribute__ = itsNatHttpSession.getAttribute(Controller.HumanUser);
//            final SessionBoundBadRefWrapper<HumanUserLocal> sessionBoundBadRefWrapper = attribute__ == null ? null : (SessionBoundBadRefWrapper<HumanUserLocal>) attribute__;
//
//            if (sessionBoundBadRefWrapper != null && sessionBoundBadRefWrapper.boundInstance.getHumanUserId() != null) {
//                try {
//                    ((HttpServletResponse) response__.getServletResponse()).sendRedirect(LOCATION_HUB);
//                } catch (final IOException e) {
//                    Loggers.EXCEPTION.error("", e);
//                }
//            }
        } else if (isDownTownPage(URL__)) {
            response__.addCodeToSend(JSCodeToSend.redirectPageWithURL(PAGE_ORG));
            return;//let's be paranoid
        } else {
            if (isNonLocationPage(URL__)) {/*i.e. starts with underscore*/
                if (isPhotoPage(URL__)) {
                    request__.getServletRequest().setAttribute(RBGet.globalConfig.getString(HTTP_SESSION_ATTR_LOCATION), getPhotoLocation(URL__));
                    request__.getServletRequest().setAttribute(PHOTO_URL, getPhotoURL(URL__));
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, PHOTO);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0005) + getPhotoLocation(URL__));
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0006) + getPhotoURL(URL__));
                } else if (isHumanPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocPhotos);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0007));
                } else if (isOrganizePage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocOrganize);/*Framework specific*/
                    request__.getServletRequest().setAttribute(Page.DocOrganizeCategory, request__.getServletRequest().getParameter(Page.DocOrganizeCategory));
                    request__.getServletRequest().setAttribute(Page.DocOrganizeLocation, request__.getServletRequest().getParameter(Page.DocOrganizeLocation));
                    request__.getServletRequest().setAttribute(Page.DocOrganizeEvent, request__.getServletRequest().getParameter(Page.DocOrganizeEvent));
                    request__.getServletRequest().setAttribute(Page.DocOrganizeAlbum, request__.getServletRequest().getParameter(Page.DocOrganizeAlbum));
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0013));
                } else if (isFriendsPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocFriends);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0014));
                } else if (isBookingsPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocBook);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0015));
                } else if (isTribesPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Page.DocTribes);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0025));
                } else if (isProfilePage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, "DocProfile");/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0016));
                } else if (isIPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocI);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0017));
                } else if (isActivatePage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocActivate);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0018));
                } else if (isSharePage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocShare);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0019));
                } else if (isGeoBusinessPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocGeoBusiness);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0020));
                } else if (isTemplateGenericPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocPublic);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0021));
                } else if (isLegalPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocLegal);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0022));
                } else if (isMusterPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocMuster);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0023));
                } else if (isHelpPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocHelpPage);/*Framework specific*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0024));
                } else {/*Divert to home page*/
                    Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0008));
                    request__.getServletRequest().setAttribute(LOCATION, Controller.EMPTY);
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Page.DocAarrr);/*Framework specific*/
                }
            } else if (isCorrectLocationFormat(URL__)) {
                request__.getServletRequest().setAttribute(LOCATION, URL__);
                request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocLocation);/*Framework specific*/
                Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0009) + URL__);
            } else {/*Divert to home page*/
                Loggers.DEBUG.debug(RBGet.logMsgs.getString(AI_ILIKEPLACES_SERVLETS_CONTROLLER_0026));
                request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Page.DocAarrr);/*Framework specific*/
                request__.getServletRequest().setAttribute(LOCATION, Controller.EMPTY);
                try {
                    ((HttpServletResponse) response__.getServletResponse()).sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (final IOException e) {
                    Loggers.EXCEPTION.error(Loggers.EMBED, e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Check if the place contains any odd/special characters that are not valid
     * Also check if the url is of itsnat?docblablabla format with "?"
     * Home page will not have any parameters. e.g. ilikeplaces.com/ilikeplaces/page/[nothing here]
     *
     * @param URL__
     * @return boolean
     */
    static private boolean isHomePage(final String URL__) {
        return URL__.length() == 0;
    }

    static private boolean isDownTownPage(final String URL_) {
        return (URL_.startsWith(DT) || URL_.startsWith(DOWNTOWN));
    }

    static private boolean isNonLocationPage(final String URL_) {
        return (URL_.startsWith(UNDERSCORE) || URL_.startsWith(HASH));
    }

    static private boolean isSignOut(final String URL_) {
        return (URL_.equals(_SO));
    }

    static private boolean isPhotoPage(final String URL_) {
        return (URL_.startsWith(_PHOTO_) && URL_.split(UNDERSCORE).length == 4);
    }

    static private String getPhotoLocation(final String URL_) {
        return URL_.replace(_PHOTO_, EMPTY).split(UNDERSCORE)[0];
    }

    static private String getPhotoURL(final String URL_) {
        return URL_.replace(_PHOTO_, EMPTY).split(UNDERSCORE)[1];
    }

    static private boolean isHumanPage(final String URL_) {
        return (URL_.startsWith(_ME));
    }

    static private boolean isOrganizePage(final String URL_) {
        return (URL_.startsWith(_ORG));
    }

    static private boolean isFriendsPage(final String URL_) {
        return (URL_.startsWith(_FRIENDS));
    }

    static private boolean isBookingsPage(final String URL_) {
        return (URL_.startsWith(_BOOK));
    }

    static private boolean isTribesPage(final String URL_) {
        return (URL_.startsWith(_TRIBES));
    }

    static private boolean isProfilePage(final String URL_) {
        return (URL_.startsWith(_PROFILE));
    }

    static private boolean isIPage(final String URL_) {
        return (URL_.startsWith(_I));
    }

    static private boolean isActivatePage(final String URL_) {
        return (URL_.startsWith(_ACTIVATE));
    }

    static private boolean isSharePage(final String URL_) {
        return (URL_.startsWith(_SHARE));
    }

    static private boolean isGeoBusinessPage(final String URL_) {
        return (URL_.startsWith(_GEOBUSINESS));
    }

    static private boolean isTemplateGenericPage(final String URL_) {
        return (URL_.startsWith(_PUBLIC));
    }

    static private boolean isLegalPage(final String URL_) {
        return (URL_.startsWith(_LEGAL));
    }

    static private boolean isMusterPage(final String URL_) {
        return (URL_.startsWith(_MUSTER));
    }

    static private boolean isHelpPage(final String URL_) {
        return (URL_.startsWith(_HELP));
    }

    /**
     * Check if the place contains any odd/special characters that are not valid
     * Also check if the url is of itsnat?docblablabla format with "?"
     *
     * @param URL__
     * @return boolean
     */
    @_fix(issue = "Location should not contain underscores")
    static private boolean isCorrectLocationFormat(final String URL__) {
        /*"_" (underscore) check first is vital as the photo and me urls might have "/"*/
        return !(URL__.startsWith("_") || URL__.contains(","));
    }

    /**
     * Register all your document keys before using. Accepts variable argument length.
     * Usage: putAllPageElementIds("id1","id2","id3");
     *
     * @param ids__
     */
    public final static void PutAllPageElementIds(final Object... ids__) {
        for (final Object id_ : ids__) {
            if (!GlobalHTMLIdRegistry.containsKey(id_.toString())) {
                /**
                 * Do not verify if element exists in "document" here as elements
                 * can be dynamically created
                 */
                GlobalHTMLIdRegistry.put(id_.toString(), id_.toString());
            } else {
                throw new SecurityException("SORRY! THIS KEY IS ALREADY IN REGISTRY:" + id_);
            }
        }
    }
}
