package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.HumanIdFace;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.Arrays;
import java.util.List;

import static ai.ilikeplaces.servlets.Controller.Page.pc_close;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class MemberHandler<M extends HumansFriend, T extends List<HumansFriend>, RETURN_TYPE> extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(MemberHandler.class.getName());

    M m;
    T poss;
    Save<RETURN_TYPE> saveAdd;
    Save<RETURN_TYPE> saveRemove;

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public MemberHandler(final ItsNatDocument itsNatDocument__,
                         final Element appendToElement__,
                         @NOTE(note = "The member owning everything")
                         final M m,
                         @NOTE(note = "The list containing possible adds to be added.")
                         final List<? extends HumansFriend> possibilities,
                         final Save<RETURN_TYPE> saveAdd,
                         final Save<RETURN_TYPE> saveRemove) {
        super(itsNatDocument__, Controller.Page.FriendList, appendToElement__, m, possibilities, saveAdd, saveRemove);
    }


    @Override
    protected void init(final Object... initArgs) {
        m = (M) initArgs[0];
        poss = (T) initArgs[1];
        saveAdd = (Save<RETURN_TYPE>) initArgs[2];
        saveRemove = (Save<RETURN_TYPE>) initArgs[3];

        Loggers.DEBUG.debug("INIT:" + Arrays.toString(poss.toArray()));
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        for (final HumansFriend possibility : poss) {
            final Element li = $$(MarkupTag.LI);
            li.setTextContent(possibility.getHumanId());
            $$(Controller.Page.FriendListList).appendChild(li);
            itsNatHTMLDocument__.addEventListener((EventTarget) li, EventType.click.toString(), new EventListener() {
                Boolean positive = false;

                @Override
                public void handleEvent(final Event evt_) {
                    positive = !positive;
                    if (positive) {
                        saveAdd.save(new HumanId(m.getHumanId()), possibility);
                        ((Element) evt_.getCurrentTarget()).setTextContent(possibility.getHumanId() + " Added");
                    } else {
                        saveRemove.save(new HumanId(m.getHumanId()), possibility);
                        ((Element) evt_.getCurrentTarget()).setTextContent(possibility.getHumanId() + " Removed");
                    }
                }
            }, false);
        }
    }
}