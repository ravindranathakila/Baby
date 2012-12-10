package ai.ilikeplaces.logic.Listeners.widgets.teach;

import ai.ilikeplaces.logic.validators.unit.Email;
import ai.util.HumanId;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 11/27/11
 * Time: 8:08 AM
 */
public class TeachMomentCriteria {
// ------------------------------ FIELDS ------------------------------


// ------------------------------ FIELDS (NON-STATIC)--------------------


    private HumanId humanId;

    private SignupCriteria signupCriteria;

// --------------------------- CONSTRUCTORS ---------------------------

    public TeachMomentCriteria() {
    }

    public TeachMomentCriteria(final HumanId humanId) {

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
    public TeachMomentCriteria setHumanId(final HumanId humanId) {

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
