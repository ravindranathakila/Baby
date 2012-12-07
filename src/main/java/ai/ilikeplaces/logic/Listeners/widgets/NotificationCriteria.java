package ai.ilikeplaces.logic.Listeners.widgets;

import ai.doc._note;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/13/11
 * Time: 9:19 PM
 */
@_note("Use this class with one type of NotificationAction* ONLY. " +
        "Only one should be populated and others should be null. ")
public class NotificationCriteria {
// ------------------------------ FIELDS ------------------------------

    NotificationActionJS notificationActionJS = null;
    String notificationText = "";

// --------------------------- CONSTRUCTORS ---------------------------

    public NotificationCriteria(final NotificationActionJS notificationActionJS, final String notificationText) {
        this.notificationActionJS = notificationActionJS;
        this.notificationText = notificationText;
    }

    public NotificationCriteria(String notificationText) {
        this.notificationText = notificationText;
    }
}
