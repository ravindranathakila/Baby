package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.validators.unit.WallEntry;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MarkupTag;
import org.itsnat.core.ItsNatDocument;
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
abstract public class WallWidget extends AbstractWidgetListener {

    final static public String PARAM_WALL_ENTRY = "wall_entry";

    final protected WallEntry wallAppend = new WallEntry("");
    public static final String MUTE = "Mute";
    public static final String LISTEN = "Listen";
    protected static final String WALL_SUBMIT_WIDGET = "wall_submit_widget";
    protected static final String ORGANIZE_SECTION = "organize";


    public WallWidget(final ItsNatServletRequest request__,  final Element appendToElement__, final Object... initArgs) {
        super(request__, Page.WallHandler, appendToElement__, initArgs);

        UCProcessWallText:
        {

            itsNatDocument_.addEventListener((EventTarget) $$(Page.wallAppend), EventType.BLUR.toString(), new EventListener() {

                @Override
                public void handleEvent(final Event evt_) {
                    wallAppend.setObj(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
                    if (wallAppend.validate() == 0) {
                        wallAppend.setObj(wallAppend.getObj());
                        $$clear($$(Page.wallNotice));
                    } else {
                        $$(Page.wallNotice).setTextContent(wallAppend.getViolationAsString());
                        wallAppend.setObj("");
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


    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}