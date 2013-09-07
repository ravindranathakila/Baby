package ai.baby.logic.Listeners.widgets;

import ai.baby.logic.validators.unit.SimpleString;
import ai.ilikeplaces.entities.etc.HumanId;
import ai.baby.logic.validators.unit.Password;
import ai.baby.util.Obj;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 3/7/12
 * Time: 2:02 PM
 */
public class SignInOnCriteria {

    private HumanId humanId;

    public HumanId username = null;
    public Password password = null;

    public SimpleString dbHash = null;
    public SimpleString dbSalt = null;
    public Obj<Boolean> userOk = null;
    public Obj<Boolean> existButNotActive = null;

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
