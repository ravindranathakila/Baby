package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.VTribeName;
import ai.ilikeplaces.logic.validators.unit.VTribeStory;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/20/11
 * Time: 8:51 PM
 */
public class TribeCreateWidgetCriteria {
    private VTribeName vTribeName;
    private VTribeStory vTribeStory;
    private HumanId humanId;

    public TribeCreateWidgetCriteria(HumanId humanId) {
        this.humanId = humanId;
        this.vTribeName = new VTribeName();
        this.vTribeStory = new VTribeStory();
    }

    public VTribeName getvTribeName() {
        return vTribeName;
    }

    public VTribeStory getvTribeStory() {
        return vTribeStory;
    }

    public HumanId getHumanId() {
        return humanId;
    }

    public void setHumanId(HumanId humanId) {
        this.humanId = humanId;
    }

}
