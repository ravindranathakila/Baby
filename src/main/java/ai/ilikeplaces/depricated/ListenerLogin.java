package ai.ilikeplaces.depricated;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

/**
 *
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Deprecated
public class ListenerLogin implements ItsNatServletRequestListener {

    /**
     *
     * @param request
     * @param response
     */
    @Override
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response) {
        //ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument) request.getItsNatDocument();
        new Login(request.getItsNatDocument());
    }
}
