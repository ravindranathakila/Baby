package ai.ilikeplaces.feasibility;

import ai.scribble.License;
import twitter4j.*;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Oct 29, 2010
 * Time: 7:05:16 AM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class Twitter4jTests {
    public static void main(final String args[]) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        final Map<String, RateLimitStatus> r = twitter.getRateLimitStatus();

        Query query = new Query("fun OR happening OR enjoy OR nightclub OR restaurant OR party OR travel :)");

        final long start = System.currentTimeMillis();
        QueryResult result = twitter.search(query.geoCode(new GeoLocation(6.876754135813088, 79.93997275829315), 20, Query.MILES));
        System.out.println("hits:" + result.getMaxId());
        for (Status tweet : result.getTweets()) {
            System.out.println(tweet.getUser() + ":" + tweet.getText());
        }
        System.out.println("Time taken:" + (System.currentTimeMillis() - start));
    }
}
