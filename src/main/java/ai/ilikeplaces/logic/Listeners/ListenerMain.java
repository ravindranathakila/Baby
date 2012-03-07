package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.LongMsg;
import ai.ilikeplaces.logic.Listeners.widgets.SignInOn;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.modules.Modules;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.jpa.RefreshSpec;
import ai.ilikeplaces.ygp.impl.Client;
import ai.ilikeplaces.ygp.impl.ClientFactory;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;
import twitter4j.*;
import where.yahooapis.com.v1.Place;

import java.text.MessageFormat;
import java.util.*;

import static ai.ilikeplaces.servlets.Controller.Page.*;
import static ai.ilikeplaces.util.MarkupTag.*;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@TODO(task = "RENAME TO LISTENERLOCATION. DO A STRING SEARCH ON LISTENERMAIN TO FIND USAGE FIRST. CURRENT SEARCH SHOWS NO ISSUES. REFAC DELAYED TILL NEXT CHECK")
public class ListenerMain implements ItsNatServletRequestListener {


    final static protected String LocationId = RBGet.globalConfig.getString("LOCATIONID");
    private static final String RETURNING_LOCATION = "Returning location ";
    private static final String TO_USER = " to user";
    private static final String WRONG_WOEID_FORMAT = "SORRY! WRONG WOEID FORMAT";
    public static final String NUMBER_OF_PHOTOS_FOR = "Number of photos for ";
    public static final String COLON = ":";
    private static final String HTTP_SESSION_ATTR_LOCATION = "HttpSessionAttr.location";
    private static final String AI_ILIKEPLACES_LOGIC_LISTENERS_LISTENER_MAIN_0004 = "ai.ilikeplaces.logic.Listeners.ListenerMain.0004";
    private static final String AI_ILIKEPLACES_LOGIC_LISTENERS_LISTENER_MAIN_0005 = "ai.ilikeplaces.logic.Listeners.ListenerMain.0005";
    private static final String POST_ID = "id";
    private static final String HTTP_TRAVEL_ILIKEPLACES_COM_INDEX_JSP_CID_317285_PAGE_NAME_HOT_SEARCH_SUBMITTED_TRUE_VALIDATE_CITY_TRUE_CITY = "http://travel.ilikeplaces.com/index.jsp?c" + POST_ID + "=317285&pageName=hotSearch&submitted=true&validateCity=true&city=";
    private static final String QUERIES_FOR_LOCATION = " queries for location ";
    private static final String OF = " of ";
    private static final String AI_ILIKEPLACES_RBS_GUI = "ai.ilikeplaces.rbs.GUI";
    private static final String PROFILE_PHOTOS = "PROFILE_PHOTOS";
    private static final String THIS_IS = "This is ";
    private static final String BACK_TO = "Back to";
    private static final String AT = "At ";
    private static final String YOU_CAN_VISIT = " you can visit";
    private static final String SPACE = " ";
    private static final String PLACE_LIST = "place_list";
    private static final String TRAVEL_TO = "Travel to ";
    private static final String PAGE = "/page/";
    private static final String _OF_ = "_of_";
    private static final String CLICK_TO_EXPLORE = "Click to explore ";
    private static final String VTIP = "vtip";
    private static final String WIDTH = "w" + POST_ID + "th";
    private static final String PX = "110px;";
    private static final String UNLOADING_BODY_TIME_SPENT = "Unloading body. Time spent:";
    private static final String WOEIDPAGE_TITLE = "woe" + POST_ID + "page.title";
    private static final String WOEIDPAGE_DESC = "woe" + POST_ID + "page.desc";
    private static final String COMMA = ",";
    private static final String SIMPLE_LOCATION_NAME = "simpleLocationName";
    private static final ClientFactory YAHOO_GEO_PLANET_FACTORY = Modules.getModules().getYahooGeoPlanetFactory();
    private static final com.disqus.api.impl.ClientFactory DISQUS_API_FACTORY = Modules.getModules().getDisqusAPIFactory();
    private static final String HTTP_DISQUS_COM_API_3_0_THREADS = "http://disqus.com/api/3.0/threads/";
    private static final String HTTP_DISQUS_COM_API_3_0_POSTS = "http://disqus.com/api/3.0/posts/";
    private static final String IDENT_WOEID = POST_ID + "ent:WOEID=";
    private static final String THREAD = "thread";
    private static final String LIST = "list";
    private static final String RESPONSE = "response";
    private static final String ID = POST_ID;
    private static final String NUMBER_OF_ITEMS_AT_DISQUS_IN_THREAD = "Number of Items At Disqus in Thread:";
    private static final String RAW__MESSAGE = "raw_message";
    private static final String ERROR_IN_UC_DISQUS = "Error in UC DISQUS";
    private static final String ERROR_IN_UC_SET_LOGIN_WIDGET = "Error in UC setLoginW" + POST_ID + "get";
    private static final String ERROR_IN_UC_SEO = "Error in UC SEO";
    private static final String ERROR_IN_UC_SIGN_ON_DISPLAY_LINK = "Error in UC signOnDisplayLink";
    private static final String ERROR_IN_UC_SET_PROFILE_PHOTO_LINK = "Error in UC setProfilePhotoLink";
    private static final String ERROR_IN_UC_SET_LOCATION_ID_FOR_JSREFERENCE = "Error in UC setLocationIdForJSReference";
    private static final String ERROR_IN_UC_SHOW_UPLOAD_FILE_LINK = "Error in UC showUploadFileLink";
    private static final String ERROR_IN_UC_SET_LOCATION_NAME_FOR_JSREFERENCE = "Error in UC setLocationNameForJSReference";
    private static final String ERROR_IN_UC_SET_LOCATION_AS_PAGE_TOPIC = "Error in UC setLocationAsPageTopic";
    private static final String ERROR_IN_UC_NO_SUPPORT_FOR_NEW_LOCATIONS = "Error in UC noSupportForNewLocations";

    private static final RefreshSpec REFRESH_SPEC = new RefreshSpec("longMsgs");
    private static final String CREATED_AT = "createdAt";
    private static final String ERROR_IN_UC_SET_PROFILE_LINK = "Error in UC setProfileLink";
    private static final String PIPE = "|";

    final static Twitter TWITTER = new TwitterFactory().getInstance();
    final static Query QUERY = new Query("fun OR happening OR enjoy OR nightclub OR restaurant OR party OR travel :)");
    private static final String AT_SIGN = "@";
    private static final String EMPTY = "";

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {

        new AbstractListener(request__, response__) {

            protected String location;

            protected String superLocation;

            protected Long WOEID;

            /**
             * Initialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            @TODO(task = "If location is not available, it should be added through a w" + POST_ID + "get(or fragment maybe?)")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {

                final SmartLogger sl = SmartLogger.start(
                        Loggers.LEVEL.DEBUG,
                        "Location Page.",
                        60000,
                        null,
                        true
                );


                //this.location = (String) request_.getServletRequest().getAttribute(RBGet.config.getString("HttpSessionAttr.location"));
                getLocationSuperLocation:
                {
                    final String[] attr = ((String) request__.getServletRequest().getAttribute(RBGet.globalConfig.getString(HTTP_SESSION_ATTR_LOCATION))).split("_");
                    location = attr[0];
                    if (attr.length == 3) {
                        superLocation = attr[2];
                    } else {
                        superLocation = EMPTY;
                    }
                    tryLocationId:
                    {
                        try {
                            final String w = request__.getServletRequest().getParameter(Location.WOEID);
                            if (w != null) {
                                WOEID = Long.parseLong(request__.getServletRequest().getParameter(Location.WOEID));
                            }
                        } catch (final NumberFormatException e) {
                            Loggers.USER_EXCEPTION.error(WRONG_WOEID_FORMAT, e);
                        }
                    }
                }

                sl.appendToLogMSG(RETURNING_LOCATION + location + TO_USER);

                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor + JSCodeToSend.FnLocationId + JSCodeToSend.FnLocationName + JSCodeToSend.FnSetTitle);
                final ResourceBundle gUI = ResourceBundle.getBundle(AI_ILIKEPLACES_RBS_GUI);

                logTheQueryLocationPerUser:
                {
                    if (getUsername() != null) {
                        Loggers.USER.info(getUsernameAsValid() + QUERIES_FOR_LOCATION + location + OF + superLocation);
                        sl.appendToLogMSG(getUsernameAsValid());
                    } else {
                        Loggers.NON_USER.info(request__.getServletRequest().getRemoteHost() + QUERIES_FOR_LOCATION + location + OF + superLocation);
                        sl.appendToLogMSG(request__.getServletRequest().getRemoteHost());
                    }
                }

                UCShowPlacesDataFromGooglePlaceAPIIncludingHotelsRestaurants:
                {

                }


                layoutNeededForAllPages:
                {
                    setLoginWidget:
                    {
                        try {
                            new SignInOn(request__, $(Main_login_widget), new HumanId(getUsername()), request__.getServletRequest()) {
                            };
                        } catch (final Throwable t) {
                            sl.l(ERROR_IN_UC_SET_LOGIN_WIDGET, t);
                        }
                    }

                    SEO:
                    {
                        try {
                            setMainTitle:
                            {
                                $(mainTitle).setTextContent(MessageFormat.format(gUI.getString(WOEIDPAGE_TITLE), location));

                            }
                            setMetaDescription:
                            {
                                $(mainMetaDesc).setAttribute(MarkupTag.META.content(), MessageFormat.format(gUI.getString(WOEIDPAGE_DESC), location));
                            }
                            setHotelsLink:
                            {
                                //$(Main_hotels_link).setAttribute(MarkupTag.A.href(), HTTP_TRAVEL_ILIKEPLACES_COM_INDEX_JSP_CID_317285_PAGE_NAME_HOT_SEARCH_SUBMITTED_TRUE_VALIDATE_CITY_TRUE_CITY + location.split(OF)[0].replace("/", SPACE));
                                $(Main_loading_hotels_link).setAttribute(MarkupTag.A.href(), HTTP_TRAVEL_ILIKEPLACES_COM_INDEX_JSP_CID_317285_PAGE_NAME_HOT_SEARCH_SUBMITTED_TRUE_VALIDATE_CITY_TRUE_CITY + location.split(OF)[0].replace("/", SPACE));
                            }
                        } catch (final Throwable t) {
                            sl.l(ERROR_IN_UC_SEO, t);
                        }
                    }
                    signOnDisplayLink:
                    {
                        try {
                            if (getUsername() != null) {
                                $(Main_othersidebar_identity).setTextContent(gUI.getString(AI_ILIKEPLACES_LOGIC_LISTENERS_LISTENER_MAIN_0004) + getUsernameAsValid());
                            } else {
                                $(Main_othersidebar_identity).setTextContent(gUI.getString(AI_ILIKEPLACES_LOGIC_LISTENERS_LISTENER_MAIN_0005) + location);
                            }
                        } catch (final Throwable t) {
                            sl.l(ERROR_IN_UC_SIGN_ON_DISPLAY_LINK, t);
                        }

                    }
                    setProfileLink:
                    {
                        try {
                            if (getUsername() != null) {
                                $(Main_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.Profile.getURL());
                            } else {
                                $(Main_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.signup.getURL());
                            }
                        } catch (final Throwable t) {
                            sl.l(ERROR_IN_UC_SET_PROFILE_LINK, t);
                        }
                    }
                    setProfilePhotoLink:
                    {
                        try {
                            if (getUsername() != null) {
                                /**
                                 * TODO check for db failure
                                 */
                                String url = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansProfilePhoto(new HumanId(getUsernameAsValid())).returnValueBadly();
                                url = url == null ? null : RBGet.globalConfig.getString(PROFILE_PHOTOS) + url;
                                if (url != null) {
                                    $(Main_profile_photo).setAttribute(MarkupTag.IMG.src(), url);
                                    //displayBlock($(Main_profile_photo));
                                    //displayNone($(Main_location_photo));
                                } else {
                                    //displayNone($(Main_profile_photo));
                                    //displayBlock($(Main_location_photo));
                                }
                            } else {
                                //displayNone($(Main_profile_photo));
                                //displayBlock($(Main_location_photo));
                            }
                        } catch (final Throwable t) {
                            sl.l(ERROR_IN_UC_SET_PROFILE_PHOTO_LINK, t);

                        }
                    }
                }

                final Return<Location> r;
                if (WOEID != null) {
                    r = DB.getHumanCRUDLocationLocal(true).doRLocation(WOEID, REFRESH_SPEC);
                } else {
                    r = new ReturnImpl<Location>(ExceptionCache.UNSUPPORTED_OPERATION_EXCEPTION, "Search Unavailable", false);
                    //r = DB.getHumanCRUDLocationLocal(true).dirtyRLocation(location, superLocation);
                }


                if (r.returnStatus() == 0 && r.returnValue() != null) {
                    final Location existingLocation_ = r.returnValue();

                    doTextReplace:
                    {
                        $i18nize(SIMPLE_LOCATION_NAME, existingLocation_.getLocationName());
                    }
                    GEO:
                    {
                        if (existingLocation_.getLocationGeo1() == null || existingLocation_.getLocationGeo2() == null) {
                            final Client ygpClient = YAHOO_GEO_PLANET_FACTORY.getInstance(RBGet.globalConfig.getString("where.yahooapis.com.v1.place"));
                            final Place place = ygpClient.getPlace(existingLocation_.getLocationId().toString());
                            existingLocation_.setLocationGeo1(Double.toString(place.getCoordinates().getY()));
                            existingLocation_.setLocationGeo2(Double.toString(place.getCoordinates().getX()));

                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    DB.getHumanCRUDLocationLocal(true).doULocationLatLng(
                                            new Obj<Long>(existingLocation_.getLocationId()),
                                            new Obj<Double>(place.getCoordinates().getY()),
                                            new Obj<Double>(place.getCoordinates().getX()));
                                }
                            }).run();
                        }
                    }

                    DISQUS:
                    {
                        try {

                            UCIReadDatabaseData:
                            {
                                final List<LongMsg> existingLongMsgs = existingLocation_.getLongMsgs();

                                if (existingLongMsgs == null || existingLongMsgs.size() == 0) {
                                    sl.l("Direct update since exisitng data is zero or null.");
                                    /*start disqus population anyway*/
                                    UCICheckForDisqusData:
                                    {
                                        final JSONObject postJsonObject = getDisqusPosts(WOEID);
                                        final JSONArray threadPosts = postJsonObject.getJSONArray(RESPONSE);
                                        final int numberOfThreadPosts = threadPosts.length();
                                        sl.l(NUMBER_OF_ITEMS_AT_DISQUS_IN_THREAD + numberOfThreadPosts);
                                        UCSaveDataInDatabase:
                                        {
                                            Map<String, String> postsMap = new HashMap<String, String>();
                                            for (int i = 0; i < numberOfThreadPosts; i++) {
                                                final JSONObject threadPost = threadPosts.getJSONObject(i);
                                                sl.l(threadPost.get(CREATED_AT).toString());
                                                sl.l(threadPost.get(CREATED_AT).toString());
                                                postsMap.put(threadPost.getString(POST_ID) + PIPE + threadPost.get(CREATED_AT).toString(), threadPost.toString());
                                                final Element p = $(MarkupTag.P);
                                                p.setTextContent(threadPost.get(CREATED_AT).toString() + PIPE + threadPost.get(RAW__MESSAGE).toString());
                                                $(Main_disqus_thread_data).appendChild(p);
                                            }
                                            sl.l("Attempting to update posts data to database");
                                            DB.getHumanCRUDLocationLocal(false).doULocationComments(WOEID, postsMap, REFRESH_SPEC);
                                            sl.l("Database updated.");
                                        }

                                    }
                                } else {
                                    sl.l("No direct update since there is existing data in database.");
                                    /* 500,000 indexed by Google at 80,000 pages a day is about 1 week  */
                                    UCICheckIfDataNotRefreshedWithinLastWeek:
                                    {
                                        final LongMsg dateCalcPost = existingLongMsgs.get(existingLongMsgs.size() - 1);
                                        sl.l("Date calculation data:" + dateCalcPost.getLongMsgMetadata());
                                        final String date = dateCalcPost.getLongMsgMetadata().split("\\|")[1];//we check if length is NOT zero in IF condition before
                                        final String[] dataArr = date.split("-");

                                        Calendar then = Calendar.getInstance();
                                        then.set(Integer.parseInt(dataArr[0]), Integer.parseInt(dataArr[1]), Integer.parseInt(dataArr[2].substring(0, 2)));
                                        Calendar now = Calendar.getInstance();
                                        final long elapsed = (now.getTimeInMillis() - then.getTimeInMillis()) / 1000 * 60 * 60 * 24;

                                        if (elapsed > 7) {
                                            sl.l("Seven days elapsed since last update");
                                            UCICheckForDisqusData:
                                            {
                                                UCICheckIfPostCountIsLessThanAtDisqusToAvoidDisqusErasingOurDatabase:
                                                {
                                                    final JSONObject postJsonObject = getDisqusPosts(WOEID);
                                                    final JSONArray threadPosts = postJsonObject.getJSONArray(RESPONSE);
                                                    final int numberOfThreadPosts = threadPosts.length();
                                                    sl.l(NUMBER_OF_ITEMS_AT_DISQUS_IN_THREAD + numberOfThreadPosts);

                                                    if (existingLongMsgs.size() < numberOfThreadPosts) {
                                                        UCSaveDataInDatabase:
                                                        {
                                                            Map<String, String> postsMap = new HashMap<String, String>();
                                                            for (int i = 0; i < numberOfThreadPosts; i++) {
                                                                final JSONObject threadPost = threadPosts.getJSONObject(i);
                                                                sl.l(threadPost.get(CREATED_AT).toString());
                                                                sl.l(threadPost.get(CREATED_AT).toString());
                                                                postsMap.put(threadPost.getString(POST_ID) + PIPE + threadPost.get(CREATED_AT).toString(), threadPost.toString());
                                                            }
                                                            DB.getHumanCRUDLocationLocal(false).doULocationComments(WOEID, postsMap, REFRESH_SPEC);
                                                        }
                                                    } else {
                                                        sl.l("Avoiding update since Disqus post count is lower than database post count.");
                                                    }
                                                }
                                            }
                                        } else {
                                            sl.l("Avoiding update since Last update was within seven days.");
                                        }
                                    }

                                    UCAppendingSEODisqusData:
                                    {
                                        for (final LongMsg threadPost : existingLongMsgs) {
                                            final Element p = $(MarkupTag.P);
                                            p.setTextContent(threadPost.getLongMsgMetadata().split("\\|")[1] + PIPE + new JSONObject(threadPost.getLongMsgContent()).get(RAW__MESSAGE));
                                            $(Main_disqus_thread_data).appendChild(p);
                                        }
                                    }

                                }
                            }


                        } catch (final Throwable t) {
                            if (t.getMessage().contains("Got error code:400")) {
                                sl.l(ERROR_IN_UC_DISQUS + COLON + t.getMessage());//We don't want to log the unwanted stack trace. This happens when nobody has posted a message on this page before.
                            } else {
                                sl.l(ERROR_IN_UC_DISQUS, t);
                            }
                        }
                    }

                    SEO:
                    {
                        setMetaGEOData:
                        {
                            $(Main_ICBM).setAttribute(MarkupTag.META.content(), existingLocation_.getLocationGeo1() + COMMA + existingLocation_.getLocationGeo2());
                            $(Main_geoposition).setAttribute(MarkupTag.META.content(), existingLocation_.getLocationGeo1() + COMMA + existingLocation_.getLocationGeo2());
                            $(Main_geoplacename).setAttribute(MarkupTag.META.content(), existingLocation_.getLocationName());
                            $(Main_georegion).setAttribute(MarkupTag.META.content(), existingLocation_.getLocationSuperSet().getLocationName());
                        }
                        setTwitterData:
                        {
                            try {
                                final QueryResult result = TWITTER.search(QUERY.geoCode(new GeoLocation(Double.parseDouble(existingLocation_.getLocationGeo1()), Double.parseDouble(existingLocation_.getLocationGeo2())), 40, Query.MILES));
                                for (Tweet tweet : result.getTweets()) {
                                    final Element p = $(MarkupTag.P);
                                    p.setTextContent(tweet.getText().replace(AT_SIGN, EMPTY));
                                    $(Main_disqus_thread_data).appendChild(p);
                                }
                                if(result.getTweets().size() == 0){
                                    sl.l("No twitter results found");
                                }
                            } catch (final Throwable t) {
                                sl.l("An error occurred during twitter fetch:" + t.getMessage());
                            }
                        }
                    }

                    showUploadFileLink:
                    {
                        try {
                            if (getUsername() != null) {
                                displayBlock($(Main_othersidebar_upload_file_sh));
                            }
                        } catch (final Throwable t) {
                            sl.l(ERROR_IN_UC_SHOW_UPLOAD_FILE_LINK, t);
                        }
                    }

                    setLocationIdForJSReference:
                    {
                        try {
                            final Element hiddenLocationIdInputTag = $(INPUT);
                            hiddenLocationIdInputTag.setAttribute(INPUT.type(), INPUT.typeValueHidden());
                            hiddenLocationIdInputTag.setAttribute(INPUT.id(), JSCodeToSend.LocationId);
                            hiddenLocationIdInputTag.setAttribute(INPUT.value(), existingLocation_.getLocationId().toString());
                            hTMLDocument__.getBody().appendChild(hiddenLocationIdInputTag);
                        } catch (final Throwable t) {
                            sl.l(ERROR_IN_UC_SET_LOCATION_ID_FOR_JSREFERENCE, t);
                        }
                    }

                    setLocationNameForJSReference:
                    {
                        try {
                            final Element hiddenLocationIdInputTag = $(INPUT);
                            hiddenLocationIdInputTag.setAttribute(INPUT.type(), INPUT.typeValueHidden());
                            hiddenLocationIdInputTag.setAttribute(INPUT.id(), JSCodeToSend.LocationName);
                            hiddenLocationIdInputTag.setAttribute(INPUT.value(), existingLocation_.getLocationName() + OF + existingLocation_.getLocationSuperSet().getLocationName());
                            hTMLDocument__.getBody().appendChild(hiddenLocationIdInputTag);
                        } catch (final Throwable t) {
                            sl.l(ERROR_IN_UC_SET_LOCATION_NAME_FOR_JSREFERENCE, t);
                        }
                    }


                    setLocationAsPageTopic:
                    {
                        try {
                            $(Main_center_main_location_title).setTextContent(THIS_IS + existingLocation_.getLocationName() + OF + existingLocation_.getLocationSuperSet());

                            $(Main_location_backlink).appendChild($(P).appendChild(hTMLDocument__.createTextNode(BACK_TO + COLON + SPACE)));
                            $(Main_location_backlink).appendChild(generateSimpleLocationLink(existingLocation_.getLocationSuperSet()));

                            $(Main_location_list_header).appendChild(($(P).appendChild(hTMLDocument__.createTextNode(AT + existingLocation_.getLocationName() + YOU_CAN_VISIT + COLON + SPACE))));

                            for (final Element element : generateLocationLinks(DB.getHumanCRUDLocationLocal(true).doDirtyRLocationsBySuperLocation(existingLocation_))) {
                                $(Main_location_list).appendChild(element);
                                displayBlock($(Main_notice_sh));
                            }

                        } catch (final Throwable t) {
                            sl.l(ERROR_IN_UC_SET_LOCATION_AS_PAGE_TOPIC, t);
                        }

                    }

                    getAndDisplayAllThePhotos:
                    {
//                        List<PublicPhoto> listPublicPhoto = existingLocation_.getPublicPhotos();
//                        sl.appendToLogMSG(NUMBER_OF_PHOTOS_FOR + existingLocation_.getLocationName() + COLON + listPublicPhoto.size());
//
//                        int i = 0;
//                        for (final Iterator<PublicPhoto> it = listPublicPhoto.iterator(); it.hasNext(); i++) {
//                            try {
//                                final PublicPhoto publicPhoto = it.next();
//
//                                //old mode pasted end of class if needed
//                                newMode:
//                                {
//                                    final Element image = $(IMG);
//                                    image.setAttribute(IMG.src(), publicPhoto.getPublicPhotoURLPath());
//                                    image.setAttribute(IMG.alt(), publicPhoto.getPublicPhotoDescription());
//                                    image.setAttribute(IMG.style(), WIDTH + COLON + PX);
//
//                                    final Element link = $(A);
//                                    link.setAttribute(A.href(), publicPhoto.getPublicPhotoURLPath());
//
//                                    link.appendChild(image);
//
//                                    $(Main_yox).appendChild(link);
//                                }
//                            } catch (final Throwable t) {
//                                sl.l("Error in UC getAndDisplayAllThePhotos", t);
//                            }
//                        }
                    }

                } else {
                    noSupportForNewLocations:
                    {
                        try {
//                            $(Main_notice).appendChild(($(P).appendChild(
//                                    hTMLDocument__.createTextNode(RBGet.logMsgs.getString("CANT_FIND_LOCATION")
//                                            + " Were you looking for "
//                                    ))));
//                            for (final Element element : generateLocationLinks(DB.getHumanCRUDLocationLocal(true).dirtyRLikeLocations(location))) {
//                                $(Main_notice).appendChild(element);
//                                displayBlock($(Main_notice_sh));
//                            }
                            NotSupportingLikeSearchTooForNow:
                            {
                                $(Main_notice).appendChild(($(P).appendChild(hTMLDocument__.createTextNode(RBGet.logMsgs.getString("CANT_FIND_LOCATION")))));
                            }
                        } catch (final Throwable t) {
                            sl.l(ERROR_IN_UC_NO_SUPPORT_FOR_NEW_LOCATIONS, t);
                        }
                    }
                }
                sl.complete(Loggers.LEVEL.SERVER_STATUS, Loggers.DONE);
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners
            (
                    final ItsNatHTMLDocument itsNatHTMLDocument__,
                    final HTMLDocument hTMLDocument__,
                    final ItsNatDocument itsNatDocument__) {
//                ItsNatDocument itsNatDoc = itsNatDocument__;
//                Document docEvent = (Document) itsNatDoc;
//
//                MouseEvent mouseEvent = (MouseEvent) itsNatDoc.createEvent("MouseEvent");
//
//                mouseEvent.initMouseEvent("click", true, true, ((DocumentView) itsNatDoc).getDefaultView(), 0,
//                        0, 0, 0, 0, false, false, false, false, (short) 0/*left button*/, null);
//
//                itsNatDoc.dispatchEvent((EventTarget) $(Main_notice), mouseEvent);
            }

            private List<Element> generateLocationLinks(final List<Location> locationList) {

                final ElementComposer UList = ElementComposer.compose($(UL)).$ElementSetAttribute(MarkupTag.UL.id(), PLACE_LIST);

                for (Location location : locationList) {
                    final Element link = $(A);

                    link.setTextContent(TRAVEL_TO + location.getLocationName() + OF + location.getLocationSuperSet().getLocationName());

                    link.setAttribute(A.href(),
                            PAGE
                                    + location.getLocationName()
                                    + _OF_
                                    + location.getLocationSuperSet().getLocationName()
                                    + Parameter.get(Location.WOEID, location.getWOEID().toString(), true));

                    link.setAttribute(A.alt(),
                            PAGE + location.getLocationName() + _OF_ + location.getLocationSuperSet().getLocationName());

                    link.setAttribute(A.title(),
                            CLICK_TO_EXPLORE + location.getLocationName() + OF + location.getLocationSuperSet().getLocationName());

                    link.setAttribute(A.classs(), VTIP);


                    final Element linkDiv = $(DIV);


                    linkDiv.appendChild(link);

                    UList.wrapThis(
                            ElementComposer.compose($(LI)).wrapThis(linkDiv).get()
                    );
                }

                final List<Element> elements = new ArrayList<Element>();
                elements.add(UList.get());
                return elements;

            }

            private Element generateLocationLink(final Location location) {
                final Element link = $(A);
                link.setTextContent(TRAVEL_TO + location.getLocationName() + OF + location.getLocationSuperSet().getLocationName());
                link.setAttribute(A.href(),
                        PAGE
                                + location.getLocationName()
                                + _OF_
                                + location.getLocationSuperSet().getLocationName()
                                + Parameter.get(Location.WOEID, location.getWOEID().toString(), true));

                link.setAttribute(A.alt(),
                        PAGE + location.getLocationName() + _OF_ + location.getLocationSuperSet().getLocationName());
                return link;
            }

            private Element generateSimpleLocationLink(final Location location) {
                final Element link = $(A);
                link.setTextContent(location.getLocationName());
                link.setAttribute(A.href(),
                        PAGE
                                + location.getLocationName()
                                + Parameter.get(Location.WOEID, location.getWOEID().toString(), true));

                link.setAttribute(A.alt(),
                        PAGE + location.getLocationName());
                return link;
            }
        };//Listener
    }

    private JSONObject getDisqusPosts(final long WOEID) throws JSONException {
        final com.disqus.api.impl.Client threads = DISQUS_API_FACTORY.getInstance(HTTP_DISQUS_COM_API_3_0_THREADS);
        final com.disqus.api.impl.Client posts = DISQUS_API_FACTORY.getInstance(HTTP_DISQUS_COM_API_3_0_POSTS);

        final Map<String, String> threadParams = new HashMap<String, String>();
        threadParams.put(THREAD, IDENT_WOEID + WOEID);
        final JSONObject threadJsonObject = threads.get(LIST, threadParams);

        final Map<String, String> postParams = new HashMap<String, String>();
        postParams.put(THREAD, threadJsonObject.getJSONArray(RESPONSE).getJSONObject(0).get(ID).toString());
        final JSONObject postJsonObject = posts.get(LIST, postParams);
        return postJsonObject;
    }
}
