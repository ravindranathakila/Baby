package ai.ilikeplaces.entities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 2/6/11
 * Time: 9:42 PM
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RefreshId {
    String value();
}
