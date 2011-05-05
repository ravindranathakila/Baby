package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansWall;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
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
public class CRUDHumansWall extends AbstractSLBCallbacks implements CRUDHumansWallLocal {

    @EJB
    private CrudServiceLocal<HumansWall> humansWallCrudServiceLocal_;

    public CRUDHumansWall() {
    }

    final static Logger logger = LoggerFactory.getLogger(CRUDHumansWall.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public HumansWall dirtyRHumansWall(final String humanId) {
        return humansWallCrudServiceLocal_.find(HumansWall.class, humanId);
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public HumansWall rHumansWall(final String humanId) throws DBDishonourCheckedException {
        return humansWallCrudServiceLocal_.findBadly(HumansWall.class, humanId);
    }

    public Long dirtyRHumansWallID(final String humanId) throws DBDishonourCheckedException {
        return humansWallCrudServiceLocal_.findBadly(HumansWall.class, humanId).getWall().getWallId();
    }

}