package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansWall;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.jpa.QueryParameter;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.jpa.RefreshException;
import ai.ilikeplaces.util.jpa.RefreshSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CRUDHumansWall extends AbstractSLBCallbacks implements CRUDHumansWallLocal {

    @EJB
    private CrudServiceLocal<HumansWall> humansWallCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<Wall> wallCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<Msg> crudServiceMsg_;

    public CRUDHumansWall() {
    }

    final static Logger logger = LoggerFactory.getLogger(CRUDHumansWall.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public HumansWall doRHumansWall(final String humanId, final RefreshSpec wallRefreshSpec) throws DBFetchDataException {
        final HumansWall humansWall = humansWallCrudServiceLocal_.find(HumansWall.class, humanId);
        try {
            humansWall.getWall().refresh(wallRefreshSpec);
        } catch (RefreshException e) {
            throw new DBFetchDataException(e);
        }
        return humansWall;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public HumansWall doRHumansWall(final String humanId) {
        final HumansWall humansWall = humansWallCrudServiceLocal_.find(HumansWall.class, humanId);
        humansWall.getWall().getWallMsgs().size();
        humansWall.getWall().getWallMutes().size();
        return humansWall;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Msg> doRHumansWallLastEntries(final String humanId, final Integer numberOfEntriesToFetch) {
        final HumansWall humansWall = humansWallCrudServiceLocal_.find(HumansWall.class, humanId);
        final Long humansWallId = humansWall.getWall().getWallId();

        final List<Msg> lastWallEntryInList = crudServiceMsg_.findWithNamedQuery(Msg.FindWallEntriesByWallIdOrderByIdDesc,
                QueryParameter.newInstance().add(Wall.wallIdCOL, humansWallId).parameters(), numberOfEntriesToFetch);

        return lastWallEntryInList;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long doDirtyRHumansWallID(final String humanId) throws DBDishonourCheckedException {
        return humansWallCrudServiceLocal_.findBadly(HumansWall.class, humanId).getWall().getWallId();
    }

}