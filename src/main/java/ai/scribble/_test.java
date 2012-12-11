package ai.scribble;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link _test} might not exactly be a bug. It could also be a performance problem.
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface _test {
// -------------------------- OTHER METHODS --------------------------

    /**
     * @return the date on which tested to compare against the VCS to see if things changed since
     */
    String date();

    /**
     * @return the outcome of the test
     */
    String result() default "";

    /**
     * @return the scenario of the testing
     */
    String[] scene() default "";

    /**
     * Used in case we need just to add the scenario, but test later
     *
     * @return if the test failed or succeeded.
     */
    boolean status() default true;

    /**
     * @return the related test case class if available
     */
    Class test() default Class.class;
}
