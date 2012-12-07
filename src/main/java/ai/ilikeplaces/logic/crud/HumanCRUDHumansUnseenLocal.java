package ai.ilikeplaces.logic.crud;

import ai.doc.License;
import ai.ilikeplaces.entities.HumansUnseen;
import ai.ilikeplaces.entities.Wall;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Remote
public interface HumanCRUDHumansUnseenLocal {

    final static public String NAME = HumanCRUDHumansUnseenLocal.class.getSimpleName();

    public void addEntry(final String humanId, final Long wallId);

    public void addEntry(final List<String> humanIds, final Long wallId);

    public void removeEntry(final String humanId, final Long wallId);

    public List<Wall> readEntries(final String humanId);

    public HumansUnseen getHumansUnseen(final String humanId);
}

