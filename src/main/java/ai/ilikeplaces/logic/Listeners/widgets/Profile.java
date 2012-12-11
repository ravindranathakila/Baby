package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.ProfileUrl;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.filters.ProfileRedirect;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.MarkupTag;
import ai.reaver.HumanId;
import ai.reaver.Return;
import ai.scribble.License;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class Profile extends AbstractWidgetListener {
// ------------------------------ FIELDS ------------------------------

    HumanId humanId = null;
    ProfileUrl url = null;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * @param request__
     * @param appendToElement__
     * @param humanId
     */
    public Profile(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanId) {
        super(
                request__,
                Page.ProfileWidget,
                appendToElement__,
                humanId
        );
    }

    @Override
    protected void init(final Object... initArgs) {
        this.humanId = (HumanId) initArgs[0];
        this.url = new ProfileUrl(null);
        $$(Page.ProfileURL).setAttribute(
                MarkupTag.INPUT.value(),
                DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPublicURL(humanId).returnValueBadly()
        );
    }

    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        itsNatHTMLDocument_.addEventListener((EventTarget) $$(Page.ProfileURL), EventType.CHANGE.toString(), new EventListener() {
            final HumanId myhumanId = humanId;
            final ProfileUrl myurl = url;

            @Override
            public void handleEvent(final Event evt_) {
                myurl.setObj($$(evt_).getAttribute(MarkupTag.TEXTAREA.value()));
                if (myurl.validate() != 0) {
                    $$(Page.ProfileNotice).setTextContent(myurl.getViolationAsString());
                    myurl.setObj(null);//Moving this before calling getviolations will throw an exception
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));


        itsNatHTMLDocument_.addEventListener((EventTarget) $$(Page.ProfileURLUpdate), EventType.CLICK.toString(), new EventListener() {
            final HumanId myhumanId = humanId;
            final ProfileUrl myurl = url;

            @Override
            public void handleEvent(final Event evt_) {
                if (myurl.validate() == 0) {
                    final String oldUrl = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansPublicURL(myhumanId).returnValueBadly();

                    if (myurl.getObj().equals(oldUrl)) {
                        $$(Page.ProfileNotice).setTextContent("Please enter a different value");
                    } else {
                        if (DB.getHumanCRUDHumanLocal(true).doDirtyProfileFromURL(myurl.getObj()).returnValue() != null) {
                            $$(Page.ProfileNotice).setTextContent("That's already taken :-(");
                        } else {
                            final Return<HumansIdentity> humansIdentityReturn = DB.getHumanCRUDHumanLocal(true).doUHumansPublicURL(myhumanId, myurl.getObjectAsValid());
                            if (!humansIdentityReturn.valid()) {
                                $$(Page.ProfileNotice).setTextContent("Something went wrong :-(");
                            } else {
                                final HumansIdentity humansIdentity = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(myhumanId).returnValue();
                                UserProperty.HUMANS_IDENTITY_CACHE.invalidate(humansIdentity.getHumanId());
                                $$(Page.ProfileNotice).setTextContent("Updated. Please wait while you are taken to the new address...");
                                $$sendJS(JSCodeToSend.redirectPageWithURL(ProfileRedirect.PROFILE_URL + humansIdentity.getUrl().getUrl()));
                            }
                        }
                    }
                } else {
                    $$(Page.ProfileNotice).setTextContent(myurl.getViolationAsString());
                    myurl.setObj(null);//Moving this before calling getviolations will throw an exception
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
    }
}
