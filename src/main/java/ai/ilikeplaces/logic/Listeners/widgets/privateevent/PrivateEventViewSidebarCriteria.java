package ai.ilikeplaces.logic.Listeners.widgets.privateevent;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/23/11
 * Time: 6:09 PM
 */
public class PrivateEventViewSidebarCriteria {
    String humanId__;
    long privateEventId__ ;

    public String getHumanId__() {
        return humanId__;
    }

    public PrivateEventViewSidebarCriteria setHumanId__(final String humanId__) {
        this.humanId__ = humanId__;
        return this;
    }

    public long getPrivateEventId__() {
        return privateEventId__;
    }

    public PrivateEventViewSidebarCriteria setPrivateEventId__(final long privateEventId__) {
        this.privateEventId__ = privateEventId__;
        return this;
    }
}
