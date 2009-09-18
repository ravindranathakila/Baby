package ai.ilikeplaces.servlets;

import ai.ilikeplaces.ListenerLogin;
import ai.ilikeplaces.ListenerMain;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    public enum Page {

        home(""),
        main("ai/ilikeplaces/Main.xhtml", "Main_temp1", "Main_temp2", "Main_sidebar"),
        Photo$Description("ai/ilikeplaces/fragments/Photo-Description.xhtml", "pd"),
        login("ai/ilikeplaces/security/login.xhtml"),
        include("ai/ilikeplaces/security/include.xhtml");

        private Page(final String path__, final String... ids__) {
            PrettyURLMap_.put(this, path__);
            PutAllPageElementIds(ids__);
            PutAllPageElementIdsByPage(this,ids__);
        }

        @Override
        public String toString() {
            String returnVal = null;
            switch (this) {
                case home:
                    returnVal = "/ilikeplaces/page/main";
                    break;
                case main:
                    returnVal = "main";
                    break;
                case Photo$Description:
                    returnVal = "Photo-Description";
                    break;
                case login:
                    returnVal = "login";
                    break;
                case include:
                    returnVal = "include";
                    break;
            }
            return returnVal;
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

            public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {
                final ItsNatDocument itsNatDocument__ = request__.getItsNatDocument();
                /*if(itsNatDocument != null && ((HttpServletRequest) request__.getServletRequest()).getPathInfo().contains("itsnat_doc_name")){
                throw new java.lang.RuntimeException("INVALID URL");//This code does not seem to work, please verify.
                }*/
                if (itsNatDocument__ == null && request__.getServletRequest().getAttribute("itsnat_doc_name") == null) {
                    HttpServletRequest httpServletRequest = (HttpServletRequest) request__.getServletRequest();
//                    final String pathInfo__ = httpServletRequest.getPathInfo();
//                    request__.getServletRequest().setAttribute("itsnat_doc_name", pathInfo__.substring(1));
                    pathResolver(request__);
                    request__.getItsNatServlet().processRequest(httpServletRequest, response__.getServletResponse());
                }
            }
        });

        final String realPath__ = getServletContext().getRealPath("/");
        final String pathPrefix__ = realPath__ + "WEB-INF/pages/";

        inhs__.registerItsNatDocumentTemplate(Page.main.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(Page.main)).addItsNatServletRequestListener(new ListenerMain());

        inhs__.registerItsNatDocumentTemplate("login", "text/html", pathPrefix__ + PrettyURLMap_.get(Page.login)).addItsNatServletRequestListener(new ListenerLogin());

        inhs__.registerItsNatDocFragmentTemplate(Page.Photo$Description.toString(), "text/html", pathPrefix__ + PrettyURLMap_.get(Page.Photo$Description));


        inhs__.registerItsNatDocFragmentTemplate("include2", "text/html", pathPrefix__ + PrettyURLMap_.get(Page.include));
    }

//    @Override
//    public void doGet(HttpServletRequest request__, HttpServletResponse response__) throws ServletException, IOException{
//
//        final HttpSession UserSession = request__.getSession(false);
//
//        /**
//         * We have two types of users.
//         * > Users who do not wish to login to this site(just browse)
//         * and
//         * > who have an account.
//         *      Users who have an account are either
//         * >> Logged in
//         * >> Logged out(normal or timeout)
//         * Login is handled by the login servlet, which will set "User" parameter
//         * to be true. i.e. this is a user which has an account.
//         * That servlet will also add the relevant stateful session bean to the
//         * users session.
//         *
//         * Now in this servlet,
//         * 1. We have to let web users pass by without interfereing their
//         * request.
//         * 2. We have to make sure the "user" is logged in if not, by verifying
//         * that the stateful session bean is present. If not, redirect him to the
//         * login page.
//         *
//         */
//        if(UserSession != null){/*Do not process a general user, proceed at the earliest (e.g. Refered from a search page*/
//            /*Ok the httpsession is live, is this user logged in?*/
//            if(UserSession.getAttribute(ServletLogin.User) != null){
//                //This is a user.
//                //Put so in the request and itsnathttpsession too.
//                //do all this in the sign in servlet, except itsnathttpsession
//
//            } else {/*Web user who somehow had a session object*/
//                super.doGet(request__, response__);
//            }
//        } else {/*Ok user came directly to pages, he does not want to login, and did not visit the login page*/
//            super.doGet(request__, response__);
//        }
//    }
    @Override
    public void doPost(HttpServletRequest request__, HttpServletResponse response__) throws ServletException, IOException{
        super.doPost(request__, response__);
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
    public final static Map<Page, HashSet<String>>  GlobalPageIdRegistry = new IdentityHashMap<Page, HashSet<String>>();

    private final static void PutAllPageElementIdsByPage(final Page page__, final String... ids__) {
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
        changeLog += "20090918 Inluded home link in the enum, to be used by everybody. \n";
        return showChangeLog__ ? changeLog : toString();
    }
}
