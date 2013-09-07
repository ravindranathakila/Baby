package ai.baby.servlets;

import ai.hbase.HBaseCrudService;
import ai.hbase.RowKey;
import ai.ilikeplaces.entities.GeohashSubscriber;
import ai.baby.util.Loggers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 13/4/13
 * Time: 12:47 PM
 */
@WebServlet(urlPatterns = "/unsubscribe/*", loadOnStartup = 1)
public class Unsubscribe extends HttpServlet {


    public static final String TYPE = "Type";

    public static final String VALUE = "Value";

    public enum Type {
        GeohashSubscribe
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(final HttpServletRequest __req, final HttpServletResponse __resp) throws IOException {

        final String _typeParameter = __req.getParameter(TYPE);
        try {

            switch (Type.valueOf(_typeParameter)) {
                case GeohashSubscribe:
                    final String _valueParameter = __req.getParameter(VALUE);

                    final GeohashSubscriber _geohashSubscriber = new GeohashSubscriber();

                    new HBaseCrudService<GeohashSubscriber>().delete(new RowKey() {
                        @Override
                        public String getRowKey() {
                            return _valueParameter;
                        }
                    }, _geohashSubscriber);

                    __resp.getWriter().println("Unsubscribed " + _valueParameter);

                    break;
                default:
                    __resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unrecognized Type Parameter Value:" + _typeParameter);
            }
        } catch (final Throwable e) {
            Loggers.error("Process Error", e);
            __resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unrecognized Type Parameter Value:" + _typeParameter);
        }


    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
