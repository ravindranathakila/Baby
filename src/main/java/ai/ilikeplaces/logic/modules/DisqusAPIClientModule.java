package ai.ilikeplaces.logic.modules;

import com.disqus.api.conf.AbstractDisqusAPIClientModule;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 5/2/11
 * Time: 8:32 AM
 */
public class DisqusAPIClientModule extends AbstractDisqusAPIClientModule {

    /**
     * Override this method and return yor Disqus App ID
     *
     * @return Disqus App ID
     */
    @Override
    protected String forumShortName() {
        return "ilikeplaces";
    }

    /**
     * Override this method and return yor Disqus App ID
     *
     * @return Disqus App ID
     */
    @Override
    protected String appSecret() {
        return "vQrQpNyPunth0uglUPgNGUIlxDDMdHXsRiXmLDKiBguFO1sgtQnDbde0xtoxMf9R";
    }
}