package ai.ilikeplaces.util;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 10:08:49 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface ReturnParamsFace {

    public ReturnParamsFace add(final Object... obj);

    public ReturnParamsFace set(final int index, final Object... obj);

    public Object get(final int index);
}
