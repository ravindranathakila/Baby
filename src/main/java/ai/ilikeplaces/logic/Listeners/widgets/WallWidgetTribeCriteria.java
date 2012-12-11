package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.etc.HumanId;

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

    public WallWidgetTribeCriteria setHumanId(HumanId humanId) {
        this.humanId = humanId;
        return this;
    }

    public Long getTribeId() {
        return tribeId__;
    }

    public WallWidgetTribeCriteria setTribeId(Long tribeId__) {
        this.tribeId__ = tribeId__;
        return this;
    }

    public Long getWallId() {
        return wallId;
    }

    public WallWidgetTribeCriteria setWallId(Long wallId) {
        this.wallId = wallId;
        return this;
    }
}
