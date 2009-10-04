package ai.ilikeplaces.servlets;

import ai.ilikeplaces.SBLoggedOnUserFace;
import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansPublicPhoto;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.exception.ExceptionConstructorInvokation;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.security.FileRandom;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 *
 * @author Ravindranath Akila
 */
final public class ServletFileUploads extends HttpServlet {

    final Logger logger = Logger.getLogger(ServletFileUploads.class.getName());
    final static private String FilePath = "c:\\images\\";
    final static private String Error = "error";
    final static private String Ok = "ok";

    /*Container Related Services*/
    final private Properties p_ = new Properties();
    private Context context = null;
    private CrudServiceLocal<Human> crudServiceHuman_ = null;

    @Override
    @SuppressWarnings("unchecked")
    public void init() {
        boolean initializeFailed = true;
        final StringBuilder log = new StringBuilder();
        init:
        {
            try {
                p_.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
                context = new InitialContext(p_);
                if (context == null) {
                    log.append("\nVARIABLE context IS NULL! ");
                    log.append(context);
                    break init;
                }

                crudServiceHuman_ = (CrudServiceLocal) context.lookup("CrudServiceLocal");
                if (crudServiceHuman_ == null) {
                    log.append("\nVARIABLE crudServiceLocal_ IS NULL! ");
                    log.append(crudServiceHuman_);
                    break init;
                }

            } catch (NamingException ex) {
                log.append("\nCOULD NOT INITIALIZE ServletFileUploads SERVLET DUE TO A NAMING EXCEPTION!");
                logger.log(Level.SEVERE, "\nCOULD NOT INITIALIZE ServletFileUploads SERVLET DUE TO A NAMING EXCEPTION!", ex);
                break init;
            }

            /**
             * break. Do not let this statement be reachable if initialization
             * failed. Instead, break immediately where initialization failed.
             * At this point, we set the initializeFailed to false and thereby,
             * allow initialization of an instance
             */
            initializeFailed = false;
        }
        if (initializeFailed) {
            throw new ExceptionConstructorInvokation(log.toString());
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request__
     * @param response__
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(final HttpServletRequest request__, final HttpServletResponse response__)
            throws ServletException, IOException {
        response__.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response__.getWriter();

        fileUpload:
        {
            final HttpSession session = request__.getSession(false);

            if (session == null) {
                errorNoLogin(out);
                break fileUpload;
            } else if (session.getAttribute(ServletLogin.SBLoggedOnUser) == null) {
                errorNoLogin(out);
                break fileUpload;
            }

            final SBLoggedOnUserFace sBLoggedOnUserFace = (SBLoggedOnUserFace) session.getAttribute(ServletLogin.SBLoggedOnUser);
            try {
                /*Check that we have a file upload request*/
                final boolean isMultipart = ServletFileUpload.isMultipartContent(request__);
                if (!isMultipart) {
                    Logger.getLogger(ServletFileUploads.class.getName()).log(Level.SEVERE, "IS NOT A FILE UPLOAD REQUEST");
                    try {
                        out.println("{error,SORRY! I JUST IGNORED A BAD(NON MULTIPART) REQUEST FROM YOU!}");
                    } finally {
                        out.close();
                    }
                    break fileUpload;
                }
                // Create a new file upload handler
                final ServletFileUpload upload = new ServletFileUpload();
                // Parse the request
                FileItemIterator iter = upload.getItemIterator(request__);
                while (iter.hasNext()) {
                    FileItemStream item = iter.next();
                    String name = item.getFieldName();
                    InputStream stream = item.openStream();
                    if (item.isFormField()) {
                        System.out.println("Form field " + name + " with value " + Streams.asString(stream) + " detected.");
                    } else {
                        System.out.println("File field " + name + " with file name " + item.getName() + " detected.");
                        // Process the input stream
                        if (!(item.getName().lastIndexOf(".") > 0)) {
                            errorFileType(out, item.getName());
                            break fileUpload;
                        }

                        /*Handle no extension files*/
                        final String usersFileName = (item.getName().indexOf("\\") <= 1 ? item.getName()
                                : item.getName().substring(item.getName().lastIndexOf("\\") + 1));

                        final String fileName = FilePath + FileRandom.getName() + "." + item.getName().substring(item.getName().lastIndexOf(".") + 1);
                        final File uploadedFile = new File(fileName);
                        final FileOutputStream fos = new FileOutputStream(uploadedFile);
                        while (stream.available() > 0) {
                            fos.write(stream.read());
                        }

                        final Human human = crudServiceHuman_.find(Human.class, sBLoggedOnUserFace.getLoggedOnUserId());
                        final PublicPhoto publicPhoto = new PublicPhoto();
                        publicPhoto.setPublicPhotoFilePath(fileName);

                        //human.getHumansPublicPhoto().setPublicPhotos(publicPhotoList);
                        final HumansPublicPhoto humansPublicPhoto = human.getHumansPublicPhoto();
                        final List<PublicPhoto> publicPhotoList = humansPublicPhoto.getPublicPhotos();

                        publicPhotoList.add(publicPhoto);
                        crudServiceHuman_.update(human);
                        successFileName(out, usersFileName, "public");

                        fos.close();
                        stream.close();
                    }
                }

                String headerVals = "";
                Enumeration n = request__.getHeaderNames();
                while (n.hasMoreElements()) {
                    headerVals += request__.getHeader((String) n.nextElement());
                    headerVals += "<br/>";
                }
            } catch (FileUploadException ex) {
                Logger.getLogger(ServletFileUploads.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String formatTuple(final String... params__) {
        String returnVal = "";
        if (params__.length > 0) {
            String formattedResponse = "";
            for (String param_ : params__) {
                formattedResponse += ",";
                formattedResponse += param_;
            }
            returnVal = "{" + formattedResponse.substring(formattedResponse.indexOf(",") + 1) + "}";
        } else {
            throw new IllegalArgumentException("SORRY! YOU HAVE GIVEN AN EMPTY LIST OF PARAMS FOR ME TO PROCEESS WHICH IS INSANE.");
        }
        return returnVal;

    }

    @FIXME(issues = "Handle exception")
    private void errorNoLogin(final PrintWriter out) {
        try {
            out.print(formatTuple(Error, "no_login"));
        } finally {
            out.close();
        }

    }

    @TODO(tasks = "Make a key,value implementation to avoid careless indexing errors on client")
    @FIXME(issues = "Handle exception")
    private void successFileName(final PrintWriter out, final String fileName, final String exposurePublicOrPrivate) {
        try {
            out.print(formatTuple(Ok, fileName, exposurePublicOrPrivate));
        } finally {
            out.close();
        }
    }

    @FIXME(issues = "Handle exception")
    private void errorFileType(final PrintWriter out, final String fileName) {
        try {
            out.print(formatTuple(Error, "wrong_file_type", fileName));
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
