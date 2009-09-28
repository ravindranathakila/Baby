package ai.ilikeplaces.doc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Make sure you annotate all classes with this annotation.<br>
 * A Template is given below.<br>
 * <blockquote><pre>
\@ClassPreamble(
    authors = {"Ravindranath Akila"},
    version = 1,
    description = {""},
    conventions = {""},
    notes = {"Initial Implementation"},
    TODO =  {"Under Progress"})
    </pre></blockquote>
 *
 * @author Ravindranath Akila
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface FieldPreamble {

    /**
     *
     * @return String array of description
     */
    String[] description();

    /**
     *
     * @return String array of conventions
     */
    String[] conventions() default {"Always specify some convention details"};

    /**
     *
     * @return List of IMPORTANT classes which instanciate this class
     */
    Class[] instances() default {};

    /**
     *
     * @return String array of notes
     */
    String[] notes() default {"No notes"};

    /**
     *
     * @return String array of todo
     */
    String[] TODO() default {"Nothing left TODO"};
}
