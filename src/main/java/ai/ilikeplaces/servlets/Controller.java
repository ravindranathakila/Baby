package ai.ilikeplaces.servlets;

import ai.ilikeplaces.depricated.ListenerLogin;
import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.logic.Listeners.*;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.Loggers;
import org.itsnat.core.*;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author Ravindranath Akila
 * @TODO Code to disable url calls with itsnat_doc_name=### type urls if possible
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class Controller extends HttpServletWrapper {

    private final static Map<PageFace, String> PrettyURLMap_ = new IdentityHashMap<PageFace, String>();//Please read javadoc before making any changes to this implementation
    final static private Logger staticLogger = LoggerFactory.getLogger(Controller.class.getName());
    private static final String ITSNAT_DOC_NAME = "itsnat_doc_name";

    @NOTE(note = "Inner Enums are static. Therefore, the lists shall be populated only once.")
    public enum Page implements PageFace {
        PrivateEventCreate("ai/ilikeplaces/widgets/privateevent/private_event_create.xhtml",
                Controller.Page.privateEventCreateName,
                Controller.Page.privateEventCreateInfo,
                Controller.Page.privateEventCreateSave,
                Controller.Page.privateEventCreateNotice
        ) {

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocPrivateEventCreate;
            }},

        PrivateEventView("ai/ilikeplaces/widgets/privateevent/private_event_view.xhtml",
                Controller.Page.privateEventViewNotice,
                Controller.Page.privateEventViewName,
                Controller.Page.privateEventViewInfo,
                Controller.Page.privateEventViewOwners,
                Controller.Page.privateEventViewVisitors
        ) {

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocPrivateEventView;
            }},


        PrivateEventDelete("ai/ilikeplaces/widgets/privateevent/private_event_delete.xhtml",
                Controller.Page.privateEventDeleteName,
                Controller.Page.privateEventDeleteInfo,
                Controller.Page.privateEventDeleteNotice,
                Controller.Page.privateEventDelete
        ) {

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocPrivateEventDelete;
            }},

        FindFriend("ai/ilikeplaces/widgets/friend/friend_find.xhtml",
                Controller.Page.friendFindSearchTextInput,
                Controller.Page.friendFindSearchButtonInput,
                Controller.Page.friendFindSearchResults
        ) {

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocFindFriend;
            }},

        FriendAdd("ai/ilikeplaces/widgets/friend/friend_add.xhtml",
                Controller.Page.friendAddAddButton,
                Controller.Page.friendAddBirthYearLabel,
                Controller.Page.friendAddFirstNameLabel,
                Controller.Page.friendAddLastNameLabel,
                Controller.Page.friendAddLocationLabel
        ) {

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocFriendAdd;
            }},

        FriendDelete("ai/ilikeplaces/widgets/friend/friend_delete.xhtml",
                Controller.Page.friendDeleteAddButton,
                Controller.Page.friendDeleteBirthYearLabel,
                Controller.Page.friendDeleteFirstNameLabel,
                Controller.Page.friendDeleteLastNameLabel,
                Controller.Page.friendDeleteLocationLabel
        ) {

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocFriendDelete;
            }},

        Organize(null
        ) {

            @Override
            public String getURL() {
                return RBGet.getGlobalConfigKey("AppRoot") + "page/_org";
            }

            @Override
            public String toString() {
                return DocOrganize;
            }},

        Friends(null
        ) {

            @Override
            public String getURL() {
                return RBGet.getGlobalConfigKey("AppRoot") + "page/_friends";
            }

            @Override
            public String toString() {
                return DocFriends;
            }},

        Skeleton("ai/ilikeplaces/Skeleton.xhtml",
                Controller.Page.skeletonTitle,
                Controller.Page.Skeleton_center,
                Controller.Page.Skeleton_center_content,
                Controller.Page.Skeleton_center_skeleton,
                Controller.Page.Skeleton_file_list,
                Controller.Page.Skeleton_left_column,
                Controller.Page.Skeleton_login_widget,
                Controller.Page.Skeleton_notice,
                Controller.Page.Skeleton_notice_sh,
                Controller.Page.Skeleton_othersidebar,
                Controller.Page.Skeleton_othersidebar_identity,
                Controller.Page.Skeleton_othersidebar_organizer_link,
                Controller.Page.Skeleton_othersidebar_photo_manager_link,
                Controller.Page.Skeleton_othersidebar_places_link,
                Controller.Page.Skeleton_othersidebar_profile_link,
                Controller.Page.Skeleton_othersidebar_upload_file_sh,
                Controller.Page.Skeleton_right_column,
                Controller.Page.Skeleton_sidebar

        ) {

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocSkeleton;
            }},
        PrivateLocationCreate("ai/ilikeplaces/widgets/privatelocation/private_location_create.xhtml",
                Controller.Page.privateLocationCreateName,
                Controller.Page.privateLocationCreateInfo,
                Controller.Page.privateLocationCreateSave,
                Controller.Page.privateLocationCreateNotice
        ) {

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocPrivateLocationCreate;
            }},

        PrivateLocationView("ai/ilikeplaces/widgets/privatelocation/private_location_view.xhtml",
                Controller.Page.privateLocationViewNotice,
                Controller.Page.privateLocationViewName,
                Controller.Page.privateLocationViewInfo,
                Controller.Page.privateLocationViewOwners,
                Controller.Page.privateLocationViewVisitors
        ) {

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocPrivateLocationView;
            }},


        PrivateLocationDelete("ai/ilikeplaces/widgets/privatelocation/private_location_delete.xhtml",
                Controller.Page.privateLocationDeleteName,
                Controller.Page.privateLocationDeleteInfo,
                Controller.Page.privateLocationDeleteNotice,
                Controller.Page.privateLocationDelete
        ) {

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return DocPrivateLocationDelete;
            }},

        home(
                null) {
            @Override
            public String getURL() {
                return RBGet.getGlobalConfigKey("AppRoot") + "page/main";
            }
            @Override
            public String toString() {
                return "/page/main";
            }
        },
        LocationMain(
                "ai/ilikeplaces/Main.xhtml",
                Controller.Page.mainTitle,
                Controller.Page.Main_othersidebar_identity,
                Controller.Page.Main_othersidebar_profile_link,
                Controller.Page.Main_othersidebar_upload_file_sh,
                Controller.Page.Main_notice_sh,
                Controller.Page.Main_center_main,
                Controller.Page.Main_notice,
                Controller.Page.Main_center_main_location_title,
                Controller.Page.Main_center_content,
                Controller.Page.Main_left_column,
                Controller.Page.Main_right_column,
                Controller.Page.Main_sidebar,
                Controller.Page.Main_login_widget) {
            @Override
            public String getURL() {
                return RBGet.getGlobalConfigKey("AppRoot") + "page/main";
            }
            @Override
            public String toString() {
                return DocLocation;
            }
        },
        Aarrr(
                "ai/ilikeplaces/AARRR.xhtml",
                Controller.Page.AarrrTitle,
                Controller.Page.AarrrEmail,
                Controller.Page.AarrrFunTypes) {
            @Override
            public String getURL() {
                return RBGet.getGlobalConfigKey("AppRoot") + "page/";
            }

            @Override
            public String toString() {
                return DocAarrr;
            }
        },
        Photo$Description(
                "ai/ilikeplaces/widgets/Photo-Description.xhtml",
                Controller.Page.pd,
                Controller.Page.close,
                Controller.Page.pd_photo_permalink,
                Controller.Page.pd_photo,
                Controller.Page.pd_photo_description) {

            @Override
            public String getURL() {
                throw new IllegalAccessError("SORRY! THIS IS A TEMPLATE WITH NO SPECIFIC PAGE OF WHICH YOU WANT THE URL.");
            }

            @Override
            public String toString() {
                return "Photo-Description";
            }
        },
        PhotoUpload(
                "ai/ilikeplaces/widgets/PhotoUpload.xhtml") {

            @Override
            public String getURL() {
                return RBGet.getGlobalConfigKey("AppRoot") + "page/main";
            }

            @Override
            public String toString() {
                return "PhotoUpload";
            }
        },
        signup(
                null) {
            @Override
            public String getURL() {
                return RBGet.getGlobalConfigKey("AppRoot") + "signup";
            }
            @Override
            public String toString() {
                return RBGet.getGlobalConfigKey("AppRoot") + "signup";
            }
        },
        login(
                null) {
            @Override
            public String getURL() {
                return RBGet.getGlobalConfigKey("AppRoot") + "login";
            }
            @Override
            public String toString() {
                return RBGet.getGlobalConfigKey("AppRoot") + "login";
            }
        },
        PhotoCRUD(
                "ai/ilikeplaces/widgets/PhotoCRUD.xhtml",
                Controller.Page.pc_photo_title,
                Controller.Page.pc_close,
                Controller.Page.pc,
                Controller.Page.pc_photo,
                Controller.Page.pc_photo_permalink,
                Controller.Page.pc_photo_name,
                Controller.Page.pc_update_name,
                Controller.Page.pc_photo_description,
                Controller.Page.pc_delete,
                Controller.Page.pc_update_description) {
            @Override
            public String getURL() {
                return RBGet.getGlobalConfigKey("AppRoot") + "page/_me";
            }
            @Override
            public String toString() {
                return "me";
            }
        },
        SignInOn(
                "ai/ilikeplaces/widgets/SignInOn.xhtml",
                Controller.Page.signinon_login,
                Controller.Page.signinon_logon
        ) {
            @Override
            public String getURL() {
                return RBGet.getGlobalConfigKey("AppRoot") + "page/main";
            }
            @Override
            public String toString() {
                return "DocSignInOn";
            }
        };

        /*Private Event Page*/
        final static public String DocPrivateEventView = "PrivateEventView";
        /*Private Event Create IDs*/
        final static public String privateEventViewNotice = "privateEventViewNotice";
        final static public String privateEventViewName = "privateEventViewName";
        final static public String privateEventViewInfo = "privateEventViewInfo";
        final static public String privateEventViewOwners = "privateEventViewOwners";
        final static public String privateEventViewVisitors = "privateEventViewVisitor";

        /*Private Event Page*/
        final static public String DocPrivateEventCreate = "PrivateEventCreate";
        /*Private Event Create IDs*/
        final static public String privateEventCreateName = "privateEventCreateName";
        final static public String privateEventCreateInfo = "privateEventCreateInfo";
        final static public String privateEventCreateSave = "privateEventCreateSave";
        final static public String privateEventCreateNotice = "privateEventCreateNotice";

        /*Private Event Page*/
        final static public String DocPrivateEventDelete = "PrivateEventDelete";
        /*Private Event Create IDs*/
        final static public String privateEventDeleteName = "privateEventDeleteName";
        final static public String privateEventDeleteInfo = "privateEventDeleteInfo";
        final static public String privateEventDeleteNotice = "privateEventDeleteNotice";
        final static public String privateEventDelete = "privateEventDelete";

        /*FindFriend Page*/
        final static public String DocFindFriend = "DocFindFriend";
        /*FindFriend IDs*/
        final static public String friendFindSearchTextInput = "friendFindSearchTextInput";
        final static public String friendFindSearchButtonInput = "friendFindSearchButtonInput";
        final static public String friendFindSearchResults = "friendFindSearchResults";

        /*AddFriend Page*/
        final static public String DocFriendAdd = "DocFriendAdd";
        /*AddFriend IDs*/
        final static public String friendAddFirstNameLabel = "friendAddFirstNameLabel";
        final static public String friendAddLastNameLabel = "friendAddLastNameLabel";
        final static public String friendAddBirthYearLabel = "friendAddBirthYearLabel";
        final static public String friendAddLocationLabel = "friendAddLocationLabel";
        final static public String friendAddAddButton = "friendAddAddButton";

        /*DeleteFriend Page*/
        final static public String DocFriendDelete = "DocFriendDelete";
        /*DeleteFriend IDs*/
        final static public String friendDeleteFirstNameLabel = "friendDeleteFirstNameLabel";
        final static public String friendDeleteLastNameLabel = "friendDeleteLastNameLabel";
        final static public String friendDeleteBirthYearLabel = "friendDeleteBirthYearLabel";
        final static public String friendDeleteLocationLabel = "friendDeleteLocationLabel";
        final static public String friendDeleteAddButton = "friendDeleteDeleteButton";

        /*Organize Page*/
        final static public String DocOrganize = "DocOrganize";

        /*FriendFind Page*/
        final static public String DocFriends = "DocFriends";

        /*Skeleton Page*/
        final static public String DocSkeleton = "DocPrivateLocationCreate";
        /*Skeleton Create IDs*/
        final static public String skeletonTitle = "skeletonTitle";
        final static public String Skeleton_login_widget = "Skeleton_login_widget";
        final static public String Skeleton_othersidebar = "Skeleton_othersidebar";
        final static public String Skeleton_othersidebar_identity = "Skeleton_othersidebar_identity";
        final static public String Skeleton_othersidebar_places_link = "Skeleton_othersidebar_places_link";
        final static public String Skeleton_othersidebar_profile_link = "Skeleton_othersidebar_profile_link";
        final static public String Skeleton_othersidebar_photo_manager_link = "Skeleton_othersidebar_photo_manager_link";
        final static public String Skeleton_othersidebar_organizer_link = "Skeleton_othersidebar_organizer_link";
        final static public String Skeleton_othersidebar_upload_file_sh = "Skeleton_othersidebar_upload_file_sh";
        final static public String Skeleton_file_list = "Skeleton_file_list";
        final static public String Skeleton_center = "Skeleton_center";
        final static public String Skeleton_center_skeleton = "Skeleton_center_skeleton";
        final static public String Skeleton_notice_sh = "Skeleton_notice_sh";
        final static public String Skeleton_notice = "Skeleton_notice";
        final static public String Skeleton_center_content = "Skeleton_center_content";
        final static public String Skeleton_left_column = "Skeleton_left_column";
        final static public String Skeleton_right_column = "Skeleton_right_column";
        final static public String Skeleton_sidebar = "Skeleton_sidebar";

        /*Private Location Page*/
        final static public String DocPrivateLocationView = "PrivateLocationView";
        /*Private Location Create IDs*/
        final static public String privateLocationViewNotice = "privateLocationViewNotice";
        final static public String privateLocationViewName = "privateLocationViewName";
        final static public String privateLocationViewInfo = "privateLocationViewInfo";
        final static public String privateLocationViewOwners = "privateLocationViewOwners";
        final static public String privateLocationViewVisitors = "privateLocationViewVisitor";

        /*Private Location Page*/
        final static public String DocPrivateLocationCreate = "PrivateLocationCreate";
        /*Private Location Create IDs*/
        final static public String privateLocationCreateName = "privateLocationCreateName";
        final static public String privateLocationCreateInfo = "privateLocationCreateInfo";
        final static public String privateLocationCreateSave = "privateLocationCreateSave";
        final static public String privateLocationCreateNotice = "privateLocationCreateNotice";

        /*Private Location Page*/
        final static public String DocPrivateLocationDelete = "PrivateLocationDelete";
        /*Private Location Create IDs*/
        final static public String privateLocationDeleteName = "privateLocationDeleteName";
        final static public String privateLocationDeleteInfo = "privateLocationDeleteInfo";
        final static public String privateLocationDeleteNotice = "privateLocationDeleteNotice";
        final static public String privateLocationDelete = "privateLocationDelete";

        /*Photo Descrition Specific IDs*/
        final static public String pd = "pd";
        final static public String close = "close";
        final static public String pd_photo_permalink = "pd_photo_permalink";
        final static public String pd_photo = "pd_photo";
        final static public String pd_photo_description = "pd_photo_description";


        /*Aarrr Page*/
        final static public String DocAarrr = "Aarrr";
        /*Aarrr Specific IDs*/
        final static public String AarrrTitle = "AarrrTitle";
        final static public String AarrrFunTypes = "AarrrFunTypes";
        final static public String AarrrEmail = "AarrrEmail";


        /*DocLocation Page*/
        final static public String DocLocation = "DocLocation";

        /*Main Specific IDs*/
        final static public String mainTitle = "mainTitle";
        final static public String Main_othersidebar_identity = "Main_othersidebar_identity";
        final static public String Main_othersidebar_profile_link = "Main_othersidebar_profile_link";
        final static public String Main_othersidebar_upload_file_sh = "Main_othersidebar_upload_file_sh";
        final static public String Main_center_main = "Main_center_main";
        final static public String Main_notice = "Main_notice";
        final static public String Main_notice_sh = "Main_notice_sh";
        final static public String Main_center_main_location_title = "Main_center_main_location_title";
        final static public String Main_center_content = "Main_center_content";
        final static public String Main_left_column = "Main_left_column";
        final static public String Main_right_column = "Main_right_column";
        final static public String Main_sidebar = "Main_sidebar";
        final static public String Main_login_widget = "Main_login_widget";

        /*PhotoCRUD Specific IDs*/
        final static public String pc_photo_title = "pc_photo_title";
        final static public String pc_close = "pc_close";
        final static public String pc = "pc";
        final static public String pc_photo = "pc_photo";
        final static public String pc_photo_permalink = "pc_photo_permalink";
        final static public String pc_photo_name = "pc_photo_name";
        final static public String pc_update_name = "pc_update_name";
        final static public String pc_photo_description = "pc_photo_description";
        final static public String pc_delete = "pc_delete";
        final static public String pc_update_description = "pc_update_description";

        /*SignInOn Page*/
        final static public String DocSignInOn = "SignInOn";
        /*SignInOn Specific IDs*/
        final static public String signinon_login = "signinon_login";
        final static public String signinon_logon = "signinon_logon";

        private Page(final String path__, final String... ids__) {
            Loggers.DEBUG.debug("HELLO, ENUM VAL:" + this.name());
            Loggers.DEBUG.debug("HELLO, ENUM PATH:" + path__);
            Loggers.DEBUG.debug("HELLO, ENUM IDS:" + Arrays.toString(ids__));
            PrettyURLMap_.put(this, path__);
            PutAllPageElementIds(ids__);
            PutAllPageElementIdsByPage(this, ids__);
        }

        abstract public String toString();

        abstract public String getURL();
    }

    final PageFace locationMain = Page.LocationMain;
    final PageFace aarrr = Page.Aarrr;
    final PageFace photoCRUD = Page.PhotoCRUD;
    final PageFace photo$Description = Page.Photo$Description;
    final PageFace signInOn = Page.SignInOn;

    final PageFace privateLocationCreate = Page.PrivateLocationCreate;
    final PageFace privateLocationView = Page.PrivateLocationView;
    final PageFace privateLocationDelete = Page.PrivateLocationDelete;

    final PageFace skeleton = Page.Skeleton;
    final PageFace organize = Page.Organize;
    final PageFace findFriend = Page.Friends;

    final PageFace findFriendWidget = Page.FindFriend;
    final PageFace friendAdd = Page.FriendAdd;
    final PageFace friendDelete = Page.FriendDelete;


    /**
     * @param serveletConfig__
     * @throws ServletException
     */
    @Override
    public void init(final ServletConfig serveletConfig__) throws ServletException {

        super.init(serveletConfig__);

        final ItsNatHttpServlet inhs__ = getItsNatHttpServlet();

        final ItsNatServletConfig itsNatServletConfig = inhs__.getItsNatServletConfig();

        itsNatServletConfig.setDebugMode(true);
        itsNatServletConfig.setClientErrorMode(ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS);
        itsNatServletConfig.setLoadScriptInline(true);
        itsNatServletConfig.setUseGZip(UseGZip.SCRIPT);
        itsNatServletConfig.setDefaultSyncMode(SyncMode.SYNC);
        itsNatServletConfig.setAutoCleanEventListeners(true);

        setDefaultLocale:
        {
            staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0001"));
            Locale.setDefault(new Locale("en", "US"));
            staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0002"), Locale.getDefault().toString());
        }

        staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0003"), Locale.getDefault().toString());


        /*Add a listner to convert pretty urls to proper urls*/
        inhs__.addItsNatServletRequestListener(new ItsNatServletRequestListener() {

            @Override
            public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {
                final ItsNatDocument itsNatDocument__ = request__.getItsNatDocument();
                /*if(itsNatDocument != null && ((HttpServletRequest) request__.getServletRequest()).getPathInfo().contains("itsnat_doc_name")){
                throw new java.lang.RuntimeException("INVALID URL");//This code does not seem to work, please verify.
                }*/
                if (itsNatDocument__ == null && request__.getServletRequest().getAttribute(ITSNAT_DOC_NAME) == null) {
                    final HttpServletRequest httpServletRequest = (HttpServletRequest) request__.getServletRequest();
                    pathResolver(request__);
                    request__.getItsNatServlet().processRequest(httpServletRequest, response__.getServletResponse());
                }
            }
        });

        final String realPath__ = getServletContext().getRealPath("/");
        /*final String pathPrefix__ = realPath__ + "WEB-INF/pages/";*/

        final String pathPrefix__ = RBGet.getGlobalConfigKey("PAGEFILES") != null ? RBGet.getGlobalConfigKey("PAGEFILES") : realPath__ + "WEB-INF/pages/";

        registerDocumentTemplates:
        {
            inhs__.registerItsNatDocumentTemplate(aarrr.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(aarrr)).addItsNatServletRequestListener(new ListenerAarrr());

            inhs__.registerItsNatDocumentTemplate(locationMain.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(locationMain)).addItsNatServletRequestListener(new ListenerMain());

            inhs__.registerItsNatDocumentTemplate("photo", "text/html", pathPrefix__ + PrettyURLMap_.get(locationMain)).addItsNatServletRequestListener(new ListenerPhoto());

            inhs__.registerItsNatDocumentTemplate(photoCRUD.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(locationMain)).addItsNatServletRequestListener(new ListenerHuman());

            inhs__.registerItsNatDocumentTemplate(organize.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(skeleton)).addItsNatServletRequestListener(new ListenerOrganize());

            inhs__.registerItsNatDocumentTemplate(findFriend.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(skeleton)).addItsNatServletRequestListener(new ListenerFriends());
        }

        registerDocumentFragmentTemplatesAKAWidgets:
        {
            inhs__.registerItsNatDocFragmentTemplate(photoCRUD.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(photoCRUD));

            inhs__.registerItsNatDocFragmentTemplate(photo$Description.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(photo$Description));

            inhs__.registerItsNatDocFragmentTemplate(signInOn.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(signInOn));

            inhs__.registerItsNatDocFragmentTemplate(privateLocationCreate.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(privateLocationCreate));

            inhs__.registerItsNatDocFragmentTemplate(privateLocationView.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(privateLocationView));

            inhs__.registerItsNatDocFragmentTemplate(privateLocationDelete.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(privateLocationDelete));

            inhs__.registerItsNatDocFragmentTemplate(findFriendWidget.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(findFriendWidget));

            inhs__.registerItsNatDocFragmentTemplate(friendAdd.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(friendAdd));

            inhs__.registerItsNatDocFragmentTemplate(friendDelete.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(friendDelete));
        }
    }

    /**
     * We have a URL of type say www.ilikeplaces.com/page/Egypt
     * where we are supposed to recieve the /Egypt part. Most requests WILL be
     * location requests so we go optimistic on that first.
     *
     * @param request__
     */
    private static void pathResolver(final ItsNatServletRequest request__) {
        final String pathInfo = ((HttpServletRequest) request__.getServletRequest()).getPathInfo();
        final String URL__ = pathInfo == null ? "" : ((HttpServletRequest) request__.getServletRequest()).getPathInfo().substring(1);//Removes preceeding slash
        if (isHomePage(URL__)) {
            staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0012"));
            request__.getServletRequest().setAttribute("location", "");
            request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Page.DocAarrr);/*Framework specific*/
        } else {
            if (isNonLocationPage(URL__)) {/*i.e. starts with underscore*/
                final HttpSession httpSession = ((HttpServletRequest) request__.getServletRequest()).getSession(false);
                if (isSignOut(URL__)) {
                    if (httpSession != null) {
                        try {
                            ((HttpServletRequest) request__.getServletRequest()).getSession(false).invalidate();
                        } finally {
                            request__.getServletRequest().setAttribute("location", "");
                            request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Page.DocAarrr);/*Framework specific*/
                        }
                    }
                } else if (isPhotoPage(URL__)) {
                    request__.getServletRequest().setAttribute(RBGet.config.getString("HttpSessionAttr.location"), getPhotoLocation(URL__));
                    request__.getServletRequest().setAttribute("photoURL", getPhotoURL(URL__));
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, "photo");/*Framework specific*/
                    staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0005") + getPhotoLocation(URL__));
                    staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0006") + getPhotoURL(URL__));
                } else if (isHumanPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, "me");/*Framework specific*/
                    staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0007"));
                } else if (isOrganizePage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocOrganize);/*Framework specific*/
                    final String c = request__.getServletRequest().getParameter("category");
                    if (c != null) {
                        try {
                            Integer category = null;
                            category = Integer.parseInt(c);
                            switch (category) {
                                /*Manage Mode*/
                                case 0:
                                    break;
                                /*Location Mode*/
                                case 1:
                                    
                                    break;
                                /*Private Location Mode*/
                                case 2:
                                    break;
                                default:
                                    throw new NumberFormatException("SORRY! WRONG CATEGORY.");
                            }
                        } catch (final NumberFormatException e_) {
                            Loggers.EXCEPTION.error("SORRY! I ENCOUNTERED A CATEGORY FORMAT ERROR.", e_);
                        }
                    }

                    staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0013"));
                } else if (isFriendsPage(URL__)) {
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocFriends);/*Framework specific*/
                    staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0013"));
                } else {/*Divert to home page*/
                    staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0008"));
                    request__.getServletRequest().setAttribute("location", "");
                    request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Page.DocAarrr);/*Framework specific*/
                }
            } else if (isCorrectLocationFormat(URL__)) {
                request__.getServletRequest().setAttribute("location", URL__);
                request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Controller.Page.DocLocation);/*Framework specific*/
                staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0009") + URL__);
            } else {/*Divert to home page*/
                staticLogger.info(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0012"));
                request__.getServletRequest().setAttribute("location", "");
                request__.getServletRequest().setAttribute(ITSNAT_DOC_NAME, Page.DocAarrr);/*Framework specific*/
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

    /**
     * Check if the place contains any odd/special characters that are not valid
     * Also check if the url is of itsnat?docblablabla format with "?"
     *
     * @param URL__
     * @return boolean
     */
    @FIXME(issue = "Location should not contain underscores")
    static private boolean isCorrectLocationFormat(final String URL__) {
        /*"_" (underscore) check first is vital as the photo and me urls might have "/"*/
        return !(URL__.startsWith("_") || URL__.contains("/") || URL__.contains(",") || URL__.contains("?"));
    }

    static private boolean isNonLocationPage(final String URL_) {
        return (URL_.startsWith("_") || URL_.startsWith("#"));
    }

    static private boolean isHumanPage(final String URL_) {
        return (URL_.startsWith("_me"));
    }

    static private boolean isPhotoPage(final String URL_) {
        return (URL_.startsWith("_photo_") && URL_.split("_").length == 4);
    }

    static private String getPhotoLocation(final String URL_) {
        return URL_.replace("_photo_", "").split("_")[0];
    }

    static private String getPhotoURL(final String URL_) {
        return URL_.replace("_photo_", "").split("_")[1];
    }

    static private boolean isOrganizePage(final String URL_) {
        return (URL_.startsWith("_org"));
    }

    static private boolean isFriendsPage(final String URL_) {
        return (URL_.startsWith("_friends"));
    }

    static private boolean isSignOut(final String URL_) {
        return (URL_.equals("_so"));
    }

    /**
     * This Map is static as Id's in html documents should be universally identical, i.e. as htmldocname_elementId
     */
    public final static Map<String, String> GlobalHTMLIdRegistry = new IdentityHashMap<String, String>();

    /**
     * Register all your document keys before using. Acceps variable argument length.
     * Usage: putAllPageElementIds("id1","id2","id3");
     *
     * @param ids__
     */
    public final static void PutAllPageElementIds(final String... ids__) {
        for (final String id_ : ids__) {
            if (!GlobalHTMLIdRegistry.containsKey(id_)) {
                /**
                 * Do not verify if element exists in "document" here as elements
                 * can be dynamically created
                 */
                GlobalHTMLIdRegistry.put(id_, id_);
            } else {
                throw new SecurityException(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0011" + id_));
            }
        }
    }

    /**
     * Retrievable list of all element Ids by Page
     */
    public final static Map<PageFace, HashSet<String>> GlobalPageIdRegistry = new IdentityHashMap<PageFace, HashSet<String>>();

    private final static void PutAllPageElementIdsByPage(final PageFace page__, final String... ids__) {
        final HashSet<String> ids_ = new HashSet<String>();
        for (final String id_ : ids__) {
            /**
             * Do not verify if element exists in "document" here as elements
             * can be dynamically created
             */
            ids_.add(id_);
        }
        GlobalPageIdRegistry.put(page__, ids_);
    }

    /**
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = new String(toString() + "\n");
        changeLog += "20090918 Addedhome link in the enum, to be used by everybody. \n";
        changeLog += "20090924 Changed enum type to be accessible via interface for flexibility\n";
        return showChangeLog__ ? changeLog : toString();
    }
}
