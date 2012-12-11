package ai.scribble;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates field will not be null generally
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface _forget_null {

    public abstract String hint() default "A null value is not logically possible here";
}
