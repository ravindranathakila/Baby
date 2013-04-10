package ai.ilikeplaces.logic.sits9;

import ai.ilikeplaces.util.Loggers;
import ai.scribble.License;
import ai.scribble._note;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Startup
@Singleton
@_note(note = "USE s.o.p FOR LOGGING AS LOGGER MIGHT FAIL TOO!")
public class Helios implements HeliosRemote {


    @EJB
    private SubscriberNotificationsRemote subscriberNotificationsRemote;


    public Helios() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            subscriberNotificationsRemote.startTimer();
        } catch (final Exception e) {
            Loggers.error("Error starting subscriber notification timer", e);
        }
    }
}
