package ai.ilikeplaces.logic.Listeners.widgets;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/23/11
 * Time: 6:09 PM
 */
public class TribeSidebarCriteria {
    String humanId;
    long tribeId;

    public String getHumanId() {
        return humanId;
    }

    public TribeSidebarCriteria setHumanId(final String humanId) {
        this.humanId = humanId;
        return this;
    }

    public long getTribeId() {
        return tribeId;
    }

    public TribeSidebarCriteria setTribeId(final long tribeId) {
        this.tribeId = tribeId;
        return this;
    }
}
