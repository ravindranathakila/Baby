package ai.ilikeplaces.util;

import ai.doc._note;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.exception.ConstraintsViolatedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class is a helper class to store user input values.
 * While the class also facilitates to the validation framework,
 * it also copies every reference to its own, enforcing security
 * but at the compromise of a new reference created.
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 21, 2010
 * Time: 6:03:22 PM
 */
public abstract class RefObj<T> implements Serializable {

    protected T obj = null;

    private List<ConstraintViolation> constraintViolations = null;
    public static final String CODE = "[code:";
    public static final String CLOSE_SQUARE_BRACKET = "]";

    abstract public T getObj();

    /**
     * Use this method to return this same object after validation.
     * <p/>
     * <p/>
     * If you are using a derived class, override this method with body and signature with return type DerivedClass
     * <p/>
     * <b>return (DerivedClass) super.getSelfAsValid(validator)</b>
     *
     * @param validator
     * @return valid object
     */
    public RefObj<T> getSelfAsValid(final Validator... validator) {
        final Validator v = validator.length == 0 ? new Validator() : validator[0];
        constraintViolations = v.validate(this);
        if (constraintViolations.size() != 0) {
            throw new ConstraintsViolatedException(constraintViolations);
        }
        return this;
    }

    /**
     * Use this method to obtain an object that is expected to be valid.
     *
     * @return valid object
     * @throws ConstraintsViolatedException if found validation errors
     * @throws IllegalAccessError           if called without validating
     */
    public T getObjectAsValid() {
        if (constraintViolations != null) {
            if (constraintViolations.size() == 0) {
                return getObj();
            } else {
                throw new ConstraintsViolatedException(constraintViolations);
            }
        } else {
            if (this.validate() == 0) {
                return getObj();
            } else {
                throw new ConstraintsViolatedException(constraintViolations);
            }
        }
    }

    /**
     * Sets the value for this object.
     * Each time this methods is called, validations should/will be reset
     *
     * @param valueToBeSetToObj
     */
    public void setObj(final T valueToBeSetToObj) {
        constraintViolations = null;//So that validation is consistent
        this.obj = valueToBeSetToObj;
    }

    @_note(note = "This approach is specially used when internally creating objects that should be valid as opposed to a web user." +
            "This approach will prevent the code from setting null values and other violated states.")
    public RefObj<T> setObjAsValid(final T obj) {
        constraintViolations = null;//So that validation is consistent
        this.obj = obj;
        if (this.validate() != 0) {
            this.obj = null;
            throw new ConstraintsViolatedException(this.getViolations());
        }
        return this;
    }

    /**
     * To be used as direct output to client
     *
     * @param e
     * @return
     */
    static public String validationMessages(final List<ConstraintViolation> e) {
        String returnVal = "";
        for (final ConstraintViolation v : e) {
            returnVal += v.getMessage()
                    //+ (v.getErrorCode() != null ? CODE + v.getErrorCode() + CLOSE_SQUARE_BRACKET : "")
                    + "\n";
        }
        return returnVal;
    }

    /**
     * To be used with i18n where messages are values to properties files (localization bundles)
     *
     * @param e
     * @return
     */
    static public List<String> validationMessagesAsList(final List<ConstraintViolation> e) {
        final List<String> returnVal = new ArrayList<String>();
        for (final ConstraintViolation v : e) {
            returnVal.add(v.getMessage());
            //+ (v.getErrorCode() != null ? CODE + v.getErrorCode() + CLOSE_SQUARE_BRACKET : ""));
        }
        return returnVal;
    }

    /**
     * validator is useful if you are using profiling or if you need to avoid lots of validator instances
     * being created
     *
     * @param validator
     * @return 0 if no errors or number of errors
     */
    public int validate(final Validator... validator) {
        final Validator v = validator.length == 0 ? new Validator() : validator[0];
        constraintViolations = v.validate(this);
        return constraintViolations.size();
    }

    /**
     * validator is useful if you are using profiling or if you need to avoid lots of validator instances
     * being created
     *
     * @param validator
     * @return true if valid or false if one or more errors are found
     */
    public boolean valid(final Validator... validator) {
        final Validator v = validator.length == 0 ? new Validator() : validator[0];
        constraintViolations = v.validate(this);
        return constraintViolations.size() == 0;
    }

    /**
     * validator is useful if you are using profiling or if you need to avoid lots of validator instances
     * being created
     *
     * @param validator
     * @return true if invalid or true if one or more errors are found
     */
    public boolean invalid(final Validator... validator) {
        return !valid(validator);
    }

    /**
     * validator is useful if you are using profiling or if you need to avoid lots of validator instances
     * being created
     *
     * @param validator
     * @return 0 if no errors or number of errors
     */
    public T validateThrow(final Validator... validator) {
        final Validator v = validator.length == 0 ? new Validator() : validator[0];
        constraintViolations = v.validate(this);
        if (constraintViolations.size() != 0) {
            throw new ConstraintsViolatedException(constraintViolations);
        }
        return obj;
    }

    /**
     * validator is useful if you are using profiling or if you need to avoid lots of validator instances
     * being created
     *
     * @param validator
     * @return 0 if no errors or number of errors
     */
    public RefObj<T> validateThrowAndGetThis(final Validator... validator) {
        final Validator v = validator.length == 0 ? new Validator() : validator[0];
        constraintViolations = v.validate(this);
        if (constraintViolations.size() != 0) {
            throw new ConstraintsViolatedException(constraintViolations);
        }
        return this;
    }

    public List<ConstraintViolation> getViolations() {
        if (constraintViolations == null) {
            throw new IllegalStateException("SORRY! YOU HAVE NOT CALLED THE validate METHOD YET.");
        }
        return constraintViolations;
    }

    /**
     * There would be no pupose in calling this method if validate returns 0
     *
     * @return
     */
    public List<String> getViolationAsList() {
        if (constraintViolations == null) {
            throw new IllegalStateException("SORRY! YOU HAVE NOT CALLED THE validate METHOD YET.");
        }

        return validationMessagesAsList(constraintViolations);
    }

    /**
     * There would be no pupose in calling this method if validate returns 0
     *
     * @return
     */
    public String getViolationAsString() {
        if (constraintViolations == null) {
            throw new IllegalStateException("SORRY! YOU HAVE NOT CALLED THE validate METHOD YET.");
        }

        return validationMessages(constraintViolations);
    }

    public boolean isNull() {
        return obj == null;
    }

    public boolean notNull() {
        return obj != null;
    }

    @Override
    public String toString() {
        return obj != null ? obj.toString() : "null";
    }

    @Override
    public boolean equals(Object o) {
        throw ExceptionCache.NO_EQUALS_METHOD_EXCEPTION;
    }

    @Override
    public int hashCode() {
        return obj.hashCode();
    }
}
