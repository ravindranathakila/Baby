package ai.ilikeplaces.logic.Listeners.widgets;

/**
 * Keep this JavaBean very simple as this is an info widget which will be extensively used in different contexts
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 4/15/12
 * Time: 10:35 AM
 */
public class InfoCriteria {

    private String title;

    public String getTitle() {
        return title != null ? title : (title = "");
    }

    public InfoCriteria setTitle(final String title) {
        this.title = title;
        return this;
    }
}
