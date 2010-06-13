package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.HumansFriend;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Ravindranath Akila
 */

@TODO(task = "Operation fail notification target should be sent in with object creation as parameter so that notifications can be appended")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class MemberHandler<M extends HumansFriend, T extends List<HumansFriend>, RETURN_TYPE> extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(MemberHandler.class.getName());
    final static public String Added = " is added";
    final static public String Removed = " is not added";

    M m;
    T poss;
    Set<String> existAll;
    Save<RETURN_TYPE> saveAdd;
    Save<RETURN_TYPE> saveRemove;

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     * @param m
     * @param possibilities
     * @param existingAll
     * @param saveAdd
     * @param saveRemove
     */
    public MemberHandler(final ItsNatDocument itsNatDocument__,
                         final Element appendToElement__,
                         @NOTE(note = "The member owning everything")
                         final M m,
                         @NOTE(note = "The list containing possible adds to be added.")
                         final List<? extends HumansFriend> possibilities,
                         @NOTE(note = "The list of existing users contains users this guy does not know.")
                         final List<? extends HumansFriend> existingAll,
                         final Save<RETURN_TYPE> saveAdd,
                         final Save<RETURN_TYPE> saveRemove) {
        super(itsNatDocument__, Controller.Page.FriendList, appendToElement__, m, possibilities, existingAll, saveAdd, saveRemove);
    }


    @Override
    protected void init(final Object... initArgs) {
        m = (M) initArgs[0];
        poss = (T) initArgs[1];

        existAll = new HashSet<String>();
        for (final HumansFriend f : (T) initArgs[2]) {
            existAll.add(f.getHumanId());
        }

        saveAdd = (Save<RETURN_TYPE>) initArgs[3];
        saveRemove = (Save<RETURN_TYPE>) initArgs[4];
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        for (final HumansFriend possibility : poss) {

//            final Element li = $$(MarkupTag.LI);
//            li.setTextContent(possibility.getHuman().getDisplayName() + (existAll.contains(possibility.getHumanId()) ? Added : Removed));
//
//            $$(Controller.Page.FriendListList).appendChild(li);

            final Element li = $$(MarkupTag.DIV);
            li.setAttribute(MarkupTag.DIV.classs(),"btn");
            li.setTextContent(possibility.getHuman().getDisplayName() + (existAll.contains(possibility.getHumanId()) ? Added : Removed));

            $$(Controller.Page.FriendListList).appendChild(li);

            new UserProperty(itsNatDocument_, $$(Controller.Page.FriendListList), new HumanId(possibility.getHumanId())) {
                protected void init(final Object... initArgs) {
                    $$(Controller.Page.user_property_content).appendChild(li);
                }
            };

            itsNatHTMLDocument__.addEventListener((EventTarget) li, EventType.CLICK.toString(), new EventListener() {
                Boolean positive = existAll.contains(possibility.getHumanId());

                @Override
                public void handleEvent(final Event evt_) {
                    positive = !positive;
                    if (positive) {
                        @WARNING(warning = "Assuming return type to be Return. Check Save as it is generic.")
                        final Return r = (Return) saveAdd.save(new ai.ilikeplaces.logic.validators.unit.HumanId(m.getHumanId()), possibility);
                        if (r.returnStatus() == 0) {
                            ((Element) evt_.getCurrentTarget()).setTextContent(possibility.getHuman().getDisplayName() + Added);
                        }
                    } else {
                        @WARNING(warning = "Assuming return type to be Return. Check Save as it is generic.")
                        final Return r = (Return) saveRemove.save(new ai.ilikeplaces.logic.validators.unit.HumanId(m.getHumanId()), possibility);
                        if (r.returnStatus() == 0) {
                            ((Element) evt_.getCurrentTarget()).setTextContent(possibility.getHuman().getDisplayName() + Removed);
                        }
                    }
                }

                @Override
                public void finalize() throws Throwable {
                    Loggers.finalized(this.getClass().getName());
                    super.finalize();
                }
            }, false);
        }
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}