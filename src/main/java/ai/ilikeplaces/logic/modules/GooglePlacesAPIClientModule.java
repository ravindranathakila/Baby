package ai.ilikeplaces.logic.modules;

import com.google.places.api.conf.AbstractGooglePlacesAPIClientModule;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 5/2/11
 * Time: 8:32 AM
 */
public class GooglePlacesAPIClientModule extends AbstractGooglePlacesAPIClientModule {

    /**
     * Override this method and return yor GooglePlaces App ID
     *
     * @return GooglePlaces App ID
     */
    @Override
    protected String appKey() {
        return "AIzaSyB6PCBGVcAjf9-OrZnUjSzx5lAfPxavvew";
    }
}


/*

     public static void main(final String[] args) throws JSONException {
        final Injector injector = Guice.createInjector(new GooglePlacesAPIClientModule());
        final ClientFactory clientFactory = injector.getInstance(ClientFactory.class);
        final Client places = clientFactory.getInstance("https://maps.googleapis.com/maps/api/place/search/json");

         final Map<String, String> params = new HashMap<String,String>();
         params.put("location", "-33.8670522,151.1957362");
         params.put("radius", "500");
         params.put("types", "airport%7Camusement_park%7Caquarium%7Cart_gallery%7Catm%7Cbakery%7Cbank%7Cbar%7Cbeauty_salon%7Cbook_store%7Cbowling_alley%7Cbus_station%7Ccafe%7Ccampground%7Ccar_rental%7Ccasino%7Cchurch%7Ccity_hall%7Cclothing_store%7Cembassy%7Cfire_station%7Cflorist%7Cfood%7Cgas_station%7Cgym%7Chair_care%7Chindu_temple%7Cjewelry_store%7Clibrary%7Clodging%7Cmeal_delivery%7Cmeal_takeaway%7Cmosque%7Cmovie_rental%7Cmovie_theater%7Cmuseum%7Cnight_club%7Cpark%7Cparking%7Cpet_store%7Cplace_of_worship%7Cpolice%7Crestaurant%7Crv_park%7Cschool%7Cshoe_store%7Cshopping_mall%7Cspa%7Cstadium%7Csubway_station%7Csynagogue%7Ctaxi_stand%7Ctrain_station%7Ctravel_agency%7Cuniversity%7Czoo");
         params.put("sensor", "true");
         final JSONObject placesJSONObject = places.get("", params);
         System.out.println(placesJSONObject);

     }


 */