package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.VLong;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/20/11
 * Time: 8:51 PM
 */
public class TribeWidgetCriteria {
// ------------------------------ FIELDS ------------------------------

    private HumanId humanId;
    private VLong tribeId;
    private Tribe tribe;

// ------------------------ ACCESSORS / MUTATORS ------------------------

    public HumanId getHumanId() {
        return humanId;
    }

    public Tribe getTribe() {
        return tribe;
    }

    public void setTribe(final Tribe tribe) {
        this.tribe = tribe;
    }

    public VLong getTribeId() {
        return tribeId;
    }

// -------------------------- OTHER METHODS --------------------------

    public TribeWidgetCriteria setHumanId(HumanId humanId) {
        this.humanId = humanId;
        return this;
    }

    public TribeWidgetCriteria setTribeId(VLong tribeId) {
        this.tribeId = tribeId;
        return this;
    }
}
