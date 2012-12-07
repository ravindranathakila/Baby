/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 1, 2010
 * Time: 4:22:04 PM
 */
package ai.ilikeplaces.logic.validators;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.validators.faces.ValidatorFace;
import ai.ilikeplaces.util.Factory;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.ReturnImpl;
import ai.ilikeplaces.util.ReturnParams;

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class Validator implements ValidatorFace, Factory {
    final private static Validator Instance = new Validator();

    public static Validator getInstance() {
        return Instance;
    }

    public Validator getNewInstance() {
        return Instance;
    }

    /**
     * Returns a factory object of the given type
     *
     * @param initArgs
     * @return a factory object of the given type
     */
    @Override
    public Factory getFactory(final Object... initArgs) {
        return Instance;
    }

    @Override
    public ValidatorFace getInstance(final Object... initArgs) {
        return Instance;
    }

    private Validator() {
    }


    @Override
    public Return isLessThan1000(final String input) {
        return new ReturnImpl<String>(input, "");
    }

    @Override
    public Return<ReturnParams> isPrivateEventName(final String input) {
        return new ReturnImpl<ReturnParams>((new ReturnParams(true, input)), "");
    }

    @Override
    public Return<ReturnParams> isPrivateEventInfo(final String input) {
        return new ReturnImpl<ReturnParams>((new ReturnParams(true, input)), "");
    }

    @Override
    public Return<ReturnParams> isPrivateLocationName(final String input) {
        return new ReturnImpl<ReturnParams>((new ReturnParams(true, input)), "");
    }

    @Override
    public Return<ReturnParams> isPrivateLocationInfo(final String input) {
        return new ReturnImpl<ReturnParams>((new ReturnParams(true, input)), "");
    }


}
