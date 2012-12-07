package ai.ilikeplaces.logic.validators.oval.internal;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc._note;
import ai.ilikeplaces.logic.validators.oval.UpperCase;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;

import static ai.ilikeplaces.util.apache.fixes.StringEscapeUtilsFixed.*;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Mar 8, 2010
 * Time: 3:20:12 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_note(note = "USE OWASP ANTISAMY")
public class NoXSSAttackCheck extends AbstractAnnotationCheck<UpperCase> {


    public boolean isSatisfied(final Object validatedObject,
                               final Object valueToValidate,
                               final OValContext context,
                               final Validator validator) {

        final String str = (String) valueToValidate;//Cast so we prevent invalid usage

        return !checkescapeJavaScript(str)
                && !checkescapeXml(str)
                && !checkescapeHtml(str);
    }
}
