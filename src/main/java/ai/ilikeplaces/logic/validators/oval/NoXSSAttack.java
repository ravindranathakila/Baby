package ai.ilikeplaces.logic.validators.oval;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import net.sf.oval.configuration.annotation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ai.ilikeplaces.logic.validators.oval.internal.NoXSSAttackCheck;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Mar 8, 2010
 * Time: 3:21:27 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Constraint(checkWith = NoXSSAttackCheck.class)
@NOTE(note = "COPIED FROM OVAL DOCUMENTATION AND FIXED")
public @interface NoXSSAttack {
    /**
     * message to be used for the ConstraintsViolatedException
     *
     * @see net.sf.oval.exception.ConstraintsViolatedException
     */
    public abstract String message() default "XSS attack detected";
}