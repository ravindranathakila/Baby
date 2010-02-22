package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.RefObj;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 22, 2010
 * Time: 2:16:43 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class HumanId extends RefObj<String> {

    public HumanId() {
    }

    public HumanId(final String humanId) {
        obj = humanId;
    }

    public HumanId(final String humanId, final boolean doValidateAndThrow) {
        if (doValidateAndThrow) {
            /*Threading issue possible here as the validator thread will accesses the object before creation finishes*/
            setObjAsValid(humanId);
        } else {
            obj = humanId;
        }
    }

    @IsInvariant
    @NotNull
    @Length(min = 1)
    @Override
    public String getObj() {
        return obj;
    }
}
