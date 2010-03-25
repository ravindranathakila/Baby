package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.validators.oval.NoXSSAttack;
import ai.ilikeplaces.util.RefObj;
import ai.ilikeplaces.util.apache.fixes.StringEscapeUtilsFixed;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Mar 8, 2010
 * Time: 10:20:12 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class XSSSafeString extends RefObj<String> {
    public XSSSafeString(final String str) {
        obj = StringEscapeUtilsFixed.escapeXSS(str);
    }

    @Override
    public void setObj(final String str){
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
