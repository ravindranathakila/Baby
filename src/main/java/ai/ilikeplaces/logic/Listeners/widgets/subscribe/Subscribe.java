package ai.ilikeplaces.logic.Listeners.widgets.subscribe;

import ai.hbase.HBaseCrudService;
import ai.hbase.RowKey;
import ai.ilikeplaces.entities.GeohashSubscriber;
import ai.ilikeplaces.entities.Subscriber;
import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.Bate;
import ai.ilikeplaces.logic.Listeners.widgets.PrivateLocationCreate;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.ServletActivate;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import ai.reaver.Return;
import ch.hsr.geohash.GeoHash;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 11/27/11
 * Time: 8:07 AM
 */
public class Subscribe extends AbstractWidgetListener<SubscribeCriteria> {
    // ------------------------------ FIELDS ------------------------------


// ------------------------------ FIELDS STATIC --------------------------


    private static final String PASSWORD_DETAILS = "_passwordDetails";

    private static final String URL = "_url";

    private String woehint;

    private String placeName;

    private String placeDetails;


// -------------------------- ENUMERATIONS --------------------------

    /**
     * @param request__
     * @param subscribeCriteria
     * @param appendToElement__
     */
    public Subscribe(final ItsNatServletRequest request__, final SubscribeCriteria subscribeCriteria, final Element appendToElement__) {

        super(request__, Controller.Page.Subscribe, subscribeCriteria, appendToElement__);
    }

// ------------------------ OVERRIDING METHODS ------------------------

    /**
     * Use this only in conjunction with {@link #AbstractWidgetListener(org.itsnat.core.ItsNatServletRequest, ai.ilikeplaces.servlets.Controller.Page, Object, org.w3c.dom.Element)}
     * GENERIC constructor.
     *
     * @param subscribeCriteria
     */
    @Override
    protected void init(final SubscribeCriteria subscribeCriteria) {

        super.registerUserNotifier($$(SubscribeIds.subscribe_noti));

        ShowHideWidgetAreaBasedOnLoggedInState:
        {
            if (subscribeCriteria.getHumanId().valid()) {
                $$displayBlock($$(SubscribeIds.subscribe_signed_in));
            } else {
                $$displayBlock($$(SubscribeIds.subscribe_signed_out));
            }
        }

        GetUrlParametersIfPresent:
        {
            woehint = Subscribe.this.$$(request, PrivateLocationCreate.WOEHINT);
            placeName = Subscribe.this.$$(request, PrivateLocationCreate.PLACENAME);
            placeDetails = Subscribe.this.$$(request, PrivateLocationCreate.PLACEDETAILS);
        }
    }

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the constructor
     *
     * @param itsNatHTMLDocument_
     * @param hTMLDocument_
     */
    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {

        super.registerForInputText($$(SubscribeIds.subscribe_signup_input),
                new AIEventListener<SubscribeCriteria>(criteria) {
                    /**
                     * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
                     *
                     * @param evt fired from client
                     */
                    @Override
                    protected void onFire(Event evt) {

                        criteria.getSignupCriteria().setEmail(new Email(getTargetInputText(evt)));
                    }
                }
        );

        super.registerForClick($$(SubscribeIds.subscribe_signup_click),
                new AIEventListener<SubscribeCriteria>(criteria) {
                    /**
                     * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
                     *
                     * @param evt fired from client
                     */
                    @Override
                    protected void onFire(Event evt) {

                        final HumanId _humanId = criteria.getHumanId();
                        final boolean isUserLoggedOut = _humanId.getObj() == null;

                        if (isUserLoggedOut) {
                            Loggers.debug("isUserLoggedOut:" + isUserLoggedOut);

                            final Email myemail = criteria.getSignupCriteria().getEmail();

                            if (myemail.valid()) {

                                if (!DB.getHumanCRUDHumanLocal(true).doDirtyCheckHuman(myemail.getObj()).returnValue()) {

                                    final String randomPassword = Long.toHexString(Double.doubleToLongBits(Math.random()));

                                    final Return<Boolean> humanCreateReturn = DB.getHumanCRUDHumanLocal(true).doCHuman(
                                            new HumanId().setObjAsValid(myemail.getObj()),
                                            new Password(randomPassword),
                                            myemail);

                                    try {
                                        final String boundingBox = $(Controller.Page.AarrrWOEID).getAttribute(MarkupTag.INPUT.value());
                                        Loggers.debug("LAT LNG:" + boundingBox);
                                        final String[] boundingBoxLatLngs = boundingBox.split(",");
                                        final double latitude = (Double.parseDouble(boundingBoxLatLngs[0]) + Double.parseDouble(boundingBoxLatLngs[2])) / 2;
                                        final double longitude = (Double.parseDouble(boundingBoxLatLngs[1]) + Double.parseDouble(boundingBoxLatLngs[3])) / 2;

                                        final GeoHash _geoHash = GeoHash.withCharacterPrecision(latitude, longitude, 12);
                                        final String rowKey = _geoHash.toBase32() + "-" + myemail.getObjectAsValid();

                                        final Subscriber _subscriber = Subscriber.newBuilder().setEmailId(myemail.getObjectAsValid()).build();
                                        final GeohashSubscriber _geohashSubscriber = GeohashSubscriber.newBuilder().setEmailId(myemail.getObjectAsValid()).setLatitude(latitude).setLongitude(longitude).build();

                                        new HBaseCrudService<GeohashSubscriber>().create(new RowKey() {
                                            @Override
                                            public String getRowKey() {
                                                return rowKey;
                                            }
                                        }, _geohashSubscriber);

                                    } catch (final Throwable e) {
                                        throw new RuntimeException(e);
                                    }

                                    if (humanCreateReturn.valid() && humanCreateReturn.returnValue()) {
                                        UserIntroduction.createIntroData(new HumanId(myemail.getObj()));

                                        final Parameter parameter;
                                        if (woehint != null && placeName != null && placeDetails != null) {
                                            parameter = new Parameter("http://www.ilikeplaces.com/page/_org?category=143")
                                                    .append(PrivateLocationCreate.WOEHINT,
                                                            woehint)
                                                    .append(PrivateLocationCreate.PLACENAME,
                                                            placeName)
                                                    .append(PrivateLocationCreate.PLACEDETAILS,
                                                            placeDetails);
                                        } else {
                                            parameter = new Parameter("http://www.ilikeplaces.com/page/_org?category=143");
                                        }

                                        final String activationURL;
                                        try {
                                            activationURL = new Parameter("http://www.ilikeplaces.com/" + "activate")
                                                    .append(ServletLogin.Username, myemail.getObj(), true)
                                                    .append(ServletLogin.Password,
                                                            DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(myemail.getObj()))
                                                                    .returnValue()
                                                                    .getHumanAuthenticationHash())
                                                    .append(ServletActivate.NEXT, URLEncoder.encode(parameter.toURL(), "UTF8"))
                                                    .get();
                                        } catch (UnsupportedEncodingException e) {
                                            throw new RuntimeException(e);
                                        }


                                        String htmlBody = Bate.getHTMLStringForOfflineFriendInvite("I Like Places", myemail.getObj());

                                        htmlBody = htmlBody.replace(URL, activationURL);
                                        htmlBody = htmlBody.replace(PASSWORD_DETAILS, "Your temporary password is " + randomPassword + " .");


                                        try {
                                            final String frame = HTMLDocParser.getDocumentAsString(Controller.getPagesPath() + Controller.EMAIL_FRAME);

                                            final String fullEmail = frame.replace("_FrameContent_", htmlBody);

                                            SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                                    myemail.getObj(),
                                                    "Thank God it's Friday!",
                                                    fullEmail);

                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        } catch (SAXException e) {
                                            throw new RuntimeException(e);
                                        } catch (TransformerException e) {
                                            throw new RuntimeException(e);
                                        }

                                        $$displayNone($$(SubscribeIds.subscribe_signup_section));

                                        Subscribe.this.notifyUser("Great! Check your email now!");

                                        $$sendJSStmt(JSCodeToSend.redirectPageWithURL(Controller.Page.Activate.getURL()));
                                    } else {
                                        Subscribe.this.notifyUser("Email INVALID!");
                                    }
                                } else {
                                    Subscribe.this.notifyUser("This email is TAKEN!:(");
                                }

                            } else {
                                Subscribe.this.notifyUser("Email seems to be wrong :-(");
                            }
                        } else {
                            Loggers.debug("isUserLoggedOut:" + isUserLoggedOut);

                            final String boundingBox = $(Controller.Page.AarrrWOEID).getAttribute(MarkupTag.INPUT.value());
                            Loggers.debug("LAT LNG:" + boundingBox);
                            final String[] boundingBoxLatLngs = boundingBox.split(",");
                            final double latitude = (Double.parseDouble(boundingBoxLatLngs[0]) + Double.parseDouble(boundingBoxLatLngs[2])) / 2;
                            final double longitude = (Double.parseDouble(boundingBoxLatLngs[1]) + Double.parseDouble(boundingBoxLatLngs[3])) / 2;

                            final GeoHash _geoHash = GeoHash.withCharacterPrecision(latitude, longitude, 12);
                            final String rowKey = _geoHash.toBase32() + "-" + _humanId.getObjectAsValid();

                            final Subscriber _subscriber = Subscriber.newBuilder().setEmailId(_humanId.getObjectAsValid()).build();
                            final GeohashSubscriber _geohashSubscriber = GeohashSubscriber.newBuilder().setEmailId(_humanId.getObjectAsValid()).setLatitude(latitude).setLongitude(longitude).build();

                            new HBaseCrudService<GeohashSubscriber>().create(new RowKey() {
                                @Override
                                public String getRowKey() {
                                    return rowKey;
                                }
                            }, _geohashSubscriber);

                            Subscribe.this.notifyUser("Great! You'll get updates of this place every Friday!");

                        }

                    }
                }
        );
    }

    public static enum SubscribeIds implements WidgetIds {
        subscribe_widget,
        subscribe_noti,
        subscribe_signed_in,
        subscribe_signed_out,
        subscribe_signup_section,
        subscribe_signup_input,
        subscribe_signup_click,
    }
}
