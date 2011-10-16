package ai.ilikeplaces.logic.Listeners.widgets;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/16/11
 * Time: 2:46 PM
 */
public class DownTownFlowCriteria {

    private DownTownFlowDisplayComponent downTownFlowDisplayComponent;

    public DownTownFlowDisplayComponent getDownTownFlowDisplayComponent() {
        return downTownFlowDisplayComponent;
    }

    public DownTownFlowCriteria setDownTownFlowDisplayComponent(final DownTownFlowDisplayComponent downTownFlowDisplayComponent) {
        this.downTownFlowDisplayComponent = downTownFlowDisplayComponent;
        return this;
    }

    public enum DownTownFlowDisplayComponent{
        TALKS,
        MOMENTS
    }
}
