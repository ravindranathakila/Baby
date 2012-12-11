package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.util.RefObj;
import ai.scribble.License;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 21, 2010
 * Time: 8:58:01 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ProfileUrl extends RefObj<String> {

    public ProfileUrl() {
    }

    public ProfileUrl(final String name) {
        obj = name;
    }

    @IsInvariant
    @NotNull(message = "Sorry! You must enter a value.")
    @NotEmpty(message = "Sorry! You must enter a value.")
    @MatchPattern(
            message = "Sorry! Use only letters numbers and underscores",
            pattern = "^[a-zA-Z0-9_]*$")
    @Override
    public String getObj() {
        return obj;
    }
}
