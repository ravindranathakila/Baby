package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.util.EventType;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/13/11
 * Time: 9:31 PM
 */
public class NotificationActionJS {
// ------------------------------ FIELDS ------------------------------

    final String jsCodeToSend;
    final EventType eventType;

// --------------------------- CONSTRUCTORS ---------------------------

    NotificationActionJS(final EventType eventType, final String jsCodeToSend) {
        this.eventType = eventType;
        this.jsCodeToSend = jsCodeToSend;
    }
}
