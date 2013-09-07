package ai.baby.logic.Listeners.widgets.subscribe;

import ai.ilikeplaces.entities.etc.HumanId;
import ai.baby.logic.validators.unit.Email;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 11/27/11
 * Time: 8:08 AM
 */
public class SubscribeCriteria {
// ------------------------------ FIELDS ------------------------------


// ------------------------------ FIELDS (NON-STATIC)--------------------


    private HumanId humanId;

    private SignupCriteria signupCriteria;

// --------------------------- CONSTRUCTORS ---------------------------

    public SubscribeCriteria() {
    }

    public SubscribeCriteria(final HumanId humanId) {

        this.humanId = humanId;
    }

    // ------------------------ ACCESSORS / MUTATORS ------------------------
    public HumanId getHumanId() {

        return humanId;
    }

    public SignupCriteria getSignupCriteria() {

        return signupCriteria == null ? (signupCriteria = new SignupCriteria()) : signupCriteria;
    }


    // -------------------------- OTHER METHODS --------------------------
    public SubscribeCriteria setHumanId(final HumanId humanId) {

        this.humanId = humanId;
        return this;
    }

// -------------------------- INNER CLASSES --------------------------

    public class SignupCriteria {
// ------------------------------ FIELDS ------------------------------


// ------------------------------ FIELDS (NON-STATIC)--------------------


        private Email email;

        // ------------------------ ACCESSORS / MUTATORS ------------------------
        public Email getEmail() {

            return email == null ? (email = new Email()) : email;
        }

        // -------------------------- OTHER METHODS --------------------------
        public SignupCriteria setEmail(final Email email) {

            this.email = email;
            return this;
        }
    }
}
