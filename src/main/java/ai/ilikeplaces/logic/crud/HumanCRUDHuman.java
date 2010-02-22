package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.logic.crud.unit.*;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.SimpleString;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.security.blowfish.jbcrypt.BCrypt;
import ai.ilikeplaces.security.face.SingletonHashingFace;
import ai.ilikeplaces.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class})
public class HumanCRUDHuman extends AbstractSLBCallbacks implements HumanCRUDHumanLocal {

    @EJB
    private RHumanLocal rHumanLocal_;

    @EJB
    private CHumanLocal cHumanLocal_;

    @EJB
    private DHumanLocal dHumanLocal_;

    @EJB
    private UHumansNetPeopleLocal uHumansNetPeopleLocal_;

    @EJB
    private RHumansNetPeopleLocal rHumansNetPeopleLocal_;

    @EJB
    private RHumansIdentityLocal rHumansIdentityLocal_;

    @EJB
    private SingletonHashingFace singletonHashingFace;

    @EJB
    private RHumansAuthenticationLocal rHumansAuthenticationLocal_;

    @EJB
    private RHumansPrivateLocationLocal rHumansPrivateLocation_;

    @EJB
    private RHumansPrivateEventLocal rHumansPrivateEvent_;


    public HumanCRUDHuman() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", HumanCRUDHuman.class, this.hashCode());
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
    public HumansAuthentication doDirtyRHumansAuthentication(final RefObj<String> humanId) {
        return rHumansAuthenticationLocal_.doDirtyRHumansAuthentication(humanId.getObj());
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
            r = new ReturnImpl<HumansPrivateLocation>(rHumansPrivateLocation_.doDirtyRHumansPrivateLocation(humanId.getObj()), "Read HumansPrivateLocation Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<HumansPrivateLocation>(t, "Read HumansPrivateLocation FAILED!", true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<HumansPrivateEvent> doDirtyRHumansPrivateEvent(final RefObj<String> humanId) {
        Return<HumansPrivateEvent> r;
        try {
            r = new ReturnImpl<HumansPrivateEvent>(rHumansPrivateEvent_.doDirtyRHumansPrivateEvent(humanId.getObj()), "Read HumansPrivateEvent Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<HumansPrivateEvent>(t, "Read HumansPrivateEvent FAILED!", true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Return<Boolean> doNTxAddHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(uHumansNetPeopleLocal_.doNTxAddHumansNetPeople(adderHumanId.getObj(), addeeHumanId.getObj()), "Add friend Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, "Add friend FAILED!", true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Return<Boolean> doNTxRemoveHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(uHumansNetPeopleLocal_.doNTxRemoveHumansNetPeople(adderHumanId.getObj(), addeeHumanId.getObj()), "Remove friend Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, "Remove friend FAILED!", true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Return<Boolean> doNTxIsHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(uHumansNetPeopleLocal_.doNTxIsHumansNetPeople(adderHumanId.getObj(), addeeHumanId.getObj()), "Check friend Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, "Check friend FAILED!", true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Boolean> doDirtyIsHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(uHumansNetPeopleLocal_.doDirtyIsHumansNetPeople(adderHumanId.getObj(), addeeHumanId.getObj()), "Check friend Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, "Check friend FAILED!", true);
        }
        return r;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @VERIFY(item = "Check if list email validation works", against = "MethodParams")
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
    public boolean doDirtyCheckHuman(final String humanId) {
        return rHumanLocal_.doDirtyCheckHuman(humanId);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean doDHuman(final RefObj<String> humanId) {
        dHumanLocal_.doDHuman(humanId.getObj());
        return true;
    }

    /*BEGINNING OF PREPERATOR METHODS*/

    @TODO(task = "ADD HUMANSIDENTITY VALUES TAKE FROM THE SERVLETSIGNUP. CHANGE ALL NON_CRUDSERVICE CLASSES TO USE NON_TRANSACTIONAL AS REQUIRED")
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void doCHuman(final String humanId, final String password, final String email, final String gender, final String dateOfBirth) throws IllegalAccessException {
        if (doDirtyCheckHuman(humanId)) {
            throw new IllegalAccessException(RBGet.logMsgs.getString("ai.ilikeplaces.logic.crud.HumanCRUDHuman.0001") + humanId);
        }
        final Human newUser = new Human();
        newUser.setHumanId(humanId);
        final HumansAuthentication ha = new HumansAuthentication();
        ha.setHumanId(newUser.getHumanId());
        ha.setHumanAuthenticationSalt(BCrypt.gensalt());
        ha.setHumanAuthenticationHash(singletonHashingFace.getHash(password, ha.getHumanAuthenticationSalt()));
        newUser.setHumansAuthentications(ha);
        newUser.setHumanAlive(true);

        setHumansIdentityInfo:
        {
            final HumansIdentity hid = new HumansIdentity();
            hid.setHumanId(newUser.getHumanId());

            try {
                hid.setHumansIdentityDateOfBirth((new SimpleDateFormat("yyyy-MM-DD")).parse(dateOfBirth));
            } catch (ParseException e) {
                logger.error("SORRY! THIS SHOULD NOT HAPPEN. NON-VALIDATED DATE RECEIVED: {}", e);
            }

            hid.setHumansIdentityEmail(email);
            hid.setHumansIdentityGenderCode(HumansIdentity.GENDER.getGenderCode(gender));
            newUser.setHumansIdentity(hid);
        }

        //HumansNet has as many internal primarykeyjoin entities
        tricky:
        {
            final HumansNetPeople hnp = new HumansNetPeople();
            hnp.setHumanId(newUser.getHumanId());

            final HumansNet hn = new HumansNet();
            hn.setDisplayName("TODO:" + newUser.getHumanId());
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
        doNTxCHuman(newUser);
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
    @NOTE(note = "Will be centralized", see = "IntegrityTester.class")
    @FIXME(issue = "Throws exception for duplicate key. Check why.")
    public String verify() {
        final String random = this.getClass().getSimpleName() + System.currentTimeMillis();
        DB.getHumanCRUDMapLocal(true).createEntry("verify", random);
        String returnVal = "";

        verifydoDirtyRHumansIdentitiesByEmails:
        {
            try {
                setUpSomeHumans:
                {
                    DB.getHumanCRUDHumanLocal(true).doCHuman(random + "1", random, random + "1" + "@example.com", "Male", "1984-06-25");
                    DB.getHumanCRUDHumanLocal(true).doCHuman(random + "2", random, random + "2" + "@example.com", "Male", "1984-06-25");
                    DB.getHumanCRUDHumanLocal(true).doCHuman(random + "3", random, random + "3" + "@example.com", "Male", "1984-06-25");
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

            } catch (IllegalAccessException e) {
                logger.error("{}", e);
            }
        }

        verifyAddRemoveFriends:
        {
            try {
                setUpSomeHumans:
                {
                    DB.getHumanCRUDHumanLocal(true).doCHuman(random + "1", random, random + "1" + "@example.com", "Male", "1984-06-25");
                    DB.getHumanCRUDHumanLocal(true).doCHuman(random + "2", random, random + "2" + "@example.com", "Male", "1984-06-25");
                    DB.getHumanCRUDHumanLocal(true).doCHuman(random + "3", random, random + "3" + "@example.com", "Male", "1984-06-25");
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

            } catch (IllegalAccessException e) {
                logger.error("{}", e);
            }
        }
        return returnVal;
    }
}
