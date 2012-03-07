package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.logic.validators.unit.HumanId;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 3/7/12
 * Time: 2:02 PM
 */
public class SignInOnCriteria {

    private HumanId humanId;

    private SignInOnDisplayComponent signInOnDisplayComponent;

    public enum SignInOnDisplayComponent {
        TALKS,
        MOMENTS,
        TRIBES,
        HOME,
        SNAPS
    }

    public HumanId getHumanId() {
        return humanId;
    }

    public SignInOnCriteria setHumanId(final HumanId humanId) {
         this.humanId = humanId;
        return this;
    }

    public SignInOnDisplayComponent getSignInOnDisplayComponent() {
        return signInOnDisplayComponent;
    }

    public SignInOnCriteria setSignInOnDisplayComponent(final SignInOnDisplayComponent signInOnDisplayComponent) {
        this.signInOnDisplayComponent = signInOnDisplayComponent;
        return this;
    }
}
