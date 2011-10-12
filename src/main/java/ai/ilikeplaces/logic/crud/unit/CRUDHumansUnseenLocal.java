package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.HumansUnseen;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.util.jpa.RefreshSpec;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;
import java.util.Set;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CRUDHumansUnseenLocal {

    public void addEntry(final String humanId, final Long wallId);

    public void addEntry(final List<String> humanIds, final Long wallId);

    public void removeEntry(final String humanId, final Long wallId);

    public Set<Wall> readEntries(final String humanId);

    public HumansUnseen getHumansUnseen(final String humanId);

}