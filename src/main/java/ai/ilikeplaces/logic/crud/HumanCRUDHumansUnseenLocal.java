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
import java.util.Set;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDHumansUnseenLocal {

    final static public String NAME = HumanCRUDHumansUnseenLocal.class.getSimpleName();

    public void addEntry(final String humanId, final Long wallId);

    public void addEntry(final List<String> humanIds, final Long wallId);

    public void removeEntry(final String humanId, final Long wallId);

    public Set<Wall> readEntries(final String humanId);

    public HumansUnseen getHumansUnseen(final String humanId);
}

