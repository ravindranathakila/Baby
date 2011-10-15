package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansUnseen;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Mute;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.jpa.RefreshException;
import ai.ilikeplaces.util.jpa.RefreshSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CRUDHumansUnseen extends AbstractSLBCallbacks implements CRUDHumansUnseenLocal {
    private static final String STR_EMPTY = "";
    private static final String STR_SPACE = " ";
    private static final String ADDING_NOTIFICATION_WALL_ENTRIES_FAILED_FOR = "ADDING NOTIFICATION WALL ENTRIES FAILED FOR: ";
    // ------------------------------ FIELDS ------------------------------

    @EJB
    private CrudServiceLocal<HumansUnseen> humansUnseenCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<Wall> wallCrudServiceLocal_;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface CRUDHumansUnseenLocal ---------------------

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addEntry(final String humanId, Long wallId) {
        getHumansUnseen(humanId).getUnseenWalls().add(wallCrudServiceLocal_.findBadly(Wall.class, wallId));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addEntry(final List<String> humanIds, final Long wallId) {
        StringBuilder stringBuilder = null;
        for (final String humanId : humanIds) {
            try {
                final Wall wall = wallCrudServiceLocal_.findBadly(Wall.class, wallId);
                getHumansUnseen(humanId).getUnseenWalls().add(wall);
            } catch (final Throwable t) {//we need to proceed with the loop for others you know, so just in case
                stringBuilder = (stringBuilder == null ? new StringBuilder(STR_EMPTY) : stringBuilder);
                stringBuilder.append(humanId).append(STR_SPACE);
            }
            if (stringBuilder != null) {
                throw new DBException(ADDING_NOTIFICATION_WALL_ENTRIES_FAILED_FOR + stringBuilder.toString());
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeEntry(final String humanId, Long wallId) {
        getHumansUnseen(humanId).getUnseenWalls().remove(wallCrudServiceLocal_.findBadly(Wall.class, wallId));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Set<Wall> readEntries(final String humanId) {
        HumansUnseen humansUnseen = humansUnseenCrudServiceLocal_.find(HumansUnseen.class, humanId);
        if (humansUnseen == null) {
            humansUnseenCrudServiceLocal_.create(new HumansUnseen().setHumanIdR(humanId));
            humansUnseen = humansUnseenCrudServiceLocal_.findBadly(HumansUnseen.class, humanId);
        }
        return humansUnseen.getUnseenWalls();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public HumansUnseen getHumansUnseen(final String humanId) {
        HumansUnseen humansUnseen = humansUnseenCrudServiceLocal_.find(HumansUnseen.class, humanId);
        if (humansUnseen == null) {
            humansUnseenCrudServiceLocal_.create(new HumansUnseen().setHumanIdR(humanId));
            humansUnseen = humansUnseenCrudServiceLocal_.findBadly(HumansUnseen.class, humanId);
        }
        return humansUnseen;
    }
}