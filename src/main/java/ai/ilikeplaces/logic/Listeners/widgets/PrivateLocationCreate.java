package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Info;
import ai.ilikeplaces.logic.validators.unit.SimpleName;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import net.sf.oval.Validator;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateLocationCreate extends AbstractWidgetListener {

    private static final String SAVING = "Saving...";
    private static final String PLACENAME = "placename";
    private static final String PLACEDETAILS = "placedetails";
    private static final String WOEHINT = "woehint";
    RefObj<String> privateLocationName = null;

    RefObj<String> privateLocationInfo = null;

    GeoCoord woeid = null;

    HumanId humanId = null;

    final private Logger logger = LoggerFactory.getLogger(PrivateLocationCreate.class.getName());
    private static final String SAVING_PRIVATE_LOCATION = "Saving private location";
    private static final String WAS_CREATED = " was created!";

//    private static final String PRIVATE_LOCATION_CREATE_WOEIDUPDATE_TOKEN = "privateLocationCreateWOEIDUpdateToken";
//    private static final String privateLocationCreateWOEIDUpdate =
//            "\nprivateLocationCreateWOEIDUpdate = function(lat,lng){document.getElementById('" + PRIVATE_LOCATION_CREATE_WOEIDUPDATE_TOKEN + "').value = '' + lat + ',' + lng;}\n";

    /**
     * @param request__
     * @param appendToElement__
     * @param humanId__
     */
    public PrivateLocationCreate(final ItsNatServletRequest request__, final Element appendToElement__, final String humanId__) {
        super(request__, Page.PrivateLocationCreate, appendToElement__, humanId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = new HumanId();
        this.woeid = new GeoCoord();
        humanId.setObjAsValid((String) initArgs[0]);

        this.privateLocationName = new SimpleName();

        this.privateLocationInfo = new Info();

        Loggers.DEBUG.debug(((HttpServletRequest) request.getServletRequest()).getHeader("Referer"));

        TryToAutoCreatePrivateLocationFromURL:
        {
            TryToGetPrivateLocationNameHintFromURL:
            {
                final String placename = $$(request, PLACENAME);
                if (placename != null) {
                    this.privateLocationName.setObj(placename);
                }
            }

            TryToGetPrivateLocationDescriptionHintFromURL:
            {
                final String placedetails = $$(request, PLACEDETAILS);
                if (placedetails != null) {
                    this.privateLocationInfo.setObj(placedetails);
                }
            }

            boolean existingLocation = false;

            TryToGetPrivateLocationPositionOnMapFromURL:
            {
                if ($$(request, WOEHINT) != null) {
                    woeid = new GeoCoord().setObj($$(request, WOEHINT));
                    final List<PrivateLocation> privateLocations = DB.getHumanCRUDHumanLocal(false).doDirtyRHumansPrivateLocation(humanId).returnValue().getPrivateLocationsOwned();

                    for (final PrivateLocation privateLocation : privateLocations) {

                        if (privateLocation.getPrivateLocationLatitude() - 0.000133145585763375 < woeid.getObjectAsValid().getLatitude() &&
                                privateLocation.getPrivateLocationLatitude() + 0.000133145585763375 > woeid.getObjectAsValid().getLatitude() &&
                                privateLocation.getPrivateLocationLongitude() - 0.000184401869775 < woeid.getObjectAsValid().getLongitude() &&
                                privateLocation.getPrivateLocationLongitude() + 0.000184401869775 > woeid.getObjectAsValid().getLongitude()) {//Same place


                            itsNatDocument_.addCodeToSend(JSCodeToSend.redirectPageWithURL(
                                    new Parameter(Controller.Page.Organize.getURL())
                                            .append(Controller.Page.DocOrganizeCategory, 2, true)
                                            .append(Controller.Page.DocOrganizeLocation, privateLocation.getPrivateLocationId())
                                            .get()
                            ));
                            existingLocation = true;
                            break;
                        }
                    }

                }
            }

            DoTheCreatingPartAfterValidationPrivateLocationsCriteria:
            {
                if (this.privateLocationName.validate() == 0 && this.privateLocationInfo.validate() == 0 && this.woeid.validate() == 0) {

                    if (!existingLocation) {
                        final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).cPrivateLocation(
                                humanId,
                                privateLocationName,
                                privateLocationInfo,
                                woeid);
                        if (r.returnStatus() == 0) {
                            itsNatDocument_.addCodeToSend(JSCodeToSend.redirectPageWithURL(
                                    new Parameter(Controller.Page.Organize.getURL())
                                            .append(Controller.Page.DocOrganizeCategory, 2, true)
                                            .append(Controller.Page.DocOrganizeLocation, r.returnValue().getPrivateLocationId())
                                            .get()
                            ));//
                        } else {
                            $$(PrivateLocationCreateCNotice).setTextContent(r.returnMsg());
                        }
                    }
                }
            }
        }


        RenderWOEIDGrabber:
        {
            //new WOEIDGrabber(request, $$(privateLocationCreateWOEIDGrabber), $$(privateLocationCreateWOEID));
        }
//        itsNatDocument_.addCodeToSend(
//                privateLocationCreateWOEIDUpdate.replace(
//                        PRIVATE_LOCATION_CREATE_WOEIDUPDATE_TOKEN,
//                        $$(privateLocationCreateWOEID).getAttribute(MarkupTag.GENERIC.id()))
//        );
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {

//        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationCreateName), EventType.BLUR.toString(), new EventListener() {
//
//            final RefObj<String> myprivateLocationName = privateLocationName;
//            final Validator v = new Validator();
//            RefObj<String> name;
//
//            @Override
//            public void handleEvent(final Event evt_) {
//                name = new SimpleName(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
//                logger.debug("{}", name);
//
//                logger.debug("{}", $$(privateLocationCreateWOEID).getAttribute(MarkupTag.INPUT.value()));
//
//                if (name.validate(v) == 0) {
//                    myprivateLocationName.setObj(name.getObj());
//                    $$clear($$(PrivateLocationCreateCNotice));
//                } else {
//                    $$(PrivateLocationCreateCNotice).setTextContent(name.getViolationAsString());
//                }
//            }
//
//        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
//
//        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationCreateInfo), EventType.BLUR.toString(), new EventListener() {
//
//            final RefObj<String> myprivateLocationInfo = privateLocationInfo;
//            final Validator v = new Validator();
//            RefObj<String> info;
//
//            @Override
//            public void handleEvent(final Event evt_) {
//                info = new Info(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
//                logger.debug("{}", info);
//                logger.debug("{}", $$(privateLocationCreateWOEID).getAttribute(MarkupTag.INPUT.value()));
//
//                if (info.validate(v) == 0) {
//                    myprivateLocationInfo.setObj(info.getObj());
//                    clear($$(PrivateLocationCreateCNotice));
//                } else {
//                    $$(PrivateLocationCreateCNotice).setTextContent(info.getViolationAsString());
//                }
//            }
//
//
//        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
//
////        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationCreateWOEID), EventType.BLUR.toString(), new EventListener() {
////
////            final RefObj<String> myprivateLocationInfo = privateLocationInfo;
////            final Validator v = new Validator();
////            RefObj<String> info;
////
////            @Override
////            public void handleEvent(final Event evt_) {
////                info = new Info(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
////                logger.debug("{}", info);
////                logger.debug("{}", $$(privateLocationCreateWOEID).getAttribute(MarkupTag.INPUT.value()));
////
////                if (info.validate(v) == 0) {
////                    myprivateLocationInfo.setObj(info.getObj());
////                    clear($$(PrivateLocationCreateCNotice));
////                } else {
////                    $$(PrivateLocationCreateCNotice).setTextContent(info.getViolationAsString());
////                }
////            }
////
////            @Override
////            public void finalize() throws Throwable {
////                Loggers.finalized(this.getClass().getName());
////                super.finalize();
////            }
////        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
//
//        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationCreateSave), EventType.CLICK.toString(), new EventListener() {
//            @Override
//            public void handleEvent(final Event evt_) {
//                $$(PrivateLocationCreateCNotice).setTextContent(SAVING);
//            }
//        }, false);
//
//        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationCreateSave), EventType.CLICK.toString(), new EventListener() {
//
//            final HumanId myhumanId = humanId;
//            final RefObj<String> myprivateLocationName = privateLocationName;
//            final RefObj<String> myprivateLocationInfo = privateLocationInfo;
//            final Validator v = new Validator();
//            final GeoCoord mywoeid = woeid;
//
//            @Override
//            public void handleEvent(final Event evt_) {
//                final SmartLogger sl;
//                logger.debug("{}", $$(privateLocationCreateWOEID).getAttribute(MarkupTag.INPUT.value()));
//                mywoeid.setObj($$(privateLocationCreateWOEID).getAttribute(MarkupTag.INPUT.value()));
//
//                if (myprivateLocationName.validate(v) == 0 && myprivateLocationInfo.validate(v) == 0 && mywoeid.validate(v) == 0) {
//                    sl = SmartLogger.start(Loggers.LEVEL.DEBUG, SAVING_PRIVATE_LOCATION, 10000, null, true);
//                    final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).cPrivateLocation(
//                            myhumanId,
//                            myprivateLocationName,
//                            myprivateLocationInfo,
//                            mywoeid);
//                    if (r.returnStatus() == 0) {
//                        remove(evt_.getTarget(), EventType.CLICK, this);
//                        $$(PrivateLocationCreateCNotice).setTextContent(myprivateLocationName.getObj() + WAS_CREATED);
//                        itsNatDocument_.addCodeToSend(JSCodeToSend.redirectPageWithURL(
//                                new Parameter(Controller.Page.Organize.getURL())
//                                        .append(Controller.Page.DocOrganizeCategory, 2, true)
//                                        .append(Controller.Page.DocOrganizeLocation, r.returnValue().getPrivateLocationId())
//                                        .get()
//                        ));//
//
//                        sl.complete(Loggers.DONE + r.returnMsg());
//                    } else {
//                        $$(PrivateLocationCreateCNotice).setTextContent(r.returnMsg());
//                        sl.complete(r.returnMsg());
//                    }
//                } else {
//                    if (myprivateLocationName.validate(v) != 0) {
//                        $$(PrivateLocationCreateCNotice).setTextContent(myprivateLocationName.getViolationAsString());
//                    } else if (myprivateLocationInfo.validate(v) != 0) {
//                        $$(PrivateLocationCreateCNotice).setTextContent(myprivateLocationInfo.getViolationAsString());
//                    } else if (mywoeid.validate(v) != 0) {
//                        $$(PrivateLocationCreateCNotice).setTextContent(mywoeid.getViolationAsString());
//                    }
//                }
//            }
//        }, false);
    }
}