package ai.ilikeplaces;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;

/**
 *
 * @author Ravindranath Akila
 */
public class ListenerLogin implements ItsNatServletRequestListener {

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response) {
        //ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument) request.getItsNatDocument();
        new Login(request.getItsNatDocument());
    }
}
