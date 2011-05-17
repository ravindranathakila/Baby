package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Mute;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
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

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CRUDWall extends AbstractSLBCallbacks implements CRUDWallLocal {

    @EJB
    private CrudServiceLocal<Wall> crudServiceLocal_;

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

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Deprecated
    public Wall doNTxUAppendToWall(long wallId, String contentToBeAppended) throws DBDishonourCheckedException {
        final Wall returnVal = crudServiceLocal_.findBadly(Wall.class, wallId);
        final int wallLength = returnVal.getWallContent().length();
        if (wallLength > (Wall.WALL_LENGTH - 1) - contentToBeAppended.length()) {
            Loggers.DEBUG.debug("Trimming long wall content.");
            returnVal.setWallContent(returnVal.getWallContent().subSequence(
                    contentToBeAppended.length() - 1, wallLength - 1)
                    .toString());
        }
        returnVal.setWallContent(returnVal.getWallContent() + contentToBeAppended);
        return returnVal;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Wall doUAddEntry(long wallId, final String humanId, String contentToBeAppended) throws DBDishonourCheckedException {
        final Wall returnVal = crudServiceLocal_.findBadly(Wall.class, wallId);
        final Msg msg = new Msg();

        msg
                .setMsgContentR(contentToBeAppended)
                .setMsgTypeR(Msg.msgTypeHUMAN)
                .setMsgMetadata(humanId);

        returnVal.getWallMsgs().size();//refreshing
        returnVal.getWallMsgs().add(msg);
        return returnVal;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Wall doUAddMuteEntry(long wallId, final String mutee) throws DBDishonourCheckedException {
        final Wall returnVal = crudServiceLocal_.findBadly(Wall.class, wallId);

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
        final Wall returnVal = crudServiceLocal_.findBadly(Wall.class, wallId);
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

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Wall doUClearWall(long wallId) throws DBDishonourCheckedException {
        final Wall returnVal = crudServiceLocal_.findBadly(Wall.class, wallId);
        returnVal.setWallContent("");
        return returnVal;
    }

    @Override
    public void doNTxDWall(long wallId) {
        crudServiceLocal_.delete(Wall.class, wallId);
    }


}            