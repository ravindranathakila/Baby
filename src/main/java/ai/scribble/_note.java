package ai.scribble;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface _note {

    String[] value() default "";

    @Deprecated String[] note() default "";

    @Deprecated String[] notes() default {};

    @Deprecated String see() default "";

    @Deprecated String mustsee() default "";

    _see SEE() default @_see();
}
