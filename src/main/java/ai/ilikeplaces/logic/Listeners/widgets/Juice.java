package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.DOCUMENTATION;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.SEE;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractWidgetListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 2/17/12
 * Time: 6:47 PM
 */
@DOCUMENTATION(
        NOTE = {
                @NOTE("The term JUICE was coined up from Seth Godin's book Unleashing the Idea Virus."),
                @NOTE("The idea is to provide so much positive Juice into a HIVE which we believe can get infected by our idea virus."),
                @NOTE("We have a pitfall though. We have four sections, Talk, Tribes, Moments and Snaps. This needs 4 ideas(or do we?)"),
                @NOTE("Having four ideas might confuse the user."),
                @NOTE("This also gives us Mixed Fruit Juice rather than a fine blend of one:-/."),

                @NOTE("Excitement: http://www.psychologytoday.com/blog/learning-play/200912/the-nature-excitement"),
                @NOTE("Excitement (more depth): http://worldcupcollege.com/2010/06/the-psychology-of-excitement/"),
        }
)
public class Juice extends AbstractWidgetListener<JuiceCriteria> {
    public static enum JuiceIds implements WidgetIds {
        Juice_Content,

        Juice_Talk,
        Juice_Talk_Content,
        Juice_Tribe,
        Juice_Tribe_Content,
        Juice_Moment,
        Juice_Moment_Content,
        Juice_Snap,
        Juice_Snap_Content,
    }


    /**
     * @param request__
     * @param appendToElement__
     */
    public Juice(final ItsNatServletRequest request__, final JuiceCriteria juiceCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.Juice, juiceCriteria, appendToElement__);
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
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
