package ai.ilikeplaces.logic.validators.oval.internal;

import ai.ilikeplaces.logic.validators.oval.UpperCase;
import ai.scribble.License;
import ai.scribble._note;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Mar 8, 2010
 * Time: 3:20:12 PM
 * <p/>
 * Checks if a String is uppercase
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_note(note = "COPIED FROM OVAL DOCUMENTATION AND FIXED")
public class UpperCaseCheck extends AbstractAnnotationCheck<UpperCase> {
    public boolean isSatisfied(final Object validatedObject,
                               final Object valueToValidate,
                               final OValContext context,
                               final Validator validator) {

        if (valueToValidate == null) return true;

        final String val = valueToValidate.toString();

        return val.equals(val.toUpperCase());
    }
}
