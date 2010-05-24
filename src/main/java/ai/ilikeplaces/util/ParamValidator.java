package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.exception.ConstraintsViolatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ParamValidator {

    final static private Logger logger = LoggerFactory.getLogger(ParamValidator.class);


    @AroundInvoke
    public Object validate(InvocationContext invocation) throws Exception {
        try {
            final Object[] args = invocation.getParameters();
            final List<ConstraintViolation> violations = new ArrayList<ConstraintViolation>();
            final Validator v = new Validator();

            for (final Object param : args) {
                //violations.addAll(recursiveCollectionValidator(param, v));
                violations.addAll(v.validate(param));
            }

            if (violations.size() != 0) {
                Loggers.EXCEPTION.error(RefObj.validationMessages(violations));
                throw new ConstraintsViolatedException(violations);
            }
        } catch (final Throwable t) {
            throw new RuntimeException(t);
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