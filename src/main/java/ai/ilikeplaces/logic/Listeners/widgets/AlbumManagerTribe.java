package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.DOCUMENTATION;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.logic.Listeners.widgets.carousel.Carousel;
import ai.ilikeplaces.logic.Listeners.widgets.carousel.CarouselCriteria;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.jpa.RefreshSpec;
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


/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@DOCUMENTATION(
        WARNING = @WARNING("As of 2011-02-04 the private photo widget, if appended dynamically, will thow a javascript error on client side." +
                "This issue was not fixed despite much effort.")
)
public class AlbumManagerTribe extends AbstractWidgetListener {
// ------------------------------ FIELDS ------------------------------

    private static final String ALBUM__PHOTOS = "ALBUM_PHOTOS";
    private static final String BUTTONTEXT_EMAIL_FORWARDED = "buttontext.email.forwarded";
    private static final String BUTTONTEXT_CLICK_TO_CONFIRM = "buttontext.click.to.confirm";
    private static final String SLASH = "/";
    private static final String NO_PHOTOS_IN_ALBUM = "No photos in album!";
    //private Return<Tribe> tribeReturn = null;

    final static private RefreshSpec REFRESH_SPEC_INIT = new RefreshSpec("albumPhotos");
    final static private RefreshSpec REFRESH_SPEC_REGISTER = new RefreshSpec("albumPhotos");

    List<HumansIdentity> wallProspects;
    final private Logger logger = LoggerFactory.getLogger(AlbumManagerTribe.class.getName());
    private HumanId humanId = null;
    private Tribe tribe;
    private HumansIdentity humansIdentity;
    private Return<Album> albumReturn;

    public static enum AlbumManagerTribeIds implements WidgetIds {
        AlbumTribeNotice,
        AlbumTribeTribeId,
        AlbumTribeOwner,
        AlbumTribeForwardSection,
        AlbumTribeForward,
        AlbumTribePhotos,
        AlbumTribeWidget,
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public AlbumManagerTribe(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanId__, final Tribe tribe) {
        super(request__, Page.AlbumTribe, appendToElement__, humanId__, tribe);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        humanId = ((HumanId) initArgs[0]).getSelfAsValid();
        tribe = (Tribe) initArgs[1];
        humansIdentity = UserProperty.HUMANS_IDENTITY_CACHE.get(humanId.getHumanId(), "");
        //tribeReturn = DB.getHumanCrudTribeLocal(true).dirtyRTribeAsAny(humanId.getObj(), tribe.getTribeId());

        final List<Email> emails = new ArrayList<Email>(tribe.getTribeMembers().size());

        for (final HumansTribe humansTribe : tribe.getTribeMembers()) {
            emails.add(new Email(humansTribe.getHumanId()));
        }

        wallProspects = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentitiesByEmails(emails);


        setAlbumPhotos:
        {
            albumReturn = DB.getHumanCRUDTribeLocal(true).rTribeReadAlbum(humanId, new VLong(tribe.getTribeId()), REFRESH_SPEC_INIT);
            if (albumReturn.returnStatus() == 0) {
                final Album album = this.albumReturn.returnValue();

                int photoSequenceNumber = 1;

                final List<PrivatePhoto> albumPhotos = album.getAlbumPhotos();

                UCHideAlbumForwardIfNoPhotos:
                {
                    if (albumPhotos.size() == 0) {
                        $$displayNone(AlbumManagerTribe.AlbumManagerTribeIds.AlbumTribeForwardSection);
                    }

                    if (tribe.getTribeMembers().size() == 1) {
                        $$displayNone(AlbumManagerTribeIds.AlbumTribeWidget);
                    }
                }

                new Carousel(request, new CarouselCriteria().setAlbumPhotos(albumPhotos), $(Page.Skeleton_right_column));

                final List<Long> albumPhotoIds = new ArrayList<Long>(albumPhotos.size());

                for (final PrivatePhoto privatePhoto__ : albumPhotos) {
                    albumPhotoIds.add(privatePhoto__.getPrivatePhotoId());
                    new Photo$Description(request, $$(AlbumManagerTribeIds.AlbumTribePhotos), photoSequenceNumber++, wallProspects) {
                        @Override
                        protected void init(final Object... initArgs) {
                            final Integer photoSequenceNumber = (Integer) initArgs[0];
                            List<HumansIdentity> mywallProspects = (List<HumansIdentity>) initArgs[1];
                            final String imageURL = RBGet.globalConfig.getString(ALBUM__PHOTOS) + privatePhoto__.getPrivatePhotoURLPath();
                            $$(Page.pd_photo_permalink).setAttribute(MarkupTag.A.href(), imageURL);

                            $$(Page.pd_photo).setAttribute(MarkupTag.IMG.title(), imageURL);

                            $$setClass($$(Page.pd), privatePhoto__.getPrivatePhotoURLPath(), false);

                            /* final String photoThumbURL = imageURL.substring(0, imageURL.lastIndexOf(SLASH) + 1) + CDNAlbumPrivateEvent.THUMBNAIL + imageURL.substring(imageURL.lastIndexOf(SLASH) + 1, photoURL.length());
                           $$(Controller.Page.pd_photo).setAttribute(MarkupTag.IMG.src(), photoThumbURL);*/

                            $$(Page.pd_photo_sequence_number).setTextContent(photoSequenceNumber.toString());

                            displayNone($$(Page.pd_photo_delete));

                            new WallWidgetPrivatePhoto(request, $$(Page.pd_photo_wall), humanId, privatePhoto__.getPrivatePhotoId(), mywallProspects);
                        }
                    };
                }

                $$getHumanUserFromRequest(request).storeAndUpdateWith(HumanUserLocal.STORE_KEY.USER_LOCATION_PRIVATE_PHOTOS, albumPhotoIds);

            } else {
                $$(AlbumManagerTribeIds.AlbumTribeNotice).setTextContent(albumReturn.returnMsg());
            }
        }

        $$(AlbumManagerTribeIds.AlbumTribeTribeId).setAttribute(MarkupTag.INPUT.value(), tribe.getTribeId().toString());

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        itsNatHTMLDocument__.addEventListener((EventTarget) $$(AlbumManagerTribeIds.AlbumTribeForward), EventType.CLICK.toString(), new EventListener() {
            final HumanId myhumanId = humanId;
            final Long mytribeId = tribe.getTribeId();
            boolean confirmed = false;
            final HumansIdentity myhumansIdentity = humansIdentity;
            //final Return<Tribe> mytribeReturn = tribeReturn;
            final Tribe mytribe = tribe;
            final Return<Album> myalbumReturn = albumReturn;

            @Override
            public void handleEvent(final Event evt_) {
                if (confirmed) {
//                    if (mytribeReturn.returnStatus() == 0) {
                    final Tribe t = mytribe;// mytribeReturn.returnValue();
                    final Album album = myalbumReturn.returnValue();
                    Collections.sort(album.getAlbumPhotos());

                    final StringBuilder b = new StringBuilder("");
                    for (final PrivatePhoto pp : album.getAlbumPhotos()) {
                        b.append(
                                UserProperty.fetchToEmailStatically(
                                        myhumansIdentity.getHuman().getDisplayName(),
                                        UserProperty.formatProfileUrl(myhumansIdentity.getUrl().getUrl(), true),
                                        UserProperty.formatProfilePhotoUrl(myhumansIdentity.getHumansIdentityProfilePhoto()),
                                        ElementComposer.compose($$(MarkupTag.IMG)).$ElementSetAttribute(MarkupTag.IMG.src(), RBGet.globalConfig.getString(ALBUM__PHOTOS) + pp.getPrivatePhotoURLPath()).get()
                                )
                        );
                    }


                    for (final HumansTribe member : t.getTribeMembers()) {
                        SendMail.getSendMailLocal().sendAsHTMLAsynchronously(member.getHumanId(), t.getTribeName(), b.toString());
                    }
//                    } else {
//                        $$(Controller.Page.AlbumNotice).setTextContent(mytribeReturn.returnMsg());
//                    }

                    remove(evt_.getTarget(), EventType.CLICK, this, false);
                    confirmed = false;
                    //$$(evt_).setTextContent(RBGet.gui().getString(BUTTONTEXT_EMAIL_FORWARDED));
                    $$(evt_).setAttribute(MarkupTag.IMG.src(),
                            $$(evt_).getAttribute(MarkupTag.IMG.src()).replace("confirm_forward_album_to_friends.png", "forwarded_forward_album_to_friends.png")
                    );
                } else {
                    if (!(albumReturn.returnValue().getAlbumPhotos().size() == 0)) {
                        confirmed = true;
                        $$(evt_).setAttribute(MarkupTag.IMG.src(),
                                $$(evt_).getAttribute(MarkupTag.IMG.src()).replace("forward_album_to_friends.png", "confirm_forward_album_to_friends.png")
                        );
                        //$$(evt_).setTextContent(RBGet.gui().getString(BUTTONTEXT_CLICK_TO_CONFIRM));
                    } else {
                        //Why are we displaying the button in the first place if the album doesn't contain photos? To imply photos can be uploaded and forwarded
                        $$displayNone($$(AlbumManagerTribeIds.AlbumTribeForward));
                    }
                }
            }
        }, false);
    }
}