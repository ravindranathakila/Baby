package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.logic.validators.unit.Email;
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

    public HumansNetPeople doDirtyRHumansNetPeople(final RefObj<String> humanId);

    public Return<Boolean> doNTxAddHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId);

    public Return<Boolean> doNTxRemoveHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId);

    public Return<Boolean> doNTxIsHumansNetPeople(final RefObj<String> adderHumanId, final RefObj<String> addeeHumanId);

    public List<HumansIdentity> doDirtyRHumansIdentitiesByEmails(final List<Email> emails);

    public void doCHuman(final String username, final String password, final String email, final String gender, final String dateOfBirth) throws IllegalAccessException;

    public boolean doDirtyCheckHuman(final String humanId);

    public boolean doDHuman(final RefObj<String> humanId);
}

