package ai.ilikeplaces.servlets;

import ai.ilikeplaces.util.Loggers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The GreatWallOfExceptions will be the global wrapper for all exceptions of www.ilikeplaces.com.
 * <p/>
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 1/1/13
 * Time: 2:59 PM
 */
abstract public class GreatWallOfExceptions extends HttpServlet {
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (final ServletException e) {
            Loggers.error("Error forwarding request to error page", e);//And we leave the user hanging, or we'd end up putting him and a redirect loop
        } catch (final IOException e) {
            Loggers.error("Error forwarding request to error page", e);//And we leave the user hanging, or we'd end up putting him and a redirect loop
        }
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (final ServletException e) {
            Loggers.error("Error forwarding request to error page", e);//And we leave the user hanging, or we'd end up putting him and a redirect loop
        } catch (final IOException e) {
            Loggers.error("Error forwarding request to error page", e);//And we leave the user hanging, or we'd end up putting him and a redirect loop
        }
    }

    abstract void processRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException;


}
