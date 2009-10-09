package ai.ilikeplaces.servlets;

import ai.ilikeplaces.SBLoggedOnUserFace;
import ai.ilikeplaces.SessionBoundBadReferenceWrapper;
import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansPrivatePhoto;
import ai.ilikeplaces.entities.HumansPublicPhoto;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.exception.ExceptionConstructorInvokation;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
    final static private String FilePath = "C:\\Program Files\\Apache Software Foundation\\apache-tomcat-6.0.14\\webapps\\cdn\\";
    final static private String CDN = "/cdn/";
    final static private String Error = "error";
    final static private String Ok = "ok";

    /*Container Related Services*/
    final private Properties p_ = new Properties();
    private Context context = null;
    private CrudServiceLocal<Human> crudServiceHuman_ = null;
    private CrudServiceLocal<Location> crudServiceLocation_ = null;

    @Override
    @SuppressWarnings("unchecked")
    @FIXME(issue = "Concurrent calls seem to create concurrency issues on persistance initialization. Verify. Entities all done't initialise\n" +
    "The bean encountered a non-application exception; nested exception is:" +
    "<openjpa-1.2.0-r422266:683325 nonfatal user error> org.apache.openjpa.persistence.ArgumentException: An error occurred while parsing the query filter SELECT loc FROM Location loc WHERE loc.locationName = " +
    ":locationName. Error message: The name Location is not a recognized entity or identifier. Perhaps you meant HumansAuthorization, which is a close match. Known entity names: [HumansPublicPhoto, Human, HumansAuthorization]")
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

                crudServiceHuman_ = (CrudServiceLocal<Human>) context.lookup("CrudServiceLocal");
                if (crudServiceHuman_ == null) {
                    log.append("\nVARIABLE crudServiceLocal_ IS NULL! ");
                    log.append(crudServiceHuman_);
                    break init;
                }
                crudServiceLocation_ = (CrudServiceLocal<Location>) context.lookup("CrudServiceLocal");
                if (crudServiceLocation_ == null) {
                    log.append("\nVARIABLE crudServiceLocation_ IS NULL! ");
                    log.append(crudServiceLocation_);
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
            processSignOn:
            {
                final HttpSession session = request__.getSession(false);

                if (session == null) {
                    errorNoLogin(out);
                    break fileUpload;
                } else if (session.getAttribute(ServletLogin.SBLoggedOnUser) == null) {
                    errorNoLogin(out);
                    break processSignOn;
                }

                processRequestType:
                {
                    @SuppressWarnings("unchecked")
                    final SBLoggedOnUserFace sBLoggedOnUserFace =
                            ((SessionBoundBadReferenceWrapper<SBLoggedOnUserFace>) session.getAttribute(ServletLogin.SBLoggedOnUser)).boundInstance;
                    try {
                        /*Check that we have a file upload request*/
                        final boolean isMultipart = ServletFileUpload.isMultipartContent(request__);
                        if (!isMultipart) {
                            Logger.getLogger(ServletFileUploads.class.getName()).log(Level.SEVERE, "IS NOT A FILE UPLOAD REQUEST");
                            errorNonMultipart(out);
                            break processRequestType;
                        }

                        processRequest:
                        {

                            // Create a new file upload handler
                            final ServletFileUpload upload = new ServletFileUpload();
                            // Parse the request
                            FileItemIterator iter = upload.getItemIterator(request__);
                            boolean persisted = false;

                            loop:
                            {
                                Long locationId = null;
                                String photoDescription = null;
                                Boolean isPublic = null;
                                boolean fileSaved = false;

                                while (iter.hasNext()) {
                                    FileItemStream item = iter.next();
                                    String name = item.getFieldName();
                                    String fileName = FilePath;

                                    InputStream stream = item.openStream();
                                    @FIXME(issue = "Handle no extension files")
                                    String usersFileName = null;
                                    String randomFileName = null;


                                    if (item.isFormField()) {
                                        final String value = Streams.asString(stream);
                                        logger.info("HELLO, I DETECTED A FORM FIELD WITH NAME " + name + " AND WITH VALUE " + value + ".");
                                        if (name.equals("locationId")) {
                                            locationId = Long.parseLong(value);
                                            logger.info("HELLO, I PROPERLY RECIEVED locationId");
                                        }
                                        if (name.equals("photoDescription")) {
                                            photoDescription = value;
                                            logger.info("HELLO, I PROPERLY RECIEVED photoDescription");
                                        }
                                        if (name.equals("isPublic")) {
                                            if (!(value.equals("true") || value.equals("false"))) {
                                                throw new IllegalArgumentException("SORRY! I REQUIRE A BOOLEAN AND YOU GIVE ME:" + value);
                                            }
                                            isPublic = Boolean.valueOf(value);
                                            logger.info("HELLO, I PROPERLY RECIEVED isPublic");

                                        }
                                    }
                                    if ((!item.isFormField())) {
                                        logger.info("HELLO, I DETECTED A FILE FIELD WITH NAME " + name + " AND WITH FILE NAME " + item.getName() + ".");
                                        // Process the input stream
                                        if (!(item.getName().lastIndexOf(".") > 0)) {
                                            errorFileType(out, item.getName());
                                            break processRequest;
                                        }
                                        usersFileName = (item.getName().indexOf("\\") <= 1 ? item.getName()
                                                : item.getName().substring(item.getName().lastIndexOf("\\") + 1));

//                        final String fileName = FilePath + FileRandom.getName() + "." + item.getName().substring(item.getName().lastIndexOf(".") + 1);
                                        randomFileName = String.valueOf(System.currentTimeMillis()) + "." + item.getName().substring(item.getName().lastIndexOf(".") + 1);
                                        fileName += randomFileName;
                                        final File uploadedFile = new File(fileName);
                                        final FileOutputStream fos = new FileOutputStream(uploadedFile);
                                        while (true) {
                                            final int dataByte = stream.read();
                                            if (dataByte != -1) {
                                                fos.write(dataByte);
                                            } else {
                                                break;
                                            }
                                        }
                                        fos.close();
                                        stream.close();
                                        fileSaved = true;
                                    }


                                    System.out.println("locationId" + locationId);
                                    System.out.println("fileSaved" + fileSaved);
                                    System.out.println("photoDescription" + photoDescription);
                                    System.out.println("isPublic" + isPublic);
                                    if (fileSaved && (locationId != null) && (photoDescription != null) && (isPublic != null)) {
                                        persistData:
                                        {
                                            final Human human = crudServiceHuman_.find(Human.class, sBLoggedOnUserFace.getLoggedOnUserId());
                                            logger.info("HELLO, I AM DOING A FILE UPLOAD PERSISTENCE ON HUMAN id:" + human.getHumanId());

                                            Location locationn = crudServiceLocation_.find(Location.class, locationId);
                                            logger.info("HELLO, I AM DOING A FILE UPLOAD PERSISTENCE ON LOCATION NAME:" + locationn.getLocationName());


                                            handlePublicPrivateness:
                                            {
                                                if (isPublic) {
                                                    final PublicPhoto publicPhoto = new PublicPhoto();
                                                    publicPhoto.setPublicPhotoFilePath(fileName);
                                                    locationn.getPublicPhotos().add(publicPhoto);
                                                    publicPhoto.setLocation(locationn);
                                                    publicPhoto.setPublicPhotoDescription(photoDescription);
                                                    publicPhoto.setPublicPhotoURLPath(new String(CDN + randomFileName));

                                                    final HumansPublicPhoto humansPublicPhoto = human.getHumansPublicPhoto();
                                                    publicPhoto.setHumansPublicPhoto(humansPublicPhoto);
                                                    final List<PublicPhoto> publicPhotoList = humansPublicPhoto.getPublicPhotos();

                                                    publicPhotoList.size();
                                                    publicPhotoList.add(publicPhoto);
                                                } else {
                                                    final PrivatePhoto privatePhoto = new PrivatePhoto();
                                                    privatePhoto.setPrivatePhotoFilePath(fileName);
                                                    privatePhoto.setLocation(locationn);
                                                    privatePhoto.setPrivatePhotoDescription(photoDescription);
                                                    privatePhoto.setPrivatePhotoURLPath(new String(CDN + randomFileName));

                                                    final HumansPrivatePhoto humansPrivatePhoto = human.getHumansPrivatePhoto();
                                                    privatePhoto.setHumansPrivatePhoto(humansPrivatePhoto);
                                                    final List<PrivatePhoto> privatePhotoList = humansPrivatePhoto.getPrivatePhotos();

                                                    privatePhotoList.size();
                                                    privatePhotoList.add(privatePhoto);
                                                }
                                            }
                                            final int retryLimit = 4;
                                            for (int retries = 1, uploaded = 0; uploaded != 1 && retries <= retryLimit/* 15 seconds*/; retries++) {
                                                try {
                                                    crudServiceHuman_.update(human);
                                                    uploaded = 1;
                                                    successFileName(out, usersFileName, "public");
                                                    if (retries > 1) {
                                                        logger.info("HELLO, I MANAGED TO PERSIST THE DATA AFTER " + retries + " RETRIES.");
                                                    }
                                                } catch (javax.ejb.EJBTransactionRolledbackException e_) {
                                                    logger.log(Level.SEVERE, "SORRY! I AM UNABLE TO PERSIST FILE UPLOAD DATA.", e_);
                                                    if (retries == retryLimit) {/*ok this is the last retrey. Failed. lets report to the client*/
                                                        errorBusy(out);
                                                        /*@WARN: do not do this without closing streams!*/
                                                        break fileUpload;
                                                    } else {
                                                        logger.info("HELLO, I AM RETRYING TO PERSIST THE DATA AFTER " + retries + " SECONDS THREAD SLEEP. I AM GOING TO SLEEP NOW.");
                                                        try {
                                                            Thread.sleep(1000 * retries);
                                                        } catch (InterruptedException ex) {
                                                            Logger.getLogger(ServletFileUploads.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                    }
                                                }
                                            }
                                            persisted = true;
                                        }
                                        /*We got what we need from the loop. Lets break it*/
                                        break loop;
                                    }
                                }

                            }
                            if (!persisted) {
                                errorMissingParameters(out);
                                break processRequest;
                            }
                        }

                    } catch (FileUploadException ex) {
                        Logger.getLogger(ServletFileUploads.class.getName()).log(Level.SEVERE, null, ex);
                        errorBusy(out);
                    }
                }

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

    private PrintWriter flush(final PrintWriter out) {
        out.flush();
        return out;
    }

    @FIXME(issue = "Handle exception")
    private void errorNoLogin(final PrintWriter out) {
        try {
            flush(out).print(formatTuple(Error, "no_login"));
        } finally {
            out.close();
        }

    }

    @FIXME(issue = "Handle exception")
    private void errorBusy(final PrintWriter out) {
        try {
            flush(out).print(formatTuple(Error, "server_busy"));
        } finally {
            out.close();
        }

    }

    @FIXME(issue = "Handle exception")
    private void errorNonMultipart(final PrintWriter out) {
        try {
            flush(out).print(formatTuple(Error, "non_multipart_request"));
        } finally {
            out.close();
        }

    }

    @TODO(task = "Make a key,value implementation to avoid careless indexing errors on client")
    @FIXME(issue = "Handle exception")
    private void successFileName(final PrintWriter out, final String fileName, final String exposurePublicOrPrivate) {
        try {
            flush(out).print(formatTuple(Ok, fileName, exposurePublicOrPrivate));
        } finally {
            out.close();
        }
    }

    @FIXME(issue = "Handle exception")
    private void errorFileType(final PrintWriter out, final String fileName) {
        try {
            flush(out).print(formatTuple(Error, "wrong_file_type", fileName));
        } finally {
            out.close();
        }
    }

    @FIXME(issue = "Handle exception")
    private void errorMissingParameters(final PrintWriter out) {
        try {
            flush(out).print(formatTuple(Error, "missing_parameters"));
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
