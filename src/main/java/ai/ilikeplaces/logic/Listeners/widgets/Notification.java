package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/13/11
 * Time: 9:18 PM
 */
public class Notification extends AbstractWidgetListener<NotificationCriteria>{

    public Notification(final ItsNatServletRequest request__, final NotificationCriteria notificationCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.Notification, notificationCriteria, appendToElement__);
    }

    @Override
    protected void init(final NotificationCriteria notificationCriteria) {
        $$(Controller.Page.notification_simple).setTextContent(notificationCriteria.notificationText);
    }

    @Override
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {
    }
}
