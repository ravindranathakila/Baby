package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.entities.Map;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Dec 19, 2009
 * Time: 4:19:09 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({MethodTimer.class, MethodParams.class})
public class HumanCRUDMap extends AbstractSLBCallbacks implements HumanCRUDMapLocal {

    @EJB
    private CrudServiceLocal<Map> mapCrudServiceLocal;


    @Override
    public Return createEntry(final String label, final String entry) {
        Return r;
        try {
            final Map map = new Map();
            map.setLabel(label);
            map.setEntry(entry);
            r = new ReturnImpl<Map>(mapCrudServiceLocal.create(map), "Map entry save successful!");
        } catch (final Exception e) {
            INFO.error("DB ERROR! {}", e);
            r = new ReturnImpl<Map>(e, "Map entry save FAILED!", true);
        }

        return r;
    }


}