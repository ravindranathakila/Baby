package ai.ilikeplaces.doc;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Ravindranath Akila
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface CONVENTION {

    String convention();

    String[] conventions() default {};
}
