package ai.scribble;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to specify a convention.
 * <p/>
 * <p/>
 * <p/>
 * <b>A list of common conventions are enlisted below</b>
 * <p/>
 * <p/>
 * <b>"operator"</b> variable name is used to indicate the person action on the specific task.
 * <p/>
 * For example, if the method signature is addEntry(final Human operator, final String entry) this means operator is the human
 * <p/>
 * who did the addition.
 * <p/>
 * <p/>
 * <p/>
 * <b>"_" and "__"</b> are used to indicate the scope of the variable.
 * <p/>
 * _ indicates the variable is a instance variable.
 * <p/>
 * __ indicates the variable is a local variable or parameter.
 * <p/>
 * <p/>
 * <b>"instanceVariable_"</b> has one underscore(variables defined outside methods in a class. Usually private.)
 * <p/>
 * <b>"parameterOrLocalVariable__"</b> has two underscores(variables defined inside a method constructor or block, or arguments received.)
 * <p/>
 * <b>"protectedOrGlobalVariable"</b> has no underscores
 * <p/>
 * <b>"STATIC_VARIABLES_PUBLIC_OR_PRIVATE"</b> are in all caps and have no underscores
 * <p/>
 * While this variable practice might have been abandoned, it is better to stick to it whenever possible, and minimally
 * for parameterOrLocalVariable__'s.
 * <p/>
 * <p/>
 * <p/>
 * <b>In case of JavaBeans you should avoid underscores except in static and protected context.</b>
 * <p/>
 * <b>Declare variables final whenever possible. While this does ensure safety, it also leaves some space for compiler
 * optimization</b>
 * <p/>
 * <p/>
 * <i>Trailing underscores instead of preceding underscores were chosen to simplify intelli-sense features.
 * <p/>
 * Initially this was also intended to avoid conflict in JavaBeans but is not used in javabeans now.</i>
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface _convention {

    String convention();

    String[] conventions() default {};
}

