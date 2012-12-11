package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.util.Loggers;
import ai.scribble.License;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Nov 23, 2010
 * Time: 6:15:52 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerGeoBusiness implements ItsNatServletRequestListener {
    public static final String CALLED = " CALLED.";

    @Override
    public void processRequest(final ItsNatServletRequest itsNatServletRequest, final ItsNatServletResponse response) {
        Loggers.INFO.info(getClass().getName() + CALLED);
    }

}
