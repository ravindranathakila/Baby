package ai.ilikeplaces.doc;

import java.lang.annotation.*;

/**
 * Make sure you annotate all classes with this annotation.<br>
 * A Template is given below.<br>
 * <blockquote><pre>
 * \@ClassPreamble(
 * authors = {"Ravindranath Akila"},
 * version = 1,
 * description = {""},
 * conventions = {""},
 * implementations={Class.class},
 * notes = {"Initial Implementation"},
 * TODO =  {"Under Progress"})
 * </pre></blockquote>
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface InterfacePreamble {

    /**
     * @return String array of authors
     */
    String[] authors();

    /**
     * @return Version number
     */
    int version();

    /**
     * @return String array of description
     */
    String[] description();

    /**
     * @return String array of conventions
     */
    String[] conventions() default {"Always specify some convention details"};

    /**
     * @return List of classes which implement this interface
     */
    Class[] implementations();

    /**
     * @return List of interfaces which extend this interface
     */
    Class[] extentions();

    /**
     * @return List of IMPORTANT classes which instanciate this class
     */
    Class[] instances() default {};

    /**
     * @return String array of notes
     */
    String[] notes() default {"No notes"};

    /**
     * @return String array of todo
     */
    String[] TODO() default {"Nothing left TODO"};
}
