package ai.ilikeplaces.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Ravindranath Akila
 */
public class FileUploads extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws FileUploadException
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        /*Check that we have a file upload request*/
        final boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            Logger.getLogger(FileUploads.class.getName()).log(Level.SEVERE, "IS NOT A FILE UPLOAD REQUEST");
        }

        /* Create a factory for disk-based file items */
        final FileItemFactory factory = new DiskFileItemFactory();

        /* Create a new file upload handler */
        final ServletFileUpload upload = new ServletFileUpload(factory);

        /* Parse the request*/
        List /* FileItem */ items = new ArrayList();
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            Logger.getLogger(FileUploads.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String filePath = "c:\\tezt.img";

        for (Object object : items) {
            final FileItem item = (FileItem) object;
            if (item.isFormField()) {
                    Logger.getLogger(FileUploads.class.getName()).log(Level.SEVERE,"FORM FIELD");
            } else {
                File uploadedFile = new File(filePath);
                try {
                    item.write(uploadedFile);
                } catch (Exception ex) {
                    Logger.getLogger(FileUploads.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }





        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FileUploads</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Done uploading file to" + filePath + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
