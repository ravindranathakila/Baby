package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Mute;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBDishonourException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.jpa.QueryParameter;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.LogNull;
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

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CRUDWall extends AbstractSLBCallbacks implements CRUDWallLocal {

    private static final String IS_NULL_OR_EMPTY = " IS NULL OR EMPTY!";
    @EJB
    private CrudServiceLocal<Wall> crudServiceLocal_;

    @EJB
    private CrudServiceLocal<Msg> msgCrudServiceLocal_;

    public CRUDWall() {
    }

    final static Logger logger = LoggerFactory.getLogger(CRUDWall.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Wall doNTxCWall(final Wall wall) {
        return crudServiceLocal_.create(wall);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Wall doDirtyRWall(long wallId) throws DBDishonourCheckedException {
        return crudServiceLocal_.findBadly(Wall.class, wallId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Wall doRWall(final long wallId, final RefreshSpec refreshSpec) throws DBFetchDataException {
        try {
            return crudServiceLocal_.findBadly(Wall.class, wallId).refresh(refreshSpec);
        } catch (RefreshException e) {
            throw new DBFetchDataException(e);
        }
    }

//    @Override
//    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//    @Deprecated
//    public Wall doNTxUAppendToWall(long wallId, String contentToBeAppended) throws DBDishonourCheckedException {
//        final Wall returnVal = crudServiceLocal_.findBadly(Wall.class, wallId);
//        final int wallLength = returnVal.getWallContent().length();
//        if (wallLength > (Wall.WALL_LENGTH - 1) - contentToBeAppended.length()) {
//            Loggers.DEBUG.debug("Trimming long wall content.");
//            returnVal.setWallContent(returnVal.getWallContent().subSequence(
//                    contentToBeAppended.length() - 1, wallLength - 1)
//                    .toString());
//        }
//        returnVal.setWallContent(returnVal.getWallContent() + contentToBeAppended);
//        return returnVal;
//    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Wall doUAddEntry(long wallId, final String humanId, String contentToBeAppended) throws DBDishonourCheckedException {
        final Wall returnVal = rWallBadly(wallId);
        final Msg msg = new Msg()
                .setMsgContentR(contentToBeAppended)
                .setMsgTypeR(Msg.msgTypeHUMAN)
                .setMsgMetadataR(humanId);

        final Msg managedMsg = msgCrudServiceLocal_.create(msg);

        returnVal.getWallMsgs().size();//refreshing
        returnVal.getWallMsgs().add(msg);

        crudServiceLocal_.update(returnVal);

        return returnVal;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Wall doUAddMuteEntry(long wallId, final String mutee) throws DBDishonourCheckedException {
        final Wall returnVal = rWallBadly(wallId);

        if (returnVal.getWallMutes().contains(new HumanId(mutee).getSelfAsValid())) {
            throw DBDishonourCheckedException.ADDING_AN_EXISTING_VALUE;
        }

        returnVal.getWallMutes().add(new Mute()
                .setMuteContentR(mutee)
                .setMuteTypeR(Mute.muteTypeHUMAN)
                .setMuteMetadataR(mutee));

        return returnVal;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Wall doURemoveMuteEntry(long wallId, final String mutee) throws DBDishonourCheckedException {
        final Wall returnVal = rWallBadly(wallId);
        final HumanId humanId = new HumanId(mutee).getSelfAsValid();
        final List<Mute> existing = new ArrayList<Mute>(2);//One for existing, other expecting duplicate entries.
        for (final Mute mute : returnVal.getWallMutes()) {
            if (mute.equals(humanId)) {
                existing.add(mute);
            }
        }
        if (!returnVal.getWallMutes().removeAll(existing)) {
            throw DBDishonourCheckedException.REMOVING_A_NON_EXISTING_VALUE;
        }

        return returnVal;
    }

    private Wall rWallBadly(long wallId) {
        return crudServiceLocal_.findBadly(Wall.class, wallId);
    }

//    @Override
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public Wall doUClearWall(long wallId) throws DBDishonourCheckedException {
//        final Wall returnVal = crudServiceLocal_.findBadly(Wall.class, wallId);
//        returnVal.setWallContent("");
//        return returnVal;
//    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void doNTxDWall(long wallId) {
        crudServiceLocal_.delete(Wall.class, wallId);
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Msg> doRHumansWallLastEntries(final long wallId, final Integer numberOfEntriesToFetch) {

        final List<Msg> lastWallEntryInList = msgCrudServiceLocal_.findWithNamedQuery(Msg.FindWallEntriesByWallIdOrderByIdDesc,
                QueryParameter.newInstance().add(Wall.wallIdCOL, wallId).parameters(), numberOfEntriesToFetch);

        return lastWallEntryInList;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @WARNING("Reflect any changes on CRUDHumansWall as well")
    public void doUpdateMetadata(final long wallId, final String key, final String value) {
        if (key == null || key.isEmpty()) {
            throw new NullPointerException(key + IS_NULL_OR_EMPTY);
        }

        if (value == null || value.isEmpty()) {
            throw new NullPointerException(value + IS_NULL_OR_EMPTY);
        }

        final Wall wall = rWallBadly(wallId);
        final String wallMetadata = wall.getWallMetadata();
        if (wallMetadata == null || wallMetadata.isEmpty()) {
            wall.setWallMetadata(key + "=" + value);
        } else {
            final String[] pairs = wallMetadata.split(",");
            boolean present = false;
            boolean same = false;
            for (final String pairString : pairs) {
                final String[] pair = pairString.split("=");
                if (key.equals(pair[0])) {
                    present = true;
                    LetsWarnAnபInconsistencies:
                    {
                        if (!value.equals(pair[1])) {
                            throw new DBDishonourException("You tried to assign " + key + "=" + value + " to " + wall.toString() + " but it already contains a DIFFERENT VALUE for they key");
                        }
                    }
                }
            }
            if (!present) {
                wall.setWallMetadata(wall.getWallMetadata() + "," + key + "=" + value);
            }
        }
    }
}