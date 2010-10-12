package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.validators.unit.Info;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import net.sf.oval.Validator;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Sep 10, 2010
 * Time: 5:07:59 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class WOEIDGrabber extends AbstractWidgetListener {

    private static final String WOEIDUPDATE_TOKEN = "WOEIDUPDATE_TOKEN";
    private static final String WOEIDGrabberWOEIDUpdate =
            "\nWOEIDGrabberWOEIDUpdate = function(lat,lng){document.getElementById('" + WOEIDUPDATE_TOKEN + "').value = '' + lat + ',' + lng; document.getElementById('" + WOEIDUPDATE_TOKEN + "').focus(); return document.getElementById('" + WOEIDUPDATE_TOKEN + "');}\n";


    private Element elementToUpdateWithWOEID;

    /**
     * @param request__
     * @param appendToElement__
     * @param elementToUpdateWithWOEID
     */
    public WOEIDGrabber(final ItsNatServletRequest request__, final Element appendToElement__, final Element elementToUpdateWithWOEID) {
        super(request__, Controller.Page.WOEIDGrabber, appendToElement__, elementToUpdateWithWOEID);
    }

    @Override
    protected void init(final Object... initArgs) {

        elementToUpdateWithWOEID = (Element) initArgs[0];

        itsNatDocument_.addCodeToSend(
                WOEIDGrabberWOEIDUpdate.replace(
                        WOEIDUPDATE_TOKEN,
                        $$(Controller.Page.WOEIDGrabberWOEID).getAttribute(MarkupTag.GENERIC.id()))
        );
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

        itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.WOEIDGrabberWOEID), EventType.BLUR.toString(), new EventListener() {

            final Validator v = new Validator();
            RefObj<String> woeid;
            final ItsNatDocument ind = itsNatDocument_;

            @Override
            public void handleEvent(final Event evt_) {
//                itsNatDocument_.addCodeToSend(" new google.maps.Marker({ position: new google.maps.LatLng(1,1),map: map,title:'Is it here? if so, click on this marker!!!'});");
//                itsNatDocument_.addCodeToSend(" new google.maps.Marker({ position: new google.maps.LatLng(2,2),map: map,title:'Is it here? if so, click on this marker!!!'});");
//                itsNatDocument_.addCodeToSend(" new google.maps.Marker({ position: new google.maps.LatLng(3,3),map: map,title:'Is it here? if so, click on this marker!!!'});");
//                itsNatDocument_.addCodeToSend(" new google.maps.Marker({ position: new google.maps.LatLng(4,4),map: map,title:'Is it here? if so, click on this marker!!!'});");
//                itsNatDocument_.addCodeToSend(" new google.maps.Marker({ position: new google.maps.LatLng(5,5),map: map,title:'Is it here? if so, click on this marker!!!'});");

                woeid = new Info(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));

                if (woeid.validate(v) == 0) {
                    elementToUpdateWithWOEID.setAttribute(MarkupTag.INPUT.value(), woeid.getObjectAsValid());
                    //
                    // clear($$(PrivateLocationCreateCNotice));
                } else {
//                    $$(PrivateLocationCreateCNotice).setTextContent(woeid.getViolationAsString());
                }
            }

            @Override
            public void finalize() throws Throwable {
                Loggers.finalized(this.getClass().getName());
                super.finalize();
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));


    }
}
