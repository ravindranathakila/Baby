package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.logic.validators.unit.HumanId;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/29/11
 * Time: 8:03 PM
 */
public class WallWidgetTribeCriteria {
// ------------------------------ FIELDS ------------------------------

    private HumanId humanId;
    private Long tribeId__;
    private Long wallId;

// ------------------------ ACCESSORS / MUTATORS ------------------------

    public HumanId getHumanId() {
        return humanId;
    }

    public void setHumanId(HumanId humanId) {
        this.humanId = humanId;
    }

    public Long getTribeId() {
        return tribeId__;
    }

    public void setTribeId__(Long tribeId__) {
        this.tribeId__ = tribeId__;
    }

    public Long getWallId() {
        return wallId;
    }

    public void setWallId(Long wallId) {
        this.wallId = wallId;
    }
}
