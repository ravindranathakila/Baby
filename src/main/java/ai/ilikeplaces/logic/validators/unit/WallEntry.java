package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.util.RefObj;
import ai.scribble.License;
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
public class
        WallEntry extends RefObj<String> {
    private static final String SPEAK_UP = "Speak up!";

    public WallEntry() {
    }

    public WallEntry(final String name) {
        obj = name;
    }

    @IsInvariant
    @NotNull(message = SPEAK_UP)
    @NotEmpty(message = SPEAK_UP)
    //@WallSeparators(message = "Don't use " + WallSeparatorsCheck.GT + " or " + WallSeparatorsCheck.PIPE)
    @Override
    public String getObj() {
        return obj;
    }
}
