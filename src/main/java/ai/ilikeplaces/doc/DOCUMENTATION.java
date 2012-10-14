package ai.ilikeplaces.doc;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 1/24/11
 * Time: 8:54 PM
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface DOCUMENTATION {
    SEE SEE() default @SEE();

    NOTE NOTE() default @NOTE(note = "");

    FIXME FIXME() default @FIXME();

    TODO TODO() default @TODO();

    WARNING WARNING() default @WARNING(warning = "");

    LOGIC LOGIC() default @LOGIC();
}
