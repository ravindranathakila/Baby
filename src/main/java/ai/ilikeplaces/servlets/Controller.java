package ai.ilikeplaces.servlets;

import ai.ilikeplaces.ListenerLogin;
import ai.ilikeplaces.ListenerMain;
import java.io.IOException;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

/**
 * @TODO Code to disable url calls with itsnat_doc_name=### type urls if possible
 *
 * @author Ravindranath Akila
 */
public class Controller extends HttpServletWrapper {

    private final static Map<Page, String> PrettyURLMap_ = new IdentityHashMap<Page, String>();//Please read javadoc before making any changes to this implementation

    public Controller() {
    }

    /**
     * Initializer for pages with their ids and paths
     */
    public enum Page implements PageFace {

        home(null) {

            @Override
            public String toString() {
                return "/ilikeplaces/page/main";
            }
        },
        main("ai/ilikeplaces/Main.xhtml", "Main_temp1", "Main_temp2", "Main_sidebar") {

            @Override
            public String toString() {
                return "main";
            }
        },
        Photo$Description("ai/ilikeplaces/fragments/Photo-Description.xhtml", "pd") {

            @Override
            public String toString() {
                return "Photo-Description";
            }
        },
        PhotoUpload("ai/ilikeplaces/widgets/PhotoUpload.xhtml"){

            @Override
            public String toString() {
                return "PhotoUpload";
            }
        },
        signup(null) {

            @Override
            public String toString() {
                return "/ilikeplaces/signup";
            }
        },
        login("ai/ilikeplaces/security/login.xhtml") {

            @Override
            public String toString() {
                return "login";
            }
        },
        include("ai/ilikeplaces/security/include.xhtml") {

            @Override
            public String toString() {
                return "include";
            }
        };

        private Page(final String path__, final String... ids__) {
            PrettyURLMap_.put(this, path__);
            PutAllPageElementIds(ids__);
            PutAllPageElementIdsByPage(this, ids__);
        }
    }

    @Override
    public void init(final ServletConfig serveletConfig__) throws ServletException {

        super.init(serveletConfig__);

        final ItsNatHttpServlet inhs__ = getItsNatHttpServlet();

        final ItsNatServletConfig itsNatServletConfig = inhs__.getItsNatServletConfig();
        itsNatServletConfig.setFrameworkScriptFilesBasePath("/ilikeplaces/js");
        itsNatServletConfig.setDebugMode(true);
        itsNatServletConfig.setClientErrorMode(ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS);
        itsNatServletConfig.setLoadScriptInline(true);
        itsNatServletConfig.setUseGZip(UseGZip.SCRIPT);
        itsNatServletConfig.setDefaultSyncMode(SyncMode.SYNC);
        itsNatServletConfig.setAutoCleanEventListeners(true);

        /*Add a listner to convert pretty urls to proper urls*/
        inhs__.addItsNatServletRequestListener(new ItsNatServletRequestListener() {

            @Override
            public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {
                final ItsNatDocument itsNatDocument__ = request__.getItsNatDocument();
                /*if(itsNatDocument != null && ((HttpServletRequest) request__.getServletRequest()).getPathInfo().contains("itsnat_doc_name")){
                throw new java.lang.RuntimeException("INVALID URL");//This code does not seem to work, please verify.
                }*/
                if (itsNatDocument__ == null && request__.getServletRequest().getAttribute("itsnat_doc_name") == null) {
                    final  HttpServletRequest httpServletRequest = (HttpServletRequest) request__.getServletRequest();
                    pathResolver(request__);
                    request__.getItsNatServlet().processRequest(httpServletRequest, response__.getServletResponse());
                }
            }
        });

        final String realPath__ = getServletContext().getRealPath("/");
        final String pathPrefix__ = realPath__ + "WEB-INF/pages/";

        final PageFace main = Page.main;

        inhs__.registerItsNatDocumentTemplate(main.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(main)).addItsNatServletRequestListener(new ListenerMain());

        final PageFace photo$Description = Page.Photo$Description;
        inhs__.registerItsNatDocFragmentTemplate(photo$Description.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(photo$Description));

        final PageFace photoUpload = Page.PhotoUpload;
        inhs__.registerItsNatDocFragmentTemplate(photoUpload.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(photoUpload));

        final PageFace login = Page.login;

        inhs__.registerItsNatDocumentTemplate("login", "text/html", pathPrefix__ + PrettyURLMap_.get(login)).addItsNatServletRequestListener(new ListenerLogin());

        final PageFace include = Page.include;
        inhs__.registerItsNatDocFragmentTemplate("include2", "text/html", pathPrefix__ + PrettyURLMap_.get(include));
    }

    /**
     * We have a URL of type say www.ilikeplaces.com/page/Egypt
     * where we are supposed to recieve the /Egypt part.
     * @param request__
     */
    private static void pathResolver(final ItsNatServletRequest request__) {
        final String location__ = ((HttpServletRequest) request__.getServletRequest()).getPathInfo().substring(1);//Removes preceeding slash
        if (isCorrectLocationFormat(location__)) {
            request__.getServletRequest().setAttribute("location", location__);
        } else {
            request__.getServletRequest().setAttribute("location", null);
        }
        request__.getServletRequest().setAttribute("itsnat_doc_name", "main");
    }

    /**
     * Check if the place contains any odd/special characters that are not valid
     * Also check if the url is of itsnat?docblablabla format with "?"
     * @param location__
     * @return boolean
     */
    private static boolean isCorrectLocationFormat(final String location__) {
        return !(location__.contains("/") || location__.contains(",") || location__.contains("?"));
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
                throw new java.lang.SecurityException("MAP ALREADY CONTAINS THIS \"" + id_ + "\" KEY!");
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
