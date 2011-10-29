package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.util.Obj;

/**
 * Does not allow nulls to be valid
 *
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/27/11
 * Time: 7:10 PM
 */
public class VLong extends Obj<Long>{

    public VLong(final Long aLong) {
        super(aLong);
    }
}
