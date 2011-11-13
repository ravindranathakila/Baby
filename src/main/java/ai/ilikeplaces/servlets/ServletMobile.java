package ai.ilikeplaces.servlets;

import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.role.HumanUser;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.ilikeplaces.util.SessionBoundBadRefWrapper;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 11/13/11
 * Time: 8:03 PM
 */
public class ServletMobile extends HttpServlet {

    protected void processRequest(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        final Object attribute__ = req.getSession(true).getAttribute(ServletLogin.HumanUser);

        final SessionBoundBadRefWrapper<HumanUserLocal> sessionBoundBadRefWrapper = getEJBSession(attribute__);

        try {
            final HumanUserLocal humanUserAsValid = HumanUser.getHumanUserAsValid(sessionBoundBadRefWrapper);

            final Map parameterMap = req.getParameterMap();

            if (parameterMap.containsKey("tribeId")) {
                final Tribe tribe = DB.getHumanCRUDTribeLocal(false).getTribe(humanUserAsValid.getHumanId(), new VLong(Long.parseLong(((String[]) parameterMap.get("tribeId"))[0])));

                final Gson gson = new Gson();

                resp.getWriter().flush();
                resp.getWriter().append(gson.toJson(tribe));
            } else {
                resp.getWriter().flush();
                resp.getWriter().append(Arrays.toString(DB.getHumanCRUDTribeLocal(false).getHumansTribes(humanUserAsValid.getHumanId()).toArray()));
            }
        } catch (final Throwable t) {
            resp.getWriter().flush();
            resp.getWriter().append(t.getMessage());
        }

    }


    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
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
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    private SessionBoundBadRefWrapper<HumanUserLocal> getEJBSession(Object attribute__) {
        return attribute__ == null ?
                null : (!((SessionBoundBadRefWrapper<HumanUserLocal>) attribute__).isAlive() ?
                null : ((SessionBoundBadRefWrapper<HumanUserLocal>) attribute__));
    }
}
