package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.exception.AbstractEjbApplicationRuntimeException;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.logic.crud.unit.*;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.DisplayNameString;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.logic.validators.unit.SimpleString;
import ai.ilikeplaces.security.blowfish.jbcrypt.BCrypt;
import ai.ilikeplaces.security.face.SingletonHashingRemote;
import ai.ilikeplaces.util.*;
import ai.reaver.RefObj;
import ai.reaver.Return;
import ai.reaver.ReturnImpl;
import ai.scribble.*;
import net.sf.oval.exception.ConstraintsViolatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({EntityManagerInjector.class, DBOffline.class, ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
public class HumanCRUDHuman extends AbstractSLBCallbacks implements HumanCRUDHumanLocal {

    private static final String IDENTITY_READ_SUCCESSFUL = "Identity Read Successful.";
    private static final String IDENTITY_READ_FAILED = "Identity Read FAILED!";
    private static final String PROFILE_READ_FROM_URL_SUCCESSFUL = "Profile Read from URL Successful.";
    private static final String AI_ILIKEPLACES_LOGIC_CRUD_HUMAN_CRUDHUMAN_0001 = "ai.ilikeplaces.logic.crud.HumanCRUDHuman.0001";
    private static final String PROFILE_PHOTO_UPDATE_SUCCESSFUL = "Profile Photo Update Successful.";
    private static final String PROFILE_PHOTO_UPDATE_FAILED = "Profile Photo Update FAILED!";
    private static final String PROFILE_PHOTO_READ_SUCCESSFUL = "Profile Photo Read Successful.";
    private static final String PROFILE_PHOTO_READ_FAILED = "Profile Photo Read FAILED!";
    private static final String PROFILE_URL_UPDATE_SUCCESSFUL = "Profile URL Update Successful.";
    private static final String PROFILE_URL_UPDATE_FAILED = "Profile URL Update FAILED!";
    private static final String PROFILE_URL_READ_SUCCESSFUL = "Profile URL Read Successful.";
    private static final String PROFILE_URL_READ_FAILED = "Profile URL Read FAILED!";
    private static final String READ_HUMANS_BEFRIENDS_SUCCESSFUL = "Read Humans Befriends Successful!";
    private static final String READ_HUMANS_BEFRIENDS_FAILED = "Read Humans Befriends FAILED!";
    private static final String EMAIL = "Email ";
    private static final String IS_NOT_FROM_A_VALID_DOMAIN = " is not from a valid domain";
    private static final String ACTIVATE_PROFILE_SUCCESSFUL = "Activate Profile Successful.";
    private static final String ACTIVATE_PROFILE_FAILED = "Activate Profile FAILED!";
    @EJB
    private RHumanLocal rHumanLocal_;

    @EJB
    private CHumanLocal cHumanLocal_;

    @EJB
    private DHumanLocal dHumanLocal_;

    @EJB
    private UHumanLocal uHumanLocal;

    @EJB
    private UHumansIdentityLocal uHumansIdentityLocal_;

    @EJB
    private UHumansNetPeopleLocal uHumansNetPeopleLocal_;

    @EJB
    private RHumansNetPeopleLocal rHumansNetPeopleLocal_;

    @EJB
    private RHumansIdentityLocal rHumansIdentityLocal_;

    @EJB
    private SingletonHashingRemote singletonHashingRemote;

    @EJB
    private RHumansAuthenticationLocal rHumansAuthenticationLocal_;

    @EJB
    private UHumansAuthenticationLocal uHumansAuthenticationLocal;

    @EJB
    private RHumansPrivateLocationLocal rHumansPrivateLocation_;

    @EJB
    private RHumansPrivateEventLocal rHumansPrivateEvent_;

    @EJB
    private UHumansNetLocal uHumansNetLocal;

    private static final String SYMBOL_AT = "@";
    private static final String READ_HUMANS_AUTHENTICATIONS_SUCCESSFUL = "Read Humans Authentications Successful!";
    private static final String READ_HUMANS_AUTHENTICATIONS_FAILED = "Read Humans Authentications FAILED!";
    private static final String READ_HUMANS_PRIVATE_LOCATION_SUCCESSFUL = "Read HumansPrivateLocation Successful!";
    private static final String READ_HUMANS_PRIVATE_LOCATION_FAILED = "Read HumansPrivateLocation FAILED!";
    private static final String READ_HUMANS_PRIVATE_EVENT_SUCCESSFUL = "Read HumansPrivateEvent Successful!";
    private static final String READ_HUMANS_PRIVATE_EVENT_FAILED = "Read HumansPrivateEvent FAILED!";
    private static final String ADD_FRIEND_SUCCESSFUL = "Add friend Successful!";
    private static final String ADD_FRIEND_FAILED = "Add friend FAILED!";
    private static final String REMOVE_FRIEND_SUCCESSFUL = "Remove friend Successful!";
    private static final String REMOVE_FRIEND_FAILED = "Remove friend FAILED!";
    private static final String CHECK_FRIEND_SUCCESSFUL = "Check friend Successful!";
    private static final String CHECK_FRIEND_FAILED = "Check friend FAILED!";
    private static final String UPDATE_DISPLAY_NAME_SUCCESSFUL = "Update Display Name Successful!";
    private static final String UPDATE_DISPLAY_NAME_FAILED = "Update Display Name FAILED!";
    private static final String ADD_USER_SUCCESSFUL = "Add User Successful!";
    private static final String ADD_USER_FAILED = "Add User FAILED!";
    private static final String PASSWORD_UPDATE_SUCCESSFUL = "Password Update Successful!";
    private static final String PASSWORD_UPDATE_FAILED = "Password Update FAILED!";


    public HumanCRUDHuman() {
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Human doDirtyRHuman(final String humanId) {
        return rHumanLocal_.doDirtyRHuman(humanId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Human doDirtyRHuman(final RefObj<String> humanId) {
        return
                rHumanLocal_.doDirtyRHuman(humanId.getObj());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<HumansAuthentication> doDirtyRHumansAuthentication(final RefObj<String> humanId) {
        Return<HumansAuthentication> r;
        r = new ReturnImpl<HumansAuthentication>(rHumansAuthenticationLocal_.doDirtyRHumansAuthentication(humanId.getObj()), READ_HUMANS_AUTHENTICATIONS_SUCCESSFUL);
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public HumansNetPeople doDirtyRHumansNetPeople(final RefObj<String> humanId) {
        return rHumansNetPeopleLocal_.doDirtyRHumansNetPeople(humanId.getObj());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<HumansPrivateLocation> doDirtyRHumansPrivateLocation(final RefObj<String> humanId) {
        Return<HumansPrivateLocation> r;
        try {
            r = new ReturnImpl<HumansPrivateLocation>(rHumansPrivateLocation_.doNTxRHumansPrivateLocation(humanId.getObj()), READ_HUMANS_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<HumansPrivateLocation>(t, READ_HUMANS_PRIVATE_LOCATION_FAILED, true);
        } catch (final DBRefreshDataException t) {
            r = new ReturnImpl<HumansPrivateLocation>(t, READ_HUMANS_PRIVATE_LOCATION_FAILED, true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<HumansPrivateEvent> doDirtyRHumansPrivateEvent(final RefObj<String> humanId) {
        Return<HumansPrivateEvent> r;
        try {
            r = new ReturnImpl<HumansPrivateEvent>(rHumansPrivateEvent_.doNTxRHumansPrivateEvent(humanId.getObj()), READ_HUMANS_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<HumansPrivateEvent>(t, READ_HUMANS_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Return<Boolean> doNTxAddHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(uHumansNetPeopleLocal_.doNTxAddHumansNetPeople(adderHumanId.getObj(), addeeHumanId.getObj()), ADD_FRIEND_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Boolean>(t, ADD_FRIEND_FAILED, true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Return<Boolean> doNTxRemoveHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(uHumansNetPeopleLocal_.doNTxRemoveHumansNetPeople(adderHumanId.getObj(), addeeHumanId.getObj()), REMOVE_FRIEND_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Boolean>(t, REMOVE_FRIEND_FAILED, true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Return<Boolean> doNTxIsHumansNetPeople(final RefObj<String> checkerHumanId, final RefObj<String> checkeeeHumanId) {
        Return<Boolean> r;
        r = new ReturnImpl<Boolean>(uHumansNetPeopleLocal_.doNTxIsHumansNetPeople(checkerHumanId.getObj(), checkeeeHumanId.getObj()), CHECK_FRIEND_SUCCESSFUL);
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Return<Boolean> doNTxUDisplayName(final HumanId humanId, DisplayNameString displayName) {
        Return<Boolean> r;
        uHumansNetLocal.doNTxUDisplayName(humanId, displayName.getObj());
        r = new ReturnImpl<Boolean>(true, UPDATE_DISPLAY_NAME_SUCCESSFUL);
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Boolean> doDirtyIsHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId) {
        Return<Boolean> r;
        r = new ReturnImpl<Boolean>(uHumansNetPeopleLocal_.doDirtyIsHumansNetPeople(adderHumanId.getObj(), addeeHumanId.getObj()), CHECK_FRIEND_SUCCESSFUL);
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @_verify(item = "Check if list email validation works", against = "MethodParams")
    public List<HumansIdentity> doDirtyRHumansIdentitiesByEmails(final List<Email> emails) {
        List<String> emailList = new ArrayList<String>();
        for (final RefObj<String> email : emails) {
            emailList.add(email.getObj());
        }
        return rHumansIdentityLocal_.doDirtyRHumansIdentitiesByEmails(emailList);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void doNTxCHuman(final Human human) {
        cHumanLocal_.doNTxCHuman(human);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Boolean> doDirtyCheckHuman(final String humanId) {

        Return<Boolean> r;
        r = new ReturnImpl<Boolean>(rHumanLocal_.doDirtyCheckHuman(humanId), "Check human Successful!");
        return r;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean doDHuman(final RefObj<String> humanId) {
        dHumanLocal_.doDHuman(humanId.getObj());
        return true;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Boolean> doUHumanPassword(final RefObj<String> humanId, final Password currentPassword, final Password newPassword) {
        Return<Boolean> r;

        if (humanId.validate() != 0) {
            throw new ConstraintsViolatedException(humanId.getViolations());
        } else if (newPassword.validate() != 0) {
            throw new ConstraintsViolatedException(newPassword.getViolations());
        } else {
            final boolean changed = uHumansAuthenticationLocal.doUHumansAuthentication(humanId.getObj(), currentPassword.getObj(), newPassword.getObj());
            if (changed) {
                SendMail.getSendMailLocal().sendAsSimpleText(humanId.getObj(),
                        "Password Changed for I Like Places",
                        "Hi,\n" +
                                "You changed your password at I Like Places right? Hmmmm, just checking.\n" +
                                "If it wasn't you, who was it?!! Well, you can always reset or change your password if you notice something suspicious.");
            }

            r = new ReturnImpl<Boolean>(changed, PASSWORD_UPDATE_SUCCESSFUL);
        }
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Boolean> doUHumanPassword(final RefObj<String> humanId, final Password newPassword) {
        Return<Boolean> r;

        if (humanId.validate() != 0) {
            throw new ConstraintsViolatedException(humanId.getViolations());
        } else if (newPassword.validate() != 0) {
            throw new ConstraintsViolatedException(newPassword.getViolations());
        } else {
            final boolean changed = uHumansAuthenticationLocal.doUHumansAuthentication(humanId.getObj(), newPassword.getObj());
            if (changed) {
                SendMail.getSendMailLocal().sendAsSimpleText(humanId.getObj(),
                        "Password Changed for I Like Places",
                        "Hi,\n" +
                                "You changed your password at I Like Places right? Hmmmm, just checking.\n" +
                                "If it wasn't you, who was it?!! Well, you can always reset or change your password if you notice something suspicious.");
            }

            r = new ReturnImpl<Boolean>(changed, PASSWORD_UPDATE_SUCCESSFUL);
        }
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<HumansIdentity> doUHumansProfilePhoto(final RefObj<String> humanId, final String url) {
        Return<HumansIdentity> r;
        try {
            r = new ReturnImpl<HumansIdentity>(uHumansIdentityLocal_.doUHumansProfilePhoto(humanId.getObj(), url), PROFILE_PHOTO_UPDATE_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<HumansIdentity>(t, PROFILE_PHOTO_UPDATE_FAILED, true);
        }
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<String> doDirtyRHumansProfilePhoto(final RefObj<String> humanId) {
        Return<String> r;
        try {
            r = new ReturnImpl<String>(rHumansIdentityLocal_.doDirtyProfilePhoto(humanId.getObj()), PROFILE_PHOTO_READ_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<String>(t, PROFILE_PHOTO_READ_FAILED, true);
        }
        return r;

    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Return<HumansIdentity> doUHumansPublicURL(final RefObj<String> humanId, final String url) {
        Return<HumansIdentity> r;
        try {
            r = new ReturnImpl<HumansIdentity>(uHumansIdentityLocal_.doUHumansPublicURL(humanId.getObj(), url), PROFILE_URL_UPDATE_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<HumansIdentity>(t, PROFILE_URL_UPDATE_FAILED, true);
        }
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<String> doDirtyRHumansPublicURL(final RefObj<String> humanId) {
        Return<String> r;
        try {
            r = new ReturnImpl<String>(rHumansIdentityLocal_.doDirtyPublicURL(humanId.getObj()), PROFILE_URL_READ_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<String>(t, PROFILE_URL_READ_FAILED, true);
        }
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<HumansIdentity> doDirtyRHumansIdentity(final RefObj<String> humanId) {
        Return<HumansIdentity> r;
        try {
            r = new ReturnImpl<HumansIdentity>(rHumansIdentityLocal_.doDirtyRHumansIdentity(humanId.getObjectAsValid()), IDENTITY_READ_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<HumansIdentity>(t, IDENTITY_READ_FAILED, true);
        }
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<String> doDirtyProfileFromURL(final String url) {
        Return<String> r;
        r = new ReturnImpl<String>(rHumansIdentityLocal_.doDirtyProfileFromURL(url), PROFILE_READ_FROM_URL_SUCCESSFUL);
        return r;

    }

    /*BEGINNING OF PREPERATOR METHODS*/

    @_todo(task = "ADD HUMANSIDENTITY VALUES TAKE FROM THE SERVLETSIGNUP. CHANGE ALL NON_CRUDSERVICE CLASSES TO USE NON_TRANSACTIONAL AS REQUIRED")
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Return<Boolean> doCHuman(final RefObj<String> humanId, final RefObj<String> password, final RefObj<String> email) {
        Return<Boolean> r;

        if (doDirtyCheckHuman(humanId.getObjectAsValid()).returnValue()) {
            r = new ReturnImpl<Boolean>(DBDishonourCheckedException.ADDING_AN_EXISTING_VALUE, HumanCRUDHuman.ADD_USER_FAILED, true);
        } else {

            if (ai.ilikeplaces.util.email.Email.isAddressValidTolerant(humanId.getObjectAsValid())) {

                final Human newUser = new Human();
                newUser.setHumanId(humanId.getObjectAsValid());
                newUser.setHumanAlive(false);//Wait for signup confirmation click by email

                final HumansAuthentication ha = new HumansAuthentication();
                ha.setHumanId(newUser.getHumanId());
                ha.setHumanAuthenticationSalt(BCrypt.gensalt());
                ha.setHumanAuthenticationHash(singletonHashingRemote.getHash(password.getObjectAsValid(), ha.getHumanAuthenticationSalt()));
                newUser.setHumansAuthentications(ha);
                newUser.setHumanAlive(false);

                setHumansIdentityInfo:
                {
                    final HumansIdentity hid = new HumansIdentity();
                    hid.setHumanId(newUser.getHumanId());
                    hid.setUrl(new Url().setUrlR(humanId.getObj()).setMetadataR(humanId.getObj()));//Yes, the default url is his/her email

                    newUser.setHumansIdentity(hid);
                }

                //HumansNet has as many internal primarykeyjoin entities
                tricky:
                {
                    final HumansNetPeople hnp = new HumansNetPeople();
                    hnp.setHumanId(newUser.getHumanId());

                    final HumansNet hn = new HumansNet();
                    hn.setDisplayName(newUser.getHumanId().split(SYMBOL_AT)[0]);
                    hn.setHumanId(newUser.getHumanId());

                    hn.setHumansNetPeople(hnp);

                    newUser.setHumansNet(hn);
                }


                final HumansPrivateLocation hpl = new HumansPrivateLocation();
                hpl.setHumanId(newUser.getHumanId());
                newUser.setHumansPrivateLocation(hpl);

                final HumansPrivateEvent hpe = new HumansPrivateEvent();
                hpe.setHumanId(newUser.getHumanId());
                newUser.setHumansPrivateEvent(hpe);

                final HumansAlbum hal = new HumansAlbum();
                hal.setHumanId(newUser.getHumanId());
                newUser.setHumansAlbum(hal);

                final HumansPrivatePhoto hprp = new HumansPrivatePhoto();
                hprp.setHumanId(newUser.getHumanId());
                newUser.setHumansPrivatePhoto(hprp);

                final HumansPublicPhoto hpup = new HumansPublicPhoto();
                hpup.setHumanId(newUser.getHumanId());
                newUser.setHumansPublicPhoto(hpup);

                final HumansWall hw = new HumansWall();
                hw.setHumanId(newUser.getHumanId());
                hw.setWall(new Wall().setWallTypeR(Wall.wallTypeHuman));
                newUser.setHumansWall(hw);

                final HumansAlbum halbum = new HumansAlbum();
                halbum.setHumanId(newUser.getHumanId());
                newUser.setHumansAlbum(halbum);

                doNTxCHuman(newUser);

                r = new ReturnImpl<Boolean>(true, ADD_USER_SUCCESSFUL);
            } else {
                r = new ReturnImpl<Boolean>(new IllegalArgumentException(EMAIL + humanId.getObjectAsValid() + IS_NOT_FROM_A_VALID_DOMAIN), HumanCRUDHuman.ADD_USER_FAILED, true);
            }
        }
        return r;
    }


    public Return<Boolean> doUActivateHuman(final RefObj<String> username) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(uHumanLocal.doUActivateHuman(username.getObjectAsValid()), ACTIVATE_PROFILE_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, ACTIVATE_PROFILE_FAILED, true);
        }
        return r;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<List<HumansNetPeople>> doDirtyRHumansBefriends(final RefObj<String> humanId) {
        Return<List<HumansNetPeople>> r;
        try {
            r = new ReturnImpl<List<HumansNetPeople>>(rHumansNetPeopleLocal_.doDirtyRHumansBefriends(humanId.getObj()), READ_HUMANS_BEFRIENDS_SUCCESSFUL);
        } catch (final AbstractEjbApplicationRuntimeException t) {
            r = new ReturnImpl<List<HumansNetPeople>>(t, READ_HUMANS_BEFRIENDS_FAILED, true);
        }
        return r;

    }

    /*END OF PREPERATOR METHODS*/

    final static Logger logger = LoggerFactory.getLogger(HumanCRUDHuman.class);

    /**
     * Do your verification inside this method. Try to encapsulate all exceptions within the method and provide
     * humanly readable debug messages.
     *
     * @return The Result Of Verification. e.g. Done, Error-Resource Down.
     */
    @Override
    @Deprecated
    @_note(note = "Will be centralized", see = "IntegrityTester.class")
    @_fix(issue = "Throws exception for duplicate key. Check why.")
    public String verify() {
        final String random = this.getClass().getSimpleName() + System.currentTimeMillis();
        DB.getHumanCRUDMapLocal(true).createEntry("verify", random);
        String returnVal = "";

        verifydoDirtyRHumansIdentitiesByEmails:
        {
            setUpSomeHumans:
            {
                DB.getHumanCRUDHumanLocal(true).doCHuman(new HumanId(random + "1"), new Password(random), null);
                DB.getHumanCRUDHumanLocal(true).doCHuman(new HumanId(random + "2"), new Password(random), null);
                DB.getHumanCRUDHumanLocal(true).doCHuman(new HumanId(random + "3"), new Password(random), null);
            }

            verify:
            {
                List<Email> emails = new ArrayList<Email>();

                returnVal += "\nAdding email";
                emails.add(new Email(random + "1" + "@example.com"));
                returnVal += "\nExpecting true" +
                        "\nSuccessful?" + (DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentitiesByEmails(emails).size() == 1);

                returnVal += "\nAdding email";
                emails.add(new Email(random + "2" + "@example.com"));
                returnVal += "\nExpecting true" +
                        "\nSuccessful?" + (DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentitiesByEmails(emails).size() == 2);

                returnVal += "\nAdding email";
                emails.add(new Email(random + "3" + "@example.com"));
                returnVal += "\nExpecting true" +
                        "\nSuccessful?" + (DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentitiesByEmails(emails).size() == 3);
            }

            cleanup:
            {
                DB.getHumanCRUDHumanLocal(true).doDHuman(new SimpleString(random + "1"));
                DB.getHumanCRUDHumanLocal(true).doDHuman(new SimpleString(random + "2"));
                DB.getHumanCRUDHumanLocal(true).doDHuman(new SimpleString(random + "3"));
            }

        }

        verifyAddRemoveFriends:
        {
            setUpSomeHumans:
            {
                DB.getHumanCRUDHumanLocal(true).doCHuman(new HumanId(random + "1"), new Password(random), null);
                DB.getHumanCRUDHumanLocal(true).doCHuman(new HumanId(random + "2"), new Password(random), null);
                DB.getHumanCRUDHumanLocal(true).doCHuman(new HumanId(random + "3"), new Password(random), null);
            }

            verify:
            {
                returnVal += "\nAdding a friend";
                DB.getHumanCRUDHumanLocal(true).doNTxAddHumansNetPeople(new HumanId(random + "1"), new HumanId(random + "2"));
                returnVal += "\nExpecting true";
                returnVal += "\nResult:" + (DB.getHumanCRUDHumanLocal(true).doNTxIsHumansNetPeople(new HumanId(random + "1"), new HumanId(random + "2")));

                returnVal += "\nRemoving a friend";
                DB.getHumanCRUDHumanLocal(true).doNTxAddHumansNetPeople(new HumanId(random + "1"), new HumanId(random + "2"));
                returnVal += "\nExpecting false";
                returnVal += "\nResult:" + (DB.getHumanCRUDHumanLocal(true).doNTxIsHumansNetPeople(new HumanId(random + "1"), new HumanId(random + "2")));
            }

            cleanup:
            {
                DB.getHumanCRUDHumanLocal(true).doDHuman(new SimpleString(random + "1"));
                DB.getHumanCRUDHumanLocal(true).doDHuman(new SimpleString(random + "2"));
                DB.getHumanCRUDHumanLocal(true).doDHuman(new SimpleString(random + "3"));
            }

        }
        return returnVal;
    }

}
