package ai.ilikeplaces.logic.validators.oval;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.logic.validators.ERRORCODE;
import ai.ilikeplaces.logic.validators.oval.internal.UpperCaseCheck;
import net.sf.oval.configuration.annotation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Mar 8, 2010
 * Time: 3:21:27 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Constraint(checkWith = UpperCaseCheck.class)
@NOTE(note = "COPIED FROM OVAL DOCUMENTATION AND FIXED")
public @interface UpperCase {
    /**
     * message to be used for the ConstraintsViolatedException
     *
     * @see net.sf.oval.exception.ConstraintsViolatedException
     */
    String message() default "must be upper case";
}