package ai.scribble;

import java.lang.annotation.*;

/**
 * Make sure you annotate all methods with this annotation.<br>
 * A Template is given below.<br>
 * <blockquote><pre>
 * \@MethodPreamble(
 * authors = {"Ravindranath Akila"},
 * version = 1,
 * description = {""},
 * conventions = {""},
 * callBackModules = {Class.class},
 * notes = {"Initial Implementation"},
 * TODO =  {"Under Implementation"})
 * </pre></blockquote>
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface _method_preamble {

    /**
     * @return String array of authors
     */
    String[] authors();

    /**
     * @return Version number
     */
    int version() default 0;

    /**
     * @return String array of description
     */
    String[] description();

    /**
     * @return Array of classes which use this method
     */
    Class[] callBackModules();

    /**
     * @return String array of conventions
     */
    String[] conventions() default {"Always specify some convention details"};

    /**
     * @return String array of reviewers
     */
    String[] reviewers() default {"No reviewers"};

    /**
     * @return String array of notes
     */
    String[] notes() default {"No notes"};

    /**
     * @return String array of todo
     */
    String[] TODO() default {"Nothing left TODO"};
}
