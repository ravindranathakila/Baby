package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.entities.etc.HumansFriend;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.MarkupTag;
import ai.ilikeplaces.util.Save;
import ai.reaver.Return;
import ai.scribble.License;
import ai.scribble.WARNING;
import ai.scribble._note;
import ai.scribble._todo;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Ravindranath Akila
 */

@_todo(task = "Operation fail notification target should be sent in with object creation as parameter so that notifications can be appended")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class MemberHandler<M extends HumansFriend, T extends Collection<HumansFriend>, RETURN_TYPE> extends AbstractWidgetListener {
// ------------------------------ FIELDS ------------------------------

    final static public String Added = " is added";
    final static public String Removed = " is not added";


    private static final String NEGATIVE = "negative";
    private static final String POSITIVE = "positive";
    private static final String STR_ALIGN_POSITIVE = "align_positive";
    private static final String STR_ALIGN_NEGATIVE = "align_negative";

    M m;
    T poss;
    Set<String> existAll;
    Save<RETURN_TYPE> saveAdd;
    Save<RETURN_TYPE> saveRemove;
    final private Logger logger = LoggerFactory.getLogger(MemberHandler.class.getName());

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * @param request__
     * @param appendToElement__
     * @param m
     * @param possibilities
     * @param existingAll
     * @param saveAdd
     * @param saveRemove
     */
    public MemberHandler(
            final ItsNatServletRequest request__, final Element appendToElement__,
            @_note(note = "The member owning everything")
            final M m,
            @_note(note = "The list containing possible adds to be added.")
            final List<? extends HumansFriend> possibilities,
            @_note(note = "The list of existing users contains users this guy does not know.")
            final Collection<? extends HumansFriend> existingAll,
            final Save<RETURN_TYPE> saveAdd,
            final Save<RETURN_TYPE> saveRemove) {
        super(request__, Controller.Page.FriendList, appendToElement__, m, possibilities, existingAll, saveAdd, saveRemove);
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
//            li.setTextContent(possibility.getHuman().getInviterDisplayName() + (existAll.contains(possibility.getHumanId()) ? Added : Removed));
//
//            $$(Controller.Page.FriendListList).appendChild(li);

            final Element div = $$(MarkupTag.DIV);
            div.setAttribute(MarkupTag.DIV.classs(), "vtip");
            div.setAttribute(MarkupTag.DIV.style(), "cursor:pointer;");
            div.setAttribute(MarkupTag.DIV.title(), "Click to Toggle Subscription");
            final boolean isExists = existAll.contains(possibility.getHumanId());
            div.setTextContent(possibility.getDisplayName() + (isExists ? Added : Removed));

            final String existingClasses = "" + div.getAttribute(MarkupTag.GENERIC.classs());
            div.setAttribute(MarkupTag.GENERIC.classs(), existingClasses + " " + (isExists ? POSITIVE : NEGATIVE));

            $$(Controller.Page.FriendListList).appendChild(div);

            final UserProperty userProperty = new UserProperty(request, $$(Controller.Page.FriendListList), new HumanId(possibility.getHumanId())) {
                protected void init(final Object... initArgs) {
                    $$(Controller.Page.user_property_content).appendChild(div);
                }
            };

            $$setClass(userProperty.$$(Controller.Page.user_property_widget), (isExists ? STR_ALIGN_POSITIVE : STR_ALIGN_NEGATIVE), false);


            itsNatHTMLDocument__.addEventListener((EventTarget) div, EventType.CLICK.toString(), new EventListener() {
                Boolean positive = existAll.contains(possibility.getHumanId());
                final UserProperty myuserProperty = userProperty;

                @Override
                public void handleEvent(final Event evt_) {
                    positive = !positive;
                    if (positive) {
                        @WARNING(warning = "Assuming return type to be Return. Check Save as it is generic.")
                        final Return r = (Return) saveAdd.save(new HumanId(m.getHumanId()).getSelfAsValid(), possibility);
                        if (r.returnStatus() == 0) {
                            ((Element) evt_.getCurrentTarget()).setTextContent(possibility.getDisplayName() + Added);
                            final String existingClasses = "" + $$(evt_).getAttribute(MarkupTag.GENERIC.classs());
                            $$(evt_).setAttribute(MarkupTag.GENERIC.classs(), existingClasses.replace(NEGATIVE, "") + " " + POSITIVE);
                            $$setClass(myuserProperty.$$(Controller.Page.user_property_widget), STR_ALIGN_POSITIVE, true);
                        }

                    } else {
                        @WARNING(warning = "Assuming return type to be Return. Check Save as it is generic.")
                        final Return r = (Return) saveRemove.save(new HumanId(m.getHumanId()).getSelfAsValid(), possibility);
                        if (r.returnStatus() == 0) {
                            ((Element) evt_.getCurrentTarget()).setTextContent(possibility.getDisplayName() + Removed);
                            final String existingClasses = "" + $$(evt_).getAttribute(MarkupTag.GENERIC.classs());
                            $$(evt_).setAttribute(MarkupTag.GENERIC.classs(), existingClasses.replace(POSITIVE, "") + " " + NEGATIVE);
                            $$setClass(myuserProperty.$$(Controller.Page.user_property_widget), STR_ALIGN_NEGATIVE, true);
                        }
                    }
                }
            }, false);
        }
    }
}
