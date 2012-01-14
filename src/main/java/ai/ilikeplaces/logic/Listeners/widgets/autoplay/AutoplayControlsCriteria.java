package ai.ilikeplaces.logic.Listeners.widgets.autoplay;

import ai.ilikeplaces.logic.validators.unit.HumanId;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/13/12
 * Time: 7:17 PM
 */
public class AutoplayControlsCriteria {
    private HumanId humanId;

    public HumanId getHumanId() {
        return humanId;
    }

    public AutoplayControlsCriteria setHumanId(final HumanId humanId) {
        this.humanId = humanId;
        return this;
    }
}
