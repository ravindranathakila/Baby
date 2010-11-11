package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ai.ilikeplaces.servlets.Controller.Page.pd_photo;
import static ai.ilikeplaces.servlets.Controller.Page.pd_photo_permalink;


/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
public class AlbumManager extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(AlbumManager.class.getName());
    private HumanId humanId = null;
    private Long privateEventId;

    public AlbumManager(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanId__, final long privateEventId) {
        super(request__, Page.Album, appendToElement__, humanId__, privateEventId);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        humanId = ((HumanId) initArgs[0]).getSelfAsValid();
        privateEventId = (Long) initArgs[1];


        setAlbumPhotos:
        {
            final Return<Album> ra = DB.getHumanCrudPrivateEventLocal(true).rPrivateEventReadAlbum(humanId, privateEventId);
            if (ra.returnStatus() == 0) {
                final Album album = ra.returnValue();

                for (final PrivatePhoto privatePhoto__ : album.getAlbumPhotos()) {
                    new Photo$Description(request, $$(Controller.Page.AlbumPhotos)) {

                        @Override
                        protected void init(final Object... initArgs) {
                            final String imageURL = RBGet.globalConfig.getString("ALBUM_PHOTOS") + privatePhoto__.getPrivatePhotoURLPath();
                            $$(pd_photo_permalink).setAttribute("href", imageURL);
                            $$(pd_photo).setAttribute("src", imageURL);
                        }
                    };
                }
            } else {
                $$(Controller.Page.AlbumNotice).setTextContent(ra.returnMsg());
            }
        }


        setAlbumOwnerMode:
        {
            final Return<Boolean> ro = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventIsOwner(humanId, privateEventId);
            if (ro.returnStatus() == 0) {
                if (ro.returnValue()) {//owner
                    $$(Controller.Page.AlbumPivateEventId).setAttribute(MarkupTag.INPUT.value(), privateEventId.toString());
                    displayBlock($$(Controller.Page.AlbumOwner));
                    //show hide operations
                }
            } else {
                $$(Controller.Page.AlbumNotice).setTextContent(ro.returnMsg());
            }
        }


    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.AlbumForward), EventType.CLICK.toString(), new EventListener() {

            final HumanId myhumanId = humanId;
            final Long myprivateEventId = privateEventId;
            boolean confirmed = false;

            @Override
            public void handleEvent(final Event evt_) {
                if (confirmed) {
                    final Return<PrivateEvent> r = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventAsAny(myhumanId.getObj(), myprivateEventId);
                    final Return<Album> ra = DB.getHumanCrudPrivateEventLocal(true).rPrivateEventReadAlbum(myhumanId, myprivateEventId);
                    if (r.returnStatus() == 0) {
                        final PrivateEvent pe = r.returnValue();
                        final Album album = ra.returnValue();
                        Collections.sort(album.getAlbumPhotos());

                        final List<String> listContainingJustLatestPhoto = new ArrayList<String>(1);
                        listContainingJustLatestPhoto.add(RBGet.globalConfig.getString("ALBUM_PHOTOS") + album.getAlbumPhotos().get(0).getPrivatePhotoURLPath());


                        final HumansIdentity hi = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(humanId).returnValue();
                        final StringBuilder b = new StringBuilder("");
                        for (final PrivatePhoto pp : album.getAlbumPhotos()) {
                            b.append(
                                    UserProperty.fetchToEmail(
                                            hi.getHuman().getDisplayName(),
                                            UserProperty.formatProfileUrl(hi.getUrl().getUrl(), true),
                                            UserProperty.formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()),
                                            ElementComposer.compose($$(MarkupTag.IMG)).$ElementSetAttribute(MarkupTag.IMG.src(), RBGet.globalConfig.getString("ALBUM_PHOTOS") + album.getAlbumPhotos().get(0).getPrivatePhotoURLPath()).get()
                                    )
                            );
                        }


                        for (final HumansPrivateEvent member : pe.getPrivateEventInvites()) {
                            SendMail.getSendMailLocal().sendAsHTMLAsynchronously(member.getHumanId(), pe.getPrivateEventName(), b.toString());
                        }
                        for (final HumansPrivateEvent member : pe.getPrivateEventInvites()) {
                            SendMail.getSendMailLocal().sendAsHTMLAsynchronously(member.getHumanId(), pe.getPrivateEventName(), b.toString());
                        }
                        for (final HumansPrivateEvent member : pe.getPrivateEventInvites()) {
                            SendMail.getSendMailLocal().sendAsHTMLAsynchronously(member.getHumanId(), pe.getPrivateEventName(), b.toString());
                        }

                    } else {
                        $$(Controller.Page.AlbumNotice).setTextContent(r.returnMsg());
                    }

                    remove(evt_.getTarget(), EventType.CLICK, this, false);
                    confirmed = false;
                    $$(evt_).setTextContent("Forwarded!");
                } else {
                    confirmed = true;
                    $$(evt_).setTextContent("Click to Confirm!");
                }
            }

            @Override
            public void finalize() throws Throwable {
                Loggers.finalized(this.getClass().getName());
                super.finalize();
            }
        }, false);
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}