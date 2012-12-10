package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.reaver.HumanId;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/20/11
 * Time: 8:51 PM
 */
public class TribeWidgetCriteria {
// ------------------------------ FIELDS ------------------------------


// ------------------------------ FIELDS (NON-STATIC)--------------------


    private HumanId humanId;
    private VLong tribeId;
    private Tribe tribe;

    private InviteData inviteData;

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
    public InviteData getInviteData() {

        return inviteData == null ? (inviteData = new InviteData()) : inviteData;
    }

    public TribeWidgetCriteria setHumanId(HumanId humanId) {

        this.humanId = humanId;
        return this;
    }

    public TribeWidgetCriteria setTribeId(VLong tribeId) {

        this.tribeId = tribeId;
        return this;
    }

// -------------------------- INNER CLASSES --------------------------

    public class InviteData {
// ------------------------------ FIELDS ------------------------------


// ------------------------------ FIELDS (NON-STATIC)--------------------


        private Email email;

        // ------------------------ ACCESSORS / MUTATORS ------------------------
        public Email getEmail() {

            return email;
        }

        // -------------------------- OTHER METHODS --------------------------
        public InviteData setEmail(final Email email) {

            this.email = email;
            return this;
        }
    }
}
