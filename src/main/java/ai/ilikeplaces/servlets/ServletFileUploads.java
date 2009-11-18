package ai.ilikeplaces.servlets;

import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.util.SessionBoundBadRefWrapper;
import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.logic.crud.DB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    final Logger logger = LoggerFactory.getLogger(ServletFileUploads.class.getName());
    
    final static private String Error = "error";
    final static private String Ok = "ok";

    final static private Random random = new Random(System.currentTimeMillis());

    private static final ResourceBundle config = PropertyResourceBundle.getBundle("ai.ilikeplaces.rbs.Config");
    private static final ResourceBundle logMsgs = PropertyResourceBundle.getBundle("ai.ilikeplaces.rbs.LogMsgs");
    private static final ResourceBundle exceptionMsgs = PropertyResourceBundle.getBundle("ai.ilikeplaces.rbs.ExceptionMsgs");

    final static private String FilePath = config.getString("path.SYSTEM_PHOTO");
    final static private String CDN = config.getString("url.CDN_PHOTO");
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request__
     * @param response__
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(final HttpServletRequest request__,
            final HttpServletResponse response__)
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
                } else if (session.getAttribute(ServletLogin.HumanUser) == null) {
                    errorNoLogin(out);
                    break processSignOn;
                }

                processRequestType:
                {
                    @SuppressWarnings("unchecked")
                    final HumanUserLocal sBLoggedOnUserLocal =
                            ((SessionBoundBadRefWrapper<HumanUserLocal>) session.getAttribute(ServletLogin.HumanUser)).boundInstance;
                    try {
                        /*Check that we have a file upload request*/
                        final boolean isMultipart = ServletFileUpload.isMultipartContent(request__);
                        if (!isMultipart) {
                            LoggerFactory.getLogger(ServletFileUploads.class.getName()).error(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0001"));
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
                                String photoName = null;
                                Boolean isPublic = null;
                                boolean fileSaved = false;

                                while (iter.hasNext()) {
                                    FileItemStream item = iter.next();
                                    String name = item.getFieldName();
                                    String absoluteFileSystemFileName = FilePath;

                                    InputStream stream = item.openStream();
                                    @FIXME(issue = "Handle no extension files")
                                    String usersFileName = null;
                                    String randomFileName = null;


                                    if (item.isFormField()) {
                                        final String value = Streams.asString(stream);
                                        logger.info(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0002"),name);
                                        logger.info(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0003"),value);
                                        if (name.equals("locationId")) {
                                            locationId = Long.parseLong(value);
                                            logger.info(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0004"));
                                        }

                                        if (name.equals("photoDescription")) {
                                            photoDescription = value;
                                            logger.info(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0005"));
                                        }

                                        if (name.equals("photoName")) {
                                            photoName = value;
                                            logger.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0006"));
                                        }

                                        if (name.equals("isPublic")) {
                                            if (!(value.equals("true") || value.equals("false"))) {
                                                throw new IllegalArgumentException(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0007") + value);
                                            }

                                            isPublic = Boolean.valueOf(value);
                                            logger.info(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0008"));

                                        }

                                    }
                                    if ((!item.isFormField())) {
                                        logger.info(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0009") + name);
                                        logger.info(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0010") + item.getName());
                                        // Process the input stream
                                        if (!(item.getName().lastIndexOf(".") > 0)) {
                                            errorFileType(out, item.getName());
                                            break processRequest;
                                        }

                                        usersFileName = (item.getName().indexOf("\\") <= 1 ? item.getName()
                                                : item.getName().substring(item.getName().lastIndexOf("\\") + 1));

                                        randomFileName = getRandomFileName(locationId);

                                        final File uploadedFile = new File(absoluteFileSystemFileName += randomFileName);
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

                                    logger.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0011") + locationId);
                                    logger.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0012") + fileSaved);
                                    logger.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0013") + photoDescription);
                                    logger.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0014") + photoName);
                                    logger.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0015") + isPublic);

                                    if (fileSaved && (locationId != null) && (photoDescription != null) && (isPublic != null)) {
                                        persistData:
                                        {
                                            handlePublicPrivateness:
                                            {
                                                if (isPublic) {
                                                    persisted = DB.getHumanCRUDPublicPhotoLocal(true).doHumanCPublicPhoto(sBLoggedOnUserLocal.getHumanUserId(), locationId, absoluteFileSystemFileName, photoName, photoDescription, new String(CDN + randomFileName), 4);
                                                    if (persisted) {
                                                        successFileName(out, usersFileName, logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0016"));
                                                    } else {
                                                        errorBusy(out);
                                                    }

                                                } else {
//                                                    final PrivatePhoto privatePhoto = new PrivatePhoto();
//                                                    privatePhoto.setPrivatePhotoFilePath(absoluteFileSystemFileName);
//                                                    privatePhoto.setPrivatePhotoDescription(photoDescription);
//                                                    privatePhoto.setPrivatePhotoURLPath(new String(CDN + randomFileName));
//                                                    privatePhoto.setLocation(locationn);
//
//                                                    final HumansPrivatePhoto humansPrivatePhoto = human.getHumansPrivatePhoto();
//                                                    privatePhoto.setHumansPrivatePhoto(humansPrivatePhoto);
//                                                    final List<PrivatePhoto> privatePhotoList = humansPrivatePhoto.getPrivatePhotos();
//
//                                                    privatePhotoList.size();
//                                                    privatePhotoList.add(privatePhoto);
//
//                                                    final int retryLimit = 4;
//                                                    for (int retries = 1, uploaded = 0; uploaded != 1 && retries <= retryLimit/* 15 seconds*/; retries++) {
//                                                        try {
//                                                            crudServiceHuman_.update(human);
//                                                            uploaded = 1;
//                                                            successFileName(out, usersFileName, "public");
//                                                            if (retries > 1) {
//                                                                logger.info("HELLO, I MANAGED TO PERSIST THE DATA AFTER " + retries + " RETRIES.");
//                                                            }
//                                                        } catch (javax.ejb.EJBTransactionRolledbackException e_) {
//                                                            logger.info("SORRY! I AM UNABLE TO PERSIST FILE UPLOAD DATA.", e_);
//                                                            if (retries == retryLimit) {/*ok this is the last retrey. Failed. lets report to the client*/
//                                                                errorBusy(out);
//                                                                /*@WARN: do not do this without closing streams!*/
//                                                                break fileUpload;
//                                                            } else {
//                                                                logger.info("HELLO, I AM RETRYING TO PERSIST THE DATA AFTER " + retries + " SECONDS THREAD SLEEP. I AM GOING TO SLEEP NOW.");
//                                                                try {
//                                                                    Thread.sleep(1000 * retries);
//                                                                } catch (InterruptedException ex) {
//                                                                    LoggerFactory.getLogger(ServletFileUploads.class.getName()).error(null, ex);
//                                                                }
//                                                            }
//                                                        }
//                                                    }
//                                                    persisted = true;
                                                    persisted = false;
                                                    throw new UnsupportedOperationException(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0017"));
                                                }
                                            }
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
                        logger.error(null, ex);
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
                formattedResponse +=
                        param_;
            }

            returnVal = "{" + formattedResponse.substring(formattedResponse.indexOf(",") + 1) + "}";
        } else {
            throw new IllegalArgumentException(exceptionMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0001"));
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
    private void successFileName(final PrintWriter out,
            final String fileName,
            final String exposurePublicOrPrivate) {
        try {
            flush(out).print(formatTuple(Ok, fileName, exposurePublicOrPrivate));
        } finally {
            out.close();
        }

    }

    @FIXME(issue = "Handle exception")
    private void errorFileType(final PrintWriter out,
            final String fileName) {
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

    final static private String getRandomFileName(final long locationId) {
        return "PHOTO_OF_" + DB.getHumanCRUDLocationLocal(true).doDirtyHumanRLocation(locationId).getLocationName() + "_" + random.nextLong() + System.currentTimeMillis();
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
    protected void doGet(final HttpServletRequest request,
            final HttpServletResponse response)
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
    protected void doPost(final HttpServletRequest request,
            final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0018");
    }// </editor-fold>
}
