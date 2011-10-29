package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.util.Obj;
import ai.ilikeplaces.util.RefObj;
import net.sf.oval.Validator;

/**
 * Does not allow nulls to be valid
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/27/11
 * Time: 7:10 PM
 */
public class VLong extends Obj<Long> {

    public VLong(final Long aLong) {
        super(aLong);
    }

    public VLong() {
    }

    @Override
    public VLong getSelfAsValid(Validator... validator) {
        return (VLong) super.getSelfAsValid(validator);
    }

    @Override
    public VLong setObjAsValid(Long obj) {
        return (VLong) super.setObjAsValid(obj);
    }
}
