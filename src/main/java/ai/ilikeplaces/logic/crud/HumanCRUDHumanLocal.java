package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.logic.validators.unit.DisplayNameString;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.logic.verify.util.Verify;
import ai.ilikeplaces.util.RefObj;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDHumanLocal extends Verify {

    final static public String NAME = HumanCRUDHumanLocal.class.getSimpleName();

    public Human doDirtyRHuman(final String humanId);

    public Human doDirtyRHuman(final RefObj<String> humanId);

    public Return<HumansAuthentication> doDirtyRHumansAuthentication(final RefObj<String> humanId);

    public HumansNetPeople doDirtyRHumansNetPeople(final RefObj<String> humanId);

    public Return<HumansPrivateLocation> doDirtyRHumansPrivateLocation(final RefObj<String> humanId);

    public Return<HumansPrivateEvent> doDirtyRHumansPrivateEvent(final RefObj<String> humanId);

    public Return<Boolean> doNTxAddHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId);

    public Return<Boolean> doNTxRemoveHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId);

    @NOTE(note = "Triple e's were intentional. Two e's confuses er with ee.")
    public Return<Boolean> doNTxIsHumansNetPeople(final RefObj<String> checkerHumanId, final RefObj<String> checkeeeHumanId);

    public Return<Boolean> doNTxUDisplayName(final HumanId humanId, DisplayNameString displayName);

    public Return<Boolean> doDirtyIsHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId);

    /**
     * Checks if the user exists on ilikeplaces.com by her email
     *
     * @param emails
     * @return
     */
    public List<HumansIdentity> doDirtyRHumansIdentitiesByEmails(final List<Email> emails);

    public Return<Boolean> doCHuman(final RefObj<String> username, final RefObj<String> password, final RefObj<String> email) throws DBDishonourCheckedException;

    public Return<Boolean> doUActivateHuman(final RefObj<String> username) throws DBDishonourCheckedException;

    /**
     * Checks if a human is in the database
     *
     * @param humanId
     * @return boolean true if user exists
     */
    public Return<Boolean> doDirtyCheckHuman(final String humanId);

    public boolean doDHuman(final RefObj<String> humanId);

    public Return<Boolean> doUHumanPassword(final RefObj<String> humanId, final Password currentPassword, final Password newPassword);

    public Return<Boolean> doUHumanPassword(final RefObj<String> humanId, final Password newPassword);

    public Return<HumansIdentity> doUHumansProfilePhoto(final RefObj<String> humanId, final String url);

    public Return<String> doDirtyRHumansProfilePhoto(final RefObj<String> humanId);

    public Return<HumansIdentity> doUHumansPublicURL(final RefObj<String> humanId, final String url);

    /**
     * Just a critical note. The humanId sent here does not imply signed on status of this user by any means. This is
     * actually a profile URL lookup that would be done by any user on another users profile. Hence the information
     * shown by the result of this call, which is the URL is privacy sensitive. Of course this will be handled, but
     * a warning will just make usage more prudent.
     *
     * @param humanId
     * @return
     */
    public Return<String> doDirtyRHumansPublicURL(final RefObj<String> humanId);

    public Return<HumansIdentity> doDirtyRHumansIdentity(final RefObj<String> humanId);

    public Return<String> doDirtyProfileFromURL(final String url);

}

