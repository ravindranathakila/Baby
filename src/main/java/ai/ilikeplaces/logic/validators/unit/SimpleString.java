package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.RefObj;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 21, 2010
 * Time: 8:58:01 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class SimpleString extends RefObj<String> {

    public SimpleString() {
    }

    public SimpleString(final String name) {
        obj = name;
    }

    @IsInvariant
    @NotNull(message = "Sorry! You must enter a value.")
    @Override
    public String getObj() {
        return obj;
    }
}