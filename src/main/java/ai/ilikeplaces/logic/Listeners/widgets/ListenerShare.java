package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.logic.Listeners.AbstractSkeletonListener;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.ElementComposer;
import ai.ilikeplaces.util.ExceptionCache;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MarkupTag;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.pd_photo;
import static ai.ilikeplaces.servlets.Controller.Page.pd_photo_permalink;

/**
 * This page is mainly intended to show shares from external sites within it's content.
 * <p/>
 * This means signing up encouragement is also important.
 *
 * @author Ravindranath Akila
 */
@WARNING(warning = "Remember, this shows profiles of other users to the current user. Might impose serious privacy issues if " +
        "not handled with utmost care")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerShare implements ItsNatServletRequestListener {
    private static final String TITLE = "title";
    private static final String LET_S_GET_YOU_SOCIALIZED = RBGet.gui().getString("LETS_GET_YOU_SOCIALIZED");
    private static final String URL = "url";
    private static final String TYPE = "type";
    private static final String SORRY_I_FAILED_TO_DISPLAY_THIS_SHARE = "SORRY! I FAILED TO DISPLAY THIS SHARE:";
    private static final String YIKES_SOMETHING_WENT_WRONG = "YIKES_SOMETHING_WENT_WRONG";
    private static final String PROFILE_PHOTO_DEFAULT = "PROFILE_PHOTO_DEFAULT";
    private static final String LOADING = "Loading...";

    public enum ShareType {
        PHOTO
    }

    /**
     * @param request__
     * @param response__
     */
    @WARNING(warning = "Remember, this shows profiles of other users to the current user. Might impose serious privacy issues if " +
            "not handled with utmost care")
    @Override
    public void processRequest(final ItsNatServletRequest request__,
                               final ItsNatServletResponse response__) {

        new AbstractSkeletonListener(request__) {

            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
                super.init(itsNatHTMLDocument__, hTMLDocument__, itsNatDocument__, initArgs);

                try {
                    final ShareType type;
                    ExtractShareCategory:
                    {
                        type = ShareType.valueOf(request__.getServletRequest().getParameter(TYPE));
                    }


                    switch (type) {

                        case PHOTO:
                            SetTitle:
                            {
                                //final String title = request__.getServletRequest().getParameter(TITLE);
                                super.setTitle(LET_S_GET_YOU_SOCIALIZED);//Not using custom title because it can be used for spamming :-)
                            }

                            AppendPhoto:
                            {
                                final String url = request__.getServletRequest().getParameter(URL);
//                                $(Controller.Page.Skeleton_center_content).appendChild(
//                                        ElementComposer.compose(
//                                                $(MarkupTag.IMG))
//                                                .$ElementSetAttribute("src", url != null ? url : RBGet.globalConfig.getString("PROFILE_PHOTO_DEFAULT"))
//                                                .$ElementSetAlt("Loading...")
//                                                .$ElementSetClasses("span-14 last")
//                                                .get()
//                                );

                                new Photo$Description(request__, $(Controller.Page.Skeleton_center_content)) {

                                    @Override
                                    protected void init(final Object... initArgs) {
                                        final String imageURL =  url != null ? url : RBGet.globalConfig.getString(PROFILE_PHOTO_DEFAULT);
                                        $$(pd_photo).setAttribute(MarkupTag.IMG.alt(), LOADING);
                                        $$(pd_photo).setAttribute(MarkupTag.IMG.title(), imageURL);
                                        $$(pd_photo).setAttribute(MarkupTag.IMG.src(), imageURL);//External sites will not support ajax
                                    }
                                };

                            }

                            break;
                        default:
                            throw ExceptionCache.UNSUPPORTED_SWITCH;
                    }
                } catch (final Throwable t) {
                    try {
                        $(Controller.Page.Skeleton_notice).setTextContent(RBGet.gui().getString(YIKES_SOMETHING_WENT_WRONG));
                        displayBlock($(Controller.Page.Skeleton_notice_sh));
                    } finally {
                        Loggers.EXCEPTION.error(SORRY_I_FAILED_TO_DISPLAY_THIS_SHARE, t);
                    }
                }

            }
        };
    }
}