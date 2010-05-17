package ai.ilikeplaces.servlets;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.SessionBoundBadRefWrapper;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.PropertyResourceBundle;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class ServletFileUploads extends HttpServlet {

    final static private String Error = "error";
    final static private String Ok = "ok";

    final static private Random random = new Random(System.currentTimeMillis());

    private static final ResourceBundle config = PropertyResourceBundle.getBundle("ai.ilikeplaces.rbs.Config");
    private static final ResourceBundle logMsgs = PropertyResourceBundle.getBundle("ai.ilikeplaces.rbs.LogMsgs");
    private static final ResourceBundle exceptionMsgs = PropertyResourceBundle.getBundle("ai.ilikeplaces.rbs.ExceptionMsgs");

    final static private String FilePath = config.getString("path.SYSTEM_PHOTO");
    final static private String CDN = config.getString("url.CDN_PHOTO");
    private static final UnsupportedOperationException UNSUPPORTED_OPERATION_EXCEPTION = new UnsupportedOperationException("SORRY! THIS STATE OF OPERATION IS NOT YET SUPPORTED!");

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request__
     * @param response__
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(final HttpServletRequest request__,
                                  final HttpServletResponse response__)
            throws ServletException, IOException {
        response__.setContentType("text/html;charset=UTF-8");
        Loggers.DEBUG.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0020"), request__.getLocale());
        PrintWriter out = response__.getWriter();

        final ResourceBundle gUI = PropertyResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");

        try {
            fileUpload:
            {
                if (!isFileUploadPermitted()) {
                    errorTemporarilyDisabled(out);
                    break fileUpload;
                }
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
                                    Boolean isPrivate = null;
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
                                            Loggers.DEBUG.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0002"), name);
                                            Loggers.DEBUG.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0003"), value);
                                            if (name.equals("locationId")) {
                                                locationId = Long.parseLong(value);
                                                Loggers.DEBUG.debug((logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0004")));
                                            }

                                            if (name.equals("photoDescription")) {
                                                photoDescription = value;
                                                Loggers.DEBUG.debug((logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0005")));
                                            }

                                            if (name.equals("photoName")) {
                                                photoName = value;
                                                Loggers.DEBUG.debug((logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0006")));
                                            }

                                            if (name.equals("isPublic")) {
                                                if (!(value.equals("true") || value.equals("false"))) {
                                                    throw new IllegalArgumentException(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0007") + value);
                                                }

                                                isPublic = Boolean.valueOf(value);
                                                Loggers.DEBUG.debug((logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0008")));

                                            }
                                            if (name.equals("isPrivate")) {
                                                if (!(value.equals("true") || value.equals("false"))) {
                                                    throw new IllegalArgumentException(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0007") + value);
                                                }

                                                isPrivate = Boolean.valueOf(value);
                                                Loggers.DEBUG.debug("HELLO, I PROPERLY RECEIVED photoName.");

                                            }

                                        }
                                        if ((!item.isFormField())) {
                                            Loggers.DEBUG.debug((logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0009") + name));
                                            Loggers.DEBUG.debug((logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0010") + item.getName()));
                                            // Process the input stream
                                            if (!(item.getName().lastIndexOf(".") > 0)) {
                                                errorFileType(out, item.getName());
                                                break processRequest;
                                            }

                                            usersFileName = (item.getName().indexOf("\\") <= 1 ? item.getName()
                                                    : item.getName().substring(item.getName().lastIndexOf("\\") + 1));

                                            final String userUploadedFileName = item.getName();

                                            String fileExtension = "error";

                                            if (userUploadedFileName.toLowerCase().endsWith(".jpg")) {
                                                fileExtension = ".jpg";
                                            } else if (userUploadedFileName.toLowerCase().endsWith(".jpeg")) {
                                                fileExtension = ".jpeg";
                                            } else if (userUploadedFileName.toLowerCase().endsWith(".png")) {
                                                fileExtension = ".png";
                                            } else {
                                                errorFileType(out, gUI.getString("ai.ilikeplaces.servlets.ServletFileUploads.0019"));
                                                break processRequest;
                                            }

                                            randomFileName = getRandomFileName(locationId);

                                            randomFileName += fileExtension;

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

                                        Loggers.DEBUG.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0011") + locationId);
                                        Loggers.DEBUG.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0012") + fileSaved);
                                        Loggers.DEBUG.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0013") + photoDescription);
                                        Loggers.DEBUG.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0014") + photoName);
                                        Loggers.DEBUG.debug(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0015") + isPublic);

                                        if (fileSaved && (photoDescription != null)) {
                                            persistData:
                                            {
                                                handlePublicPrivateness:
                                                {
                                                    if ((isPublic != null) && isPublic && (locationId != null)) {
                                                        Return<PublicPhoto> r = DB.getHumanCRUDPublicPhotoLocal(true).cPublicPhoto(sBLoggedOnUserLocal.getHumanUserId(), locationId, absoluteFileSystemFileName, photoName, photoDescription, new String(CDN + randomFileName), 4);
                                                        if (r.returnStatus() == 0) {
                                                            successFileName(out, usersFileName, logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0016"));
                                                        } else {
                                                            errorBusy(out);
                                                        }

                                                    } else if ((isPrivate != null) && isPrivate) {
                                                        Return<PrivatePhoto> r = DB.getHumanCRUDPrivatePhotoLocal(true).cPrivatePhoto(sBLoggedOnUserLocal.getHumanUserId(), absoluteFileSystemFileName, photoName, photoDescription, new String(CDN + randomFileName));
                                                        if (r.returnStatus() == 0) {
                                                            successFileName(out, usersFileName, "private");
                                                        } else {
                                                            errorBusy(out);
                                                        }
                                                    } else {
                                                        throw UNSUPPORTED_OPERATION_EXCEPTION;
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
                            Loggers.EXCEPTION.error(null, ex);
                            errorBusy(out);
                        }
                    }

                }

            }
        } catch (final Throwable t_) {
            Loggers.EXCEPTION.error("SORRY! I ENCOUNTERED AN EXCEPTION DURING THE FILE UPLOAD", t_);
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
    private void errorTemporarilyDisabled(final PrintWriter out) {
        try {
            flush(out).print(formatTuple(Error, "file_uploading_disabled"));
        } finally {
            out.close();
        }

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

    @WARNING(warning = "DO NOT USE _(UNDERSCORE) AS URL SPLITTING WORKS USING UNDERSCORE. SEE Controller FOR FURTHER INFO")
    final static private String getRandomFileName(final long locationId) {
        return "photo-of-" + DB.getHumanCRUDLocationLocal(true).dirtyRLocation(locationId).returnValue().getLocationName() + "-" + random.nextLong() + System.currentTimeMillis();
    }


    private boolean isFileUploadPermitted() {
        return (RBGet.getGlobalConfigKey("fileUploadEnabled") != null
                && RBGet.getGlobalConfigKey("fileUploadEnabled").equals("true"));
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0018");
    }// </editor-fold>

}
