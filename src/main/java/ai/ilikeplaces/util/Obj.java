package ai.ilikeplaces.util;

import ai.doc.License;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 24, 2010
 * Time: 1:35:10 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class Obj<T> extends RefObj<T> {
    public Obj() {
    }

    public Obj(final T t) {
        obj = t;
    }

    @IsInvariant
    @NotNull
    @Override
    public T getObj() {
        return obj;
    }

    public RefObj<T> getSelfAsValid(final Validator... validator) {
        return super.getSelfAsValid(validator);
    }
}
