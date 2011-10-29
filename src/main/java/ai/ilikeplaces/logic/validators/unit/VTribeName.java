package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.util.RefObj;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

/**
* Created by IntelliJ IDEA.
* User: Ravindranath Akila
* Date: 10/29/11
* Time: 10:02 AM
*/
public class VTribeName extends RefObj<String> {

    @IsInvariant
    @NotNull(message = "Tribe Name Wrong!")
    @Length(min = 1, max = 255, message = "Tribe Name Wrong!")
    @Override
    public String getObj() {
        return obj;
    }

    public RefObj<String> getSelfAsValid(final net.sf.oval.Validator... validator) {
        return super.getSelfAsValid(validator);
    }
}
