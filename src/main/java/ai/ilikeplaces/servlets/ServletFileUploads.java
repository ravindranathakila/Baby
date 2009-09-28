package ai.ilikeplaces.servlets;

import ai.ilikeplaces.servlets.Controller.Page;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                        final File uploadedFile = new File("c:\\stream.img");
                        final FileOutputStream fos = new FileOutputStream(uploadedFile);
                        while(stream.available()>0){
                            fos.write(stream.read());
                        }
                        fos.close();
                        stream.close();
                    }
                }
//            /* Create a factory for disk-based file items */
//            final FileItemFactory factory = new DiskFileItemFactory();
//
//            /* Create a new file upload handler */
//            final ServletFileUpload upload = new ServletFileUpload(factory);
//
//            /* Parse the request*/
//            List /* FileItem */ items = new ArrayList();
//            try {
//                items = upload.parseRequest(request__);
//            } catch (FileUploadException ex) {
//                Logger.getLogger(ServletFileUploads.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            final String filePath = "c:\\tezt.img";
//
//            for (Object object : items) {
//                final FileItem item = (FileItem) object;
//                if (item.isFormField()) {
//                    Logger.getLogger(ServletFileUploads.class.getName()).log(Level.SEVERE, "FORM FIELD");
//                } else {
//                    File uploadedFile = new File(filePath);
//                    try {
//                        item.write(uploadedFile);
//                    } catch (Exception ex) {
//                        Logger.getLogger(ServletFileUploads.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//
//            }
                String headerVals = "";
                Enumeration n = request__.getHeaderNames();
                while (n.hasMoreElements()) {
                    headerVals += request__.getHeader((String) n.nextElement());
                    headerVals += "<br/>";
                }
                try {
                    out.println("{ok,Filename}");
                } finally {
                    out.close();
                }
            } catch (FileUploadException ex) {
                Logger.getLogger(ServletFileUploads.class.getName()).log(Level.SEVERE, null, ex);
            }
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
