package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.TODO;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.exception.ConstraintsViolatedException;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.*;

/**
 * Made only to support simple param validations. Later on, add complex parameter validations.
 * A null anyyway should throw an exception
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 15, 2010
 * Time: 1:14:18 AM
 */

@TODO(task = "IMPLEMENT RECURRSSIVE VALIDATOR. NOW ITS COMMENTED.")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ParamValidator {
    private static final String PARAMETER_VALIDATION_ERROR_MESSAGE = "SORRY! I ENCOUNTERED AN EXCEPTION WITHIN THE PARAMETER VALIDATION INTERCEPTOR. DETAILS ARE AS FOLLOWS. ";
    private static final String SORRY_PARAMETER_POSITION_NUMBER_GUESSED = "SORRY! PARAMETER WITH POSITION NUMBER(GUESSED) ";
    private static final String IS_NULL = " IS NULL";

    @AroundInvoke
    public Object validate(final InvocationContext invocation) throws Exception {
        final Object[] args = invocation.getParameters();
        final List<ConstraintViolation> violations = new ArrayList<ConstraintViolation>();
        final Validator v = new Validator();

        Object param = null;

        for (int i = 0; i < args.length; i++) {
            param = args[i];
            //violations.addAll(recursiveCollectionValidator(param, v));
            if (param == null) {
                Loggers.EXCEPTION.error(PARAMETER_VALIDATION_ERROR_MESSAGE + SORRY_PARAMETER_POSITION_NUMBER_GUESSED + (i + 1) + IS_NULL);
                throw new NullPointerException((SORRY_PARAMETER_POSITION_NUMBER_GUESSED + (i + 1) + IS_NULL));
            } else {
                violations.addAll(v.validate(param));
            }
        }


        if (violations.size() != 0) {
            Loggers.EXCEPTION.error(PARAMETER_VALIDATION_ERROR_MESSAGE + RefObj.validationMessages(violations));
            throw new ConstraintsViolatedException(violations);
        }
        return invocation.proceed();
    }

    @NOTE(note = "This could be possibly simplified")
    private List<ConstraintViolation> recursiveCollectionValidator(final Object object, final Validator v) {
        if (object instanceof Collections) {
            final List<ConstraintViolation> violations = new ArrayList<ConstraintViolation>();
            for (final Object obj : (Collection) object) {
                violations.addAll(obj instanceof Collections
                        ? recursiveCollectionValidator(obj, v)
                        : obj instanceof Arrays
                        ? recurrsiveArrayValidator(obj, v)
                        : v.validate(obj));
            }
            return violations;
        } else if (object instanceof Arrays) {
            return recurrsiveArrayValidator(object, v);
        } else {
            return v.validate(object);
        }
    }

    @NOTE(note = "This could be possibly simplified")
    private List<ConstraintViolation> recurrsiveArrayValidator(final Object object, final Validator v) {
        if (object instanceof Arrays) {
            final List<ConstraintViolation> violations = new ArrayList<ConstraintViolation>();
            for (final Object obj : (Object[]) object) {
                violations.addAll(obj instanceof Arrays
                        ? recurrsiveArrayValidator(obj, v)
                        : obj instanceof Collections
                        ? recursiveCollectionValidator(obj, v)
                        : v.validate(obj));
            }
            return violations;
        } else if (object instanceof Collections) {
            return recurrsiveArrayValidator(object, v);
        } else {
            return v.validate(object);
        }

    }


}