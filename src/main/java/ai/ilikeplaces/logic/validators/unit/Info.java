package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.RefObj;
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
public class Info extends RefObj<String> {

    public Info(final String info) {
        obj = info;
    }

    public Info() {
    }

    @IsInvariant
    @NotNull(message = "Sorry! You must specify a value.")
    @Length(min = 1, max = 500, message = "Sorry! Value should be of length between 1 and 500.")
    @Override
    public String getObj() {
        return obj;
    }
}