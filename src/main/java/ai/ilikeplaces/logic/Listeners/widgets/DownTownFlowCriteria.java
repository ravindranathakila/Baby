package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/16/11
 * Time: 2:46 PM
 */
public class DownTownFlowCriteria {
// ------------------------------ FIELDS ------------------------------

    private DownTownFlowDisplayComponent downTownFlowDisplayComponent;
    private HumanId humanId;
    private HumanUserLocal humanUserLocal;
    private InviteData inviteData;

// -------------------------- ENUMERATIONS --------------------------

    public enum DownTownFlowDisplayComponent {
        TALKS,
        MOMENTS,
        TRIBES
    }

// ------------------------ ACCESSORS / MUTATORS ------------------------

    public DownTownFlowDisplayComponent getDownTownFlowDisplayComponent() {
        return downTownFlowDisplayComponent;
    }

    public HumanId getHumanId() {
        return humanId;
    }

    public HumanUserLocal getHumanUserLocal() {
        return humanUserLocal;
    }

// -------------------------- OTHER METHODS --------------------------

    public InviteData getInviteData() {
        return inviteData == null ? (inviteData = new InviteData()) : inviteData;
    }

    public DownTownFlowCriteria setDownTownFlowDisplayComponent(final DownTownFlowDisplayComponent downTownFlowDisplayComponent) {
        this.downTownFlowDisplayComponent = downTownFlowDisplayComponent;
        return this;
    }

    public DownTownFlowCriteria setHumanId(final HumanId humanId) {
        this.humanId = humanId;
        return this;
    }

    public DownTownFlowCriteria setHumanUserLocal(final HumanUserLocal humanUserLocal) {
        this.humanUserLocal = humanUserLocal;
        return this;
    }

// -------------------------- INNER CLASSES --------------------------

    public class InviteData {
// ------------------------------ FIELDS ------------------------------

        private Email email;

// ------------------------ ACCESSORS / MUTATORS ------------------------

        public Email getEmail() {
            return email;
        }

        public InviteData setEmail(final Email email) {
            this.email = email;
            return this;
        }
    }
}
