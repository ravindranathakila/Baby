package ai.ilikeplaces.doc;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface BIDIRECTIONAL {
    enum OWNING {
        IS,
        NOT,
        TODO
    }

    public OWNING ownerside() default OWNING.TODO;
}