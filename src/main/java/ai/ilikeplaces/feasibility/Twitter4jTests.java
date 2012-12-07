package ai.ilikeplaces.feasibility;

import ai.doc.License;
import twitter4j.*;

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
        final RateLimitStatus r = twitter.getRateLimitStatus();

        System.out.println("Hourly Limit:" + r.getHourlyLimit());
        System.out.println("Hits Left:" + r.getRemainingHits());

        Query query = new Query("fun OR happening OR enjoy OR nightclub OR restaurant OR party OR travel :)");

        final long start = System.currentTimeMillis();
        QueryResult result = twitter.search(query.geoCode(new GeoLocation(6.876754135813088, 79.93997275829315), 20, Query.MILES));
        System.out.println("hits:" + result.getMaxId());
        for (Tweet tweet : result.getTweets()) {
            System.out.println(tweet.getFromUser() + ":" + tweet.getText());
        }
        System.out.println("Time taken:" + (System.currentTimeMillis() - start));
    }
}
