package ai.ilikeplaces.logic.validators.unit;

import ai.reaver.RefObj;
import ai.scribble.License;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 22, 2010
 * Time: 2:16:43 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class Password extends RefObj<String> {

    public Password() {
    }

    public Password(final String password) {
        obj = password;
    }

    @IsInvariant
    @NotNull
    @Length(min = 8, max = 255, message = "Sorry! Password is not between 8 and 255 characters long.")
    @NotBlank(message = "Sorry! Password cannot be blank.")
    @Override
    final public String getObj() {
        return obj;
    }


}
