package ai.ilikeplaces.servlets;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.logic.cdn.CDNAlbum;
import ai.ilikeplaces.logic.cdn.CDNProfilePhoto;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.*;
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
import java.util.*;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class GenericFileGrabber extends HttpServlet {

    final static private String Error = "error";
    final static private String Ok = "ok";

    private static final ResourceBundle logMsgs = PropertyResourceBundle.getBundle("ai.ilikeplaces.rbs.LogMsgs");
    private static final ResourceBundle exceptionMsgs = PropertyResourceBundle.getBundle("ai.ilikeplaces.rbs.ExceptionMsgs");

    static String fileCache = RBGet.globalConfig.getString("FILE_CACHE");
    private static final String HYPHEN = "-";

    static {
        fileCache = fileCache.endsWith("/") || fileCache.endsWith("\\") ? fileCache : fileCache + "/";
    }


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

        Loggers.DEBUG.debug("Hello! Request on " + getClass().getName());

        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.USER, Loggers.CODE_GFG, 60000, null, true);

        response__.setContentType("text/html;charset=UTF-8");

        final PrintWriter out = response__.getWriter();

        try {
            fileUpload:
            {
                if (!isFileUploadPermitted()) {
                    errorTemporarilyDisabled(out);
                    break fileUpload;
                }
                stateSignedOn:
                {
                    final HttpSession session = request__.getSession(false);

                    breakIfNoLogin:
                    {
                        if (session == null) {
                            sl.appendToLogMSG("No Login as in no session.");
                            sl.complete(Loggers.FAILED);
                            errorNoLogin(out);
                            break stateSignedOn;
                        } else if (session.getAttribute(ServletLogin.HumanUser) == null) {
                            sl.appendToLogMSG("No Login as in no HumanUser attribute");
                            sl.complete(Loggers.FAILED);
                            errorNoLogin(out);
                            break stateSignedOn;
                        }
                    }
                    processRequestType:
                    {
                        /*Check that we have a file upload request*/
                        final boolean isMultipart = ServletFileUpload.isMultipartContent(request__);
                        if (!isMultipart) {
                            LoggerFactory.getLogger(ServletFileUploads.class.getName()).error(logMsgs.getString("ai.ilikeplaces.servlets.ServletFileUploads.0001"));
                            sl.appendToLogMSG("Not multipart request");
                            sl.complete(Loggers.FAILED);
                            errorNonMultipart(out);
                            break processRequestType;
                        }

                        @SuppressWarnings("unchecked")
                        final HumanUserLocal sBLoggedOnUserLocal = ((SessionBoundBadRefWrapper<HumanUserLocal>) session.getAttribute(ServletLogin.HumanUser)).boundInstance;

                        try {
                            processRequest:
                            {

                                // Create a new file upload handler
                                final ServletFileUpload upload = new ServletFileUpload();
                                // Parse the request
                                FileItemIterator iter = upload.getItemIterator(request__);
                                boolean persisted = false;

                                Return<File> r = processFileUploadRequest(iter, session);
                                persisted = r.returnStatus() == 0;
                                if (!persisted) {
                                    sl.appendToLogMSG(r.returnMsg());
                                    sl.complete(Loggers.FAILED);
                                    errorReorderedSomethingWentWrong(out, r.returnMsg());
                                    break processRequest;
                                } else {
                                    sl.appendToLogMSG(r.returnMsg());
                                    sl.complete(Loggers.DONE);
                                    successFileName(out, r.returnMsg(), r.returnMsg());
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
    private void errorSomethingWentWrong(final PrintWriter out) {
        try {
            flush(out).print(formatTuple(Error, "something_went_wrong"));
        } finally {
            out.close();
        }

    }


    @FIXME(issues = {"Handle exception","Tuple ordering changed with respect to profile photo upload."})
    private void errorReorderedSomethingWentWrong(final PrintWriter out, final String msg) {
        try {
            flush(out).print(formatTuple(Error, msg, "something_went_wrong"));
        } finally {
            out.close();
        }

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
    private void errorMissingParameters(final PrintWriter out, final String msg) {
        try {
            flush(out).print(formatTuple(Error, "missing_parameters", msg));
        } finally {
            out.close();
        }

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
    }


    private Return<File> processFileUploadRequest(final FileItemIterator iter, final HttpSession session) throws IOException, FileUploadException {
        String returnVal = "Sorry! No Items To Process";
        final Map<String, String> parameterMap = new HashMap<String, String>();
        final File tempFile = getTempFile();
        String userFileExtension = null;

        while (iter.hasNext()) {
            final FileItemStream item = iter.next();
            final String paramName = item.getFieldName();
            final InputStream stream = item.openStream();

            if (item.isFormField()) {//Parameter-Value
                final String paramValue = Streams.asString(stream);
                parameterMap.put(paramName, paramValue);
            }
            if (!item.isFormField()) {
                final String usersFileName = item.getName();
                final int extensionDotIndex = usersFileName.lastIndexOf(".");
                userFileExtension = usersFileName.substring(extensionDotIndex + 1);
                final FileOutputStream fos = new FileOutputStream(tempFile);
                final int uploadLimit = 1024 * 1024;//1MB
                int byteCount = 0;
                while (true) {
                    final int dataByte = stream.read();
                    if (byteCount++ > uploadLimit) {
                        fos.close();
                        tempFile.delete();
                        return new ReturnImpl<File>(ExceptionCache.FILE_SIZE_EXCEPTION, "File Too Big!", true);
                    }
                    if (dataByte != -1) {
                        fos.write(dataByte);
                    } else {
                        break;//break loop
                    }
                }
                fos.close();
            }
        }

        final FileUploadListenerFace<File> fulf;

        /**
         * Implement this as a set of listeners. Why it wasn't done now is that, a new object of listener should be
         * created per request and added to the listener pool(list or array whatever).
         */
        switch (Integer.parseInt(parameterMap.get("type"))) {
            case 1:
                fulf = CDNProfilePhoto.getProfilePhotoCDNLocal();
                break;
            case 2:
                fulf = CDNAlbum.getAlbumPhotoCDNLocal();
                break;
            default:
                return new ReturnImpl<File>(ExceptionCache.UNSUPPORTED_SWITCH, "Unsupported Case", true);
        }
        if (tempFile == null) {
            return new ReturnImpl<File>(ExceptionCache.UNSUPPORTED_OPERATION_EXCEPTION, "No File!", true);
        }

        return fulf.run(tempFile, parameterMap, userFileExtension, session);
    }

    /**
     * Note that the hyphen is used to to the url breaking standard withing the app with 'undescores' which should not
     * be used here
     *
     * @return
     */
    private File getTempFile() {
        return new File(fileCache + System.currentTimeMillis() + HYPHEN + UUID.randomUUID().toString());
    }
}
