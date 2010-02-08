package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.HumanIdFace;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.MarkupTag;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.List;

import static ai.ilikeplaces.servlets.Controller.Page.pc_close;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class MemberHandler<M extends HumanIdFace, T extends List<M>> extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(MemberHandler.class.getName());

    M m;
    T adds;
    T poss;

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public MemberHandler(final ItsNatDocument itsNatDocument__, final Element appendToElement__,
                         @NOTE(note = "The member owning everything")
                         final M m,
                         @NOTE(note = "The list to which the elements should be added to.")
                         final T adds,
                         @NOTE(note = "The list containing possible adds to be added.")
                         final T possibilities) {
        super(itsNatDocument__, null, appendToElement__, m, adds, possibilities);
    }

    @Override
    protected void init(final Object... initArgs) {
        m = (M) initArgs[0];
        adds = (T) initArgs[1];
        poss = (T) initArgs[2];
        for (final M m : adds) {
            //$$("").setTextContent(m.getHumanId());
        }
        for (final M m : poss) {
            //$$("").setTextContent(m.getHumanId());
        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        for (final M possibility : poss) {
            final Element le = $$(MarkupTag.LI);
            le.setTextContent(possibility.getHumanId());
            $$("TODO Ordered or unordered list").appendChild(le);
            itsNatHTMLDocument__.addEventListener((EventTarget) le, EventType.click.toString(), new EventListener() {
                Boolean positive = false;

                @Override
                public void handleEvent(final Event evt_) {
                    positive = !positive;
                    if (positive) {
                        adds.add(possibility);
                        ((Element) evt_.getCurrentTarget()).setTextContent(possibility.getHumanId() + " Added");
                    } else {
                        adds.remove(possibility);
                        //code to remove here
                        ((Element) evt_.getCurrentTarget()).setTextContent(possibility.getHumanId() + " Removed");
                    }
                }
            }, false);
        }
    }
}