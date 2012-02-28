package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.logic.validators.unit.Email;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 2/17/12
 * Time: 6:48 PM
 */
public class JuiceCriteria {

    private InviteData inviteData;


    public InviteData getInviteData() {
        return inviteData == null ? (inviteData = new InviteData()) : inviteData;
    }

    // -------------------------- INNER CLASSES --------------------------

    public class InviteData {
        // ------------------------------ FIELDS ------------------------------

        private Email email;

        // ------------------------ ACCESSORS / MUTATORS ------------------------

        public Email getEmail() {
            return email != null ? email : (email = new Email());
        }

        public InviteData setEmail(final Email email) {
            this.email = email;
            return this;
        }
    }
}
