package ai.ilikeplaces.logic.validators.faces;

import ai.ilikeplaces.logic.validators.Validator;
import ai.ilikeplaces.util.Factory;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.ReturnParams;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 1, 2010
 * Time: 4:09:32 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface ValidatorFace {

    /**
     * This factory object is also of the type of object you require to manufacture.
     * How ever, you should get an instance during usage. This ensures that if the
     * under-laying implementation switches between singleton and non-singleton patterns,
     * your code remains in tact.
     */
    final static public Factory<Validator> impl = ((Factory<Validator>) Validator.getInstance()).getFactory();

    public Return isLessThan1000(final String input);

    public Return<ReturnParams> isPriavateEventName(final String input);

    public Return<ReturnParams> isPrivateEventInfo(final String input);

    public Return<ReturnParams> isPrivateLocationName(final String input);

    public Return<ReturnParams> isPrivateLocationInfo(final String input);

}
