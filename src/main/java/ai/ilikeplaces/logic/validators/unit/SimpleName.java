package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.util.RefObj;
import ai.scribble.License;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 21, 2010
 * Time: 8:58:01 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class SimpleName extends RefObj<String> {

    public SimpleName() {
    }

    public SimpleName(final String name) {
        obj = name;
    }

    @IsInvariant
    @NotNull(message = "Sorry! You must specify a value.")
    @Length(min = 1, max = 30, message = "Sorry! Value should be of length between 1 and 30")
    @Override
    public String getObj() {
        return obj;
    }
}
