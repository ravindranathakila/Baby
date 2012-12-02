package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansUnseen;
import ai.ilikeplaces.entities.Wall;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CRUDHumansUnseenLocal {

    public void addEntry(final String humanId, final Long wallId);

    public void addEntry(final List<String> humanIds, final Long wallId);

    public void removeEntry(final String humanId, final Long wallId);

    public List<Wall> readEntries(final String humanId);

    public HumansUnseen doRHumansUnseenBadly(final String humanId);

}
