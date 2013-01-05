package ai.ilikeplaces.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 1/1/13
 * Time: 8:32 PM
 */
public class Status404 extends GreatWallOfExceptions {

    @Override
    void processRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/404.jsp").forward(request, response);
    }
}
