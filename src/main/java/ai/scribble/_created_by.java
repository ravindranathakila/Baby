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
public @interface _created_by {
    public abstract Class[] who() default {};

    public String note() default "";
}
