package ai.baby.logic.validators.unit;

import ai.baby.logic.validators.oval.NoXSSAttack;
import ai.baby.util.apache.fixes.StringEscapeUtilsFixed;
import ai.reaver.RefObj;
import ai.scribble.License;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Mar 8, 2010
 * Time: 10:20:12 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class XSSSafeString extends RefObj<String> {
    public XSSSafeString(final String str) {
        obj = StringEscapeUtilsFixed.escapeXSS(str);
    }

    @Override
    public void setObj(final String str) {
        obj = StringEscapeUtilsFixed.escapeXSS(str);
    }

    @IsInvariant
    @NotNull(message = "Sorry! You must enter a value.")
    @NoXSSAttack(message = "Sorry! XSS Prevention Failed.")
    @Override
    public String getObj() {
        return obj;
    }
}
