package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.validators.unit.WallEntry;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.cache.SmartCache;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class WallWidget<T> extends AbstractWidgetListener<T> {

    final static public String PARAM_WALL_ENTRY = "wall_entry";

    final protected WallEntry wallAppend = new WallEntry("");
    public static final String MUTE = "Mute";
    public static final String LISTEN = "Listen";
    protected static final String WALL_SUBMIT_WIDGET = "wall_submit_widget";
    protected static final String ORGANIZE_SECTION = "organize";

    final static public SmartCache<String, Long> HUMANS_WALL_ID = new SmartCache<String, Long>();

    public static enum WallWidgetIds implements WidgetIds {
        /*WallHandler IDs*/
        wallWidget,
        wallContent,
        wallAppend,
        wallSubmit,
        wallNotice,
        wallMute,
        wallTitle,
        wallProfilePhoto,
        wallProfileName,
        wallGame
    }


    public WallWidget(final ItsNatServletRequest request__, final Element appendToElement__, final Object... initArgs) {
        super(request__, Page.WallHandler, appendToElement__, initArgs);

        UCProcessWallText:
        {

            itsNatDocument_.addEventListener((EventTarget) $$(WallWidgetIds.wallAppend), EventType.BLUR.toString(), new EventListener() {

                @Override
                public void handleEvent(final Event evt_) {
                    wallAppend.setObj(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
                    //We are removing validation here since it is understood text is supposed to be entered
                    /*if (wallAppend.validate() == 0) {
                        wallAppend.setObj(wallAppend.getObj());
                        $$clear($$(Page.wallNotice));
                    } else {
                        $$(Page.wallNotice).setTextContent(wallAppend.getViolationAsString());
                        wallAppend.setObj("");
                    }*/
                }

            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
        }
    }

    public WallWidget(final ItsNatServletRequest request__, final T t, final Element appendToElement__) {
        super(request__, Page.WallHandler, t, appendToElement__);

        UCProcessWallText:
        {

            itsNatDocument_.addEventListener((EventTarget) $$(WallWidgetIds.wallAppend), EventType.BLUR.toString(), new EventListener() {

                @Override
                public void handleEvent(final Event evt_) {
                    wallAppend.setObj(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
                    //We are removing validation here since it is understood text is supposed to be entered
                    /*if (wallAppend.validate() == 0) {
                        wallAppend.setObj(wallAppend.getObj());
                        $$clear($$(Page.wallNotice));
                    } else {
                        $$(Page.wallNotice).setTextContent(wallAppend.getViolationAsString());
                        wallAppend.setObj("");
                    }*/
                }

            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
        }
    }


    void $$displayWallAsMuted(final Event evt_, final boolean doMute) {
        $$displayWallAsMuted($$(evt_), doMute);
    }

    void $$displayWallAsMuted(final Element element, final boolean doMute) {
        if (doMute) {
            element.setAttribute(
                    MarkupTag.IMG.src(),
                    element.getAttribute(MarkupTag.IMG.src()).replace("mute.png", "listen.png"));
        } else {
            element.setAttribute(
                    MarkupTag.IMG.src(),
                    element.getAttribute(MarkupTag.IMG.src()).replace("listen.png", "mute.png"));
        }
    }

    void setWallProfilePhoto(final String profilePhotoUrl) {
        $$(WallWidgetIds.wallProfilePhoto).setAttribute(MarkupTag.IMG.title(), profilePhotoUrl);
    }

    void setWallTitle(final String wallTitle) {
        $$(WallWidgetIds.wallTitle).setTextContent(wallTitle);
    }

    void setWallProfileName(final String wallProfileName) {
        $$(WallWidgetIds.wallProfileName).setTextContent(wallProfileName);
    }

    void fetchToEmailSetLeftSidebar(final String leftSidebar) {
        fetchToEmail = fetchToEmail.replace("___|_", leftSidebar);
    }

    void fetchToEmailSetCenter(final String center) {
        fetchToEmail = fetchToEmail.replace("___||_", center);
    }

    void fetchToEmailSetRightSidebar(final String rigtSidebar) {
        fetchToEmail = fetchToEmail.replace("___|||_", rigtSidebar);
    }


    static String fetchToEmailSetLeftSidebar(final String leftSidebar, final String document) {
        return document.replace("___|_", leftSidebar);
    }

    static String fetchToEmailSetCenter(final String center, final String document) {
        return document.replace("___||_", center);
    }

    static String fetchToEmailSetRightSidebar(final String rightSidebar, final String document) {
        return document.replace("___|||_", rightSidebar);
    }
}