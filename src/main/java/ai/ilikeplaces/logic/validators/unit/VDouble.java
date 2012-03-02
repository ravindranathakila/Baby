package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.util.Obj;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.NotNull;

/**
 * Does not allow nulls to be valid
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/27/11
 * Time: 7:10 PM
 */
public class VDouble extends Obj<Double> {

    public VDouble(final Double aDouble) {
        super(aDouble);
    }

    public VDouble() {
    }

    @Override
    public VDouble getSelfAsValid(final Validator... validator) {
        return (VDouble) super.getSelfAsValid(validator);
    }

    @Override
    public VDouble setObjAsValid(final Double obj) {
        return (VDouble) super.setObjAsValid(obj);
    }

    @IsInvariant
    @NotNull
    @Override
    public Double getObj() {
        return obj;
    }
}
