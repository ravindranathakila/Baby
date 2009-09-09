package ai.ilikeplaces.servlets;

import ai.ilikeplaces.ListenerLogin;
import ai.ilikeplaces.ListenerMain;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.ClientErrorMode;

/**
 * @TODO Code to disable url calls with itsnat_doc_name=### type urls if possible
 *
 * @author Ravindranath Akila
 */
public class Controller extends HttpServletWrapper {

    private final static Map<Page, String> PrettyURLMap_ = new IdentityHashMap<Page, String>();//Please read javadoc before making any changes to this implementation

    public Controller() {
    }

    public static enum Page {

        main("ai/ilikeplaces/Main.xhtml", "Main_temp1", "Main_temp2", "Main_sidebar"),
        Photo$Description("ai/ilikeplaces/fragments/Photo-Description.xhtml","pd"),
        login("ai/ilikeplaces/security/login.xhtml"),
        include("ai/ilikeplaces/security/include.xhtml");

        private Page(final String path__, String... ids__) {
            PrettyURLMap_.put(this, path__);
            PutAllPageElementIds(ids__);
        }

        @Override
        public String toString() {
            String returnVal = null;
            switch (this) {
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

        //inhs__.registerItsNatDocumentTemplate("include", "text/html", pathPrefix__ + PrettyURLMap_.get("include"));

        inhs__.registerItsNatDocFragmentTemplate("include2", "text/html", pathPrefix__ + PrettyURLMap_.get(Page.include));
    }

    /**
     * We have a URL of type say www.ilikeplaces.com/page/Egypt
     * where we are supposed to recieve the /Egypt part.
     * @param request__
     */
    private static void pathResolver(final ItsNatServletRequest request__) {
        final String location__ = ((HttpServletRequest) request__.getServletRequest()).getPathInfo().substring(1);//Removes preceeding slash
        System.out.println("LOCATION1:" + location__);
        if (isCorrectLocationFormat(location__)) {
            System.out.println("LOCATION2:" + location__);
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
    public final static Map<String, String> GlobalHTMLIdRegistry_ = new IdentityHashMap<String, String>();

    /**
     * Register all your document keys before using. Acceps variable argument length.
     * Usage: putAllPageElementIds("id1","id2","id3");
     * @param ids_
     */
    public final static void PutAllPageElementIds(final String... ids_) {
        for (String id_ : ids_) {
            if (!GlobalHTMLIdRegistry_.containsKey(id_)) {
                /**
                 * Do not verify if element exists in document here as elements
                 * can be dynamically created
                 */
                GlobalHTMLIdRegistry_.put(id_, id_);
            } else {
                throw new java.lang.SecurityException("MAP ALREADY CONTAINS THIS \"" + id_ + "\" KEY!");
            }
        }
    }
}
