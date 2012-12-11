package ai.scribble;

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
public @interface _doc {
    _see SEE()[] default @_see();

    _note NOTE()[] default @_note(note = "");

    _fix FIXME()[] default @_fix();

    _todo TODO()[] default @_todo();

    WARNING WARNING()[] default @WARNING(warning = "");

    _logic LOGIC()[] default @_logic();
}
