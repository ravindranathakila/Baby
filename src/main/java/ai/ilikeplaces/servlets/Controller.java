package ai.ilikeplaces.servlets;

import ai.ilikeplaces.ListenerLogin;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.logic.Listeners.ListenerHuman;
import ai.ilikeplaces.logic.Listeners.ListenerMain;
import ai.ilikeplaces.logic.Listeners.ListenerPhoto;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.ClientErrorMode;
import org.itsnat.core.SyncMode;
import org.itsnat.core.UseGZip;
import org.slf4j.Logger;

/**
 * @TODO Code to disable url calls with itsnat_doc_name=### type urls if possible
 *
 * @author Ravindranath Akila
 */
final public class Controller extends HttpServletWrapper {

    private final static Map<PageFace, String> PrettyURLMap_ = new IdentityHashMap<PageFace, String>();//Please read javadoc before making any changes to this implementation
    final static private Logger staticLogger = LoggerFactory.getLogger(Controller.class.getName());
    private static final ResourceBundle logMsgs = ResourceBundle.getBundle("ai/ilikeplaces/rbs/LogMsgs_en_US");

    @WARNING(warning = "Initializer for pages with their ids and paths.\n"
    + "Note that the pages with ID's shall be initialized only once as they will "
    + "be used within this class only. The rest shall write the the list")
    public enum Page implements PageFace {

        home(
        null) {

            @Override
            public String toString() {
                return "/ilikeplaces/page/main";
            }
        },
        main(
        "ai/ilikeplaces/Main.xhtml",
        Controller.Page.mainTitle,
        Controller.Page.Main_center_main,
        Controller.Page.Main_left_column,
        Controller.Page.Main_right_column,
        Controller.Page.Main_sidebar,
        Controller.Page.Main_login_widget,
        Controller.Page.hot,
        Controller.Page.cool) {

            @Override
            public String toString() {
                return "main";
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
            public String toString() {
                return "Photo-Description";
            }
        },
        PhotoUpload(
        "ai/ilikeplaces/widgets/PhotoUpload.xhtml") {

            @Override
            public String toString() {
                return "PhotoUpload";
            }
        },
        signup(
        null) {

            @Override
            public String toString() {
                return "/ilikeplaces/signup";
            }
        },
        login(
        null) {

            @Override
            public String toString() {
                return "/login";
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
            public String toString() {
                return "me";
            }
        },
        oldlogin(
        "ai/ilikeplaces/security/login.xhtml") {

            @Override
            public String toString() {
                return "login";
            }
        },
        include(
        "ai/ilikeplaces/security/include.xhtml") {

            @Override
            public String toString() {
                return "include";
            }
        };
        /*Photo Descrition Specific IDs*/
        final static public String pd = "pd";
        final static public String close = "close";
        final static public String pd_photo_permalink = "pd_photo_permalink";
        final static public String pd_photo = "pd_photo";
        final static public String pd_photo_description = "pd_photo_description";
        /*Main Specific IDs*/
        final static public String mainTitle = "mainTitle";
        final static public String Main_center_main = "Main_center_main";
        final static public String Main_left_column = "Main_left_column";
        final static public String Main_right_column = "Main_right_column";
        final static public String Main_sidebar = "Main_sidebar";
        final static public String Main_login_widget = "Main_login_widget";
        final static public String hot = "hot";
        final static public String cool = "cool";
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

        private Page(final String path__, final String... ids__) {
            PrettyURLMap_.put(this, path__);
            PutAllPageElementIds(ids__);
            PutAllPageElementIdsByPage(this, ids__);
        }
    }

    /**
     *
     * @param serveletConfig__
     * @throws ServletException
     */
    @Override
    public void init(final ServletConfig serveletConfig__) throws ServletException {

        super.init(serveletConfig__);

        final ItsNatHttpServlet inhs__ = getItsNatHttpServlet();

        final ItsNatServletConfig itsNatServletConfig = inhs__.getItsNatServletConfig();
//        itsNatServletConfig.setFrameworkScriptFilesBasePath("/ilikeplaces/js");
        itsNatServletConfig.setDebugMode(true);
        itsNatServletConfig.setClientErrorMode(ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS);
        itsNatServletConfig.setLoadScriptInline(true);
        itsNatServletConfig.setUseGZip(UseGZip.SCRIPT);
        itsNatServletConfig.setDefaultSyncMode(SyncMode.SYNC);
        itsNatServletConfig.setAutoCleanEventListeners(true);

        setDefaultLocale:
        {
            staticLogger.info(logMsgs.getString("ai.ilikeplaces.servlets.Controller.0001"));
            Locale.setDefault(new Locale("en", "US"));
            staticLogger.info(logMsgs.getString("ai.ilikeplaces.servlets.Controller.0002"), Locale.getDefault().toString());
        }

        staticLogger.info(logMsgs.getString("ai.ilikeplaces.servlets.Controller.0003"), Locale.getDefault().toString());

        staticLogger.debug(logMsgs.getString("ai.ilikeplaces.servlets.Controller.0004"), System.getProperty("java.class.path"));

        /*Add a listner to convert pretty urls to proper urls*/
        inhs__.addItsNatServletRequestListener(new ItsNatServletRequestListener() {

            @Override
            public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {
                final ItsNatDocument itsNatDocument__ = request__.getItsNatDocument();
                /*if(itsNatDocument != null && ((HttpServletRequest) request__.getServletRequest()).getPathInfo().contains("itsnat_doc_name")){
                throw new java.lang.RuntimeException("INVALID URL");//This code does not seem to work, please verify.
                }*/
                if (itsNatDocument__ == null && request__.getServletRequest().getAttribute("itsnat_doc_name") == null) {
                    final HttpServletRequest httpServletRequest = (HttpServletRequest) request__.getServletRequest();
                    pathResolver(request__);
                    request__.getItsNatServlet().processRequest(httpServletRequest, response__.getServletResponse());
                }
            }
        });

        final String realPath__ = getServletContext().getRealPath("/");
        final String pathPrefix__ = realPath__ + "WEB-INF/pages/";

        final PageFace main = Page.main;

        inhs__.registerItsNatDocumentTemplate(main.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(main)).addItsNatServletRequestListener(new ListenerMain());

        inhs__.registerItsNatDocumentTemplate("photo", "text/html", pathPrefix__ + PrettyURLMap_.get(main)).addItsNatServletRequestListener(new ListenerPhoto());

        final PageFace PhotoCRUD = Page.PhotoCRUD;
        inhs__.registerItsNatDocumentTemplate(PhotoCRUD.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(main)).addItsNatServletRequestListener(new ListenerHuman());
        inhs__.registerItsNatDocFragmentTemplate(PhotoCRUD.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(PhotoCRUD));

        final PageFace photo$Description = Page.Photo$Description;
        inhs__.registerItsNatDocFragmentTemplate(photo$Description.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(photo$Description));
//
//        final PageFace photoUpload = Page.PhotoUpload;
//        inhs__.registerItsNatDocFragmentTemplate(photoUpload.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(photoUpload));

        final PageFace login = Page.oldlogin;

        inhs__.registerItsNatDocumentTemplate("login", "text/html", pathPrefix__ + PrettyURLMap_.get(login)).addItsNatServletRequestListener(new ListenerLogin());

        final PageFace include = Page.include;
        inhs__.registerItsNatDocFragmentTemplate("include2", "text/html", pathPrefix__ + PrettyURLMap_.get(include));
    }

    /**
     * We have a URL of type say www.ilikeplaces.com/page/Egypt
     * where we are supposed to recieve the /Egypt part. Most requests WILL be
     * location requests so we go optimistic on that first.
     * @param request__
     */
    private static void pathResolver(final ItsNatServletRequest request__) {
        final String pathInfo = ((HttpServletRequest) request__.getServletRequest()).getPathInfo();
        final String URL__ = pathInfo == null ? "" : ((HttpServletRequest) request__.getServletRequest()).getPathInfo().substring(1);//Removes preceeding slash
        if (isNonLocationPage(URL__)) {
            if (isPhotoPage(URL__)) {
                request__.getServletRequest().setAttribute("location", getPhotoLocation(URL__));
                request__.getServletRequest().setAttribute("photoURL", getPhotoURL(URL__));
                request__.getServletRequest().setAttribute("itsnat_doc_name", "photo");/*Framework specific*/
                staticLogger.info(logMsgs.getString("ai.ilikeplaces.servlets.Controller.0005") + getPhotoLocation(URL__));
                staticLogger.info(logMsgs.getString("ai.ilikeplaces.servlets.Controller.0006") + getPhotoURL(URL__));
            } else if (isHumanPage(URL__)) {
                request__.getServletRequest().setAttribute("itsnat_doc_name", "me");/*Framework specific*/
                staticLogger.info(logMsgs.getString("ai.ilikeplaces.servlets.Controller.0007"));
            } else {
                request__.getServletRequest().setAttribute("location", "main");
                request__.getServletRequest().setAttribute("itsnat_doc_name", "main");/*Framework specific*/
                staticLogger.info(logMsgs.getString("ai.ilikeplaces.servlets.Controller.0008"));
            }
        } else if (isCorrectLocationFormat(URL__)) {
            request__.getServletRequest().setAttribute("location", URL__);
            request__.getServletRequest().setAttribute("itsnat_doc_name", "main");/*Framework specific*/
            staticLogger.info(logMsgs.getString("ai.ilikeplaces.servlets.Controller.0009") + URL__);
        } else {
            request__.getServletRequest().setAttribute("location", "main");/*Main shall serve as the main page*/
            request__.getServletRequest().setAttribute("itsnat_doc_name", "main");/*Framework specific*/
            staticLogger.info(logMsgs.getString("ai.ilikeplaces.servlets.Controller.0010"));
        }
    }

    /**
     * Check if the place contains any odd/special characters that are not valid
     * Also check if the url is of itsnat?docblablabla format with "?"
     * @param location__
     * @return boolean
     */
    static private boolean isCorrectLocationFormat(final String URL__) {
        /*"_" check first is vital as the photo and me urls might have "/"*/
        return !(URL__.startsWith("_") || URL__.contains("/") || URL__.contains(",") || URL__.contains("?"));
    }

    static private boolean isNonLocationPage(final String URL_) {
        return (URL_.startsWith("_"));
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
    /**
     * This Map is static as Id's in html documents should be universally identical, i.e. as htmldocname_elementId
     */
    public final static Map<String, String> GlobalHTMLIdRegistry = new IdentityHashMap<String, String>();

    /**
     * Register all your document keys before using. Acceps variable argument length.
     * Usage: putAllPageElementIds("id1","id2","id3");
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
                throw new SecurityException(logMsgs.getString("ai.ilikeplaces.servlets.Controller.0011" + id_));
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
     *
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
