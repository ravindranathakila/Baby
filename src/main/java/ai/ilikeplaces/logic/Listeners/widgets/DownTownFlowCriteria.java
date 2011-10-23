package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/16/11
 * Time: 2:46 PM
 */
public class DownTownFlowCriteria {

    private DownTownFlowDisplayComponent downTownFlowDisplayComponent;
    private HumanId humanId;
    private HumanUserLocal humanUserLocal;

    public DownTownFlowDisplayComponent getDownTownFlowDisplayComponent() {
        return downTownFlowDisplayComponent;
    }

    public DownTownFlowCriteria setDownTownFlowDisplayComponent(final DownTownFlowDisplayComponent downTownFlowDisplayComponent) {
        this.downTownFlowDisplayComponent = downTownFlowDisplayComponent;
        return this;
    }

    public HumanUserLocal getHumanUserLocal() {
        return humanUserLocal;
    }

    public DownTownFlowCriteria setHumanUserLocal(final HumanUserLocal humanUserLocal) {
        this.humanUserLocal = humanUserLocal;
        return this;
    }

    public enum DownTownFlowDisplayComponent {
        TALKS,
        MOMENTS
    }

    public HumanId getHumanId() {
        return humanId;
    }

    public DownTownFlowCriteria setHumanId(final HumanId humanId) {
        this.humanId = humanId;
        return this;
    }
}
