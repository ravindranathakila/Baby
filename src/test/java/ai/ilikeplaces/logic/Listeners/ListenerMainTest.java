package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.logic.modules.DisqusAPIClientModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 5/12/11
 * Time: 7:39 PM
 */
public class ListenerMainTest {

    private com.disqus.api.impl.ClientFactory DISQUS_API_FACTORY;

    @Before
    public void testBeforeDisqusAPICall() throws Exception {
        final Injector disqusApiFactoryInjector = Guice.createInjector(new DisqusAPIClientModule());
        DISQUS_API_FACTORY = disqusApiFactoryInjector.getInstance(com.disqus.api.impl.ClientFactory.class);
    }

    @Test
    public void testDisqusAPICall() throws Exception {
        final com.disqus.api.impl.Client threads = DISQUS_API_FACTORY.getInstance("http://disqus.com/api/3.0/threads/");
        final com.disqus.api.impl.Client posts = DISQUS_API_FACTORY.getInstance("http://disqus.com/api/3.0/posts/");

        final Map<String, String> threadParams = new HashMap<String, String>();
        threadParams.put("thread", "ident:WOEID=24702796");
        final JSONObject threadJsonObject = threads.get("list", threadParams);
        System.out.println(threadJsonObject.toString());
        System.out.println(threadJsonObject.getJSONArray("response").getJSONObject(0).get("id").toString());

        final Map<String, String> postParams = new HashMap<String, String>();
        postParams.put("thread", threadJsonObject.getJSONArray("response").getJSONObject(0).get("id").toString());

        final JSONObject postJsonObject = posts.get("list", postParams);
        final JSONArray threadPosts = postJsonObject.getJSONArray("response");
        final int numberOfThreadPosts = threadPosts.length();


        System.out.println(postJsonObject.toString());
        System.out.println(postJsonObject.getJSONArray("response").getJSONObject(0).get("id").toString());
        System.out.println("Number of Items in Thread:" + numberOfThreadPosts);
        for (int i = 0; i < numberOfThreadPosts; i++) {
            final JSONObject threadPost =  threadPosts.getJSONObject(i);
            System.out.println(threadPost.get("raw_message"));
            System.out.println(threadPost.get("createdAt"));
        }

        UCIReadDataFromDataBase:
        {

        }

        UCICheckIfDataNotRefreshedWithinLastWeek:// 500,000 indexed by Google at 80,000 pages a day is about 1 week
        {

        }

        UCICheckIfPostCountIsLessThanAtDisqusToAvoidDisqusErasingOurDatabase:
        {

        }
    }
}

