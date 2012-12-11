package ai.scribble;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Ravindranath Akila
 */

@Documented
@Retention(RetentionPolicy.SOURCE)
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public @interface License {

    String content() default "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3";
}
