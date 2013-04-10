package upcoming.yahoo.api.impl;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
public interface Client {
    public String getJsonEndpoint();

    /**
     * Sample response (or visit <a href="http://upcoming.yahoo.com/services/api/event.search.php">http://upcoming.yahoo.com/services/api/event.search.php</a>)
     * <p/>
     * {"rsp" : { "event" : [ { "category_id" : 2,
     * "date_posted" : "2009-01-28 08:42:24",
     * "description" : "",
     * "distance" : 43.100000000000001,
     * "distance_units" : "miles",
     * "end_date" : "",
     * "end_time" : -1,
     * "geocoding_ambiguous" : 0,
     * "geocoding_precision" : "address",
     * "id" : 1673283,
     * "latitude" : 37.762,
     * "longitude" : -122.435,
     * "metro_id" : "",
     * "name" : "Vagina Monologues",
     * "num_future_events" : 0,
     * "personal" : 0,
     * "photo_url" : "",
     * "selfpromotion" : 0,
     * "start_date" : "2009-04-16",
     * "start_date_last_rendition" : "Apr 16, 2009",
     * "start_time" : "19:30:00",
     * "ticket_free" : 0,
     * "ticket_price" : "",
     * "ticket_url" : "http://www.ticketsnow.com/InventoryBrowse/TicketList.aspx?PID=772555",
     * "url" : "http://www.ticketsnow.com/EventList/EventsList.aspx?EID=905",
     * "user_id" : 4,
     * "utc_end" : "2009-04-17 05:30:00 UTC",
     * "utc_start" : "2009-04-17 02:30:00 UTC",
     * "venue_address" : "429 Castro St",
     * "venue_city" : "San Francisco",
     * "venue_country_code" : "US",
     * "venue_country_id" : 1000000,
     * "venue_country_name" : "United States",
     * "venue_id" : 282390,
     * "venue_name" : "Castro Theater",
     * "venue_state_code" : "CA",
     * "venue_state_id" : 1000000,
     * "venue_state_name" : "CA",
     * "venue_zip" : 94114,
     * "watchlist_count" : 0
     * },
     * { "category_id" : 1,
     * "date_posted" : "2009-01-28 09:29:00",
     * "description" : "Carola Zertuche presents music and dance with great performers of traditional flamenco.",
     * "distance" : 43.829999999999998,
     * "distance_units" : "miles",
     * "end_date" : "",
     * "end_time" : -1,
     * "geocoding_ambiguous" : 0,
     * "geocoding_precision" : "address",
     * "id" : 1691425,
     * "latitude" : 37.809600000000003,
     * "longitude" : -122.4106,
     * "metro_id" : "",
     * "name" : "Flamenco Thursdays",
     * "num_future_events" : 0,
     * "personal" : 0,
     * "photo_url" : "",
     * "selfpromotion" : 0,
     * "start_date" : "2009-04-16",
     * "start_date_last_rendition" : "Apr 16, 2009",
     * "start_time" : "20:00:00",
     * "ticket_free" : 0,
     * "ticket_price" : "$12",
     * "ticket_url" : "",
     * "url" : "",
     * "user_id" : 4,
     * "utc_end" : "2009-04-17 06:00:00 UTC",
     * "utc_start" : "2009-04-17 03:00:00 UTC",
     * "venue_address" : "1630 Powell St",
     * "venue_city" : "San Francisco",
     * "venue_country_code" : "us",
     * "venue_country_id" : 1000000,
     * "venue_country_name" : "United States",
     * "venue_id" : 83329,
     * "venue_name" : "Pena Pacha Mama",
     * "venue_state_code" : "CA",
     * "venue_state_id" : 1000000,
     * "venue_state_name" : "California",
     * "venue_zip" : 94133,
     * "watchlist_count" : 0
     * }
     * ],
     * "stat" : "ok",
     * "version" : 1
     * }
     *
     * @param endpointEndValue
     * @param parameters
     * @return
     */
    public JSONObject get(final String endpointEndValue, final Map<String, String> parameters);
}
