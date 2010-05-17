package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.Loggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CRUDWall extends AbstractSLBCallbacks implements CRUDWallLocal {

    @EJB
    private CrudServiceLocal<Wall> crudServiceLocal_;

    public CRUDWall() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", CRUDWall.class, this.hashCode());
    }

    final static Logger logger = LoggerFactory.getLogger(CRUDWall.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Wall doNTxCWall(final Wall wall) {
        return crudServiceLocal_.create(wall);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Wall doDirtyRWall(long wallId) {
        return crudServiceLocal_.findBadly(Wall.class, wallId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Wall doNTxUAppendToWall(long wallId, String contentToBeAppended) {
        final Wall returnVal = crudServiceLocal_.findBadly(Wall.class, wallId);
        final int wallLength = returnVal.getWallContent().length();
        if ( wallLength > (Wall.WALL_LENGTH - 1) - contentToBeAppended.length()) {
            Loggers.DEBUG.debug("Trimming long wall content.");
            returnVal.setWallContent(returnVal.getWallContent().subSequence(
                    contentToBeAppended.length() - 1, wallLength - 1)
                    .toString());
        }
        returnVal.setWallContent(returnVal.getWallContent() + contentToBeAppended);
        return returnVal;
    }

    @Override
    public Wall doNTxUClearWall(long wallId) {
        final Wall returnVal = crudServiceLocal_.findBadly(Wall.class, wallId);
        returnVal.setWallContent("");
        return returnVal;
    }

    @Override
    public void doNTxDWall(long wallId) {
        crudServiceLocal_.delete(Wall.class, wallId);
    }


}