package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.RefObj;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 21, 2010
 * Time: 8:58:01 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ForgotPasswordCode extends RefObj<String> {

    public ForgotPasswordCode() {
    }

    public ForgotPasswordCode(final String name) {
        obj = name;
    }

    @IsInvariant
    @NotNull(message = "Oops! You must enter a value.")
    @NotEmpty(message = "Are you sure we sent you a blank code?")
    @Override
    public String getObj() {
        return obj;
    }
}