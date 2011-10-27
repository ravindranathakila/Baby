package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansTribe;
import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.jpa.CrudService;
import ai.ilikeplaces.jpa.CrudServiceLocal;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/27/11
 * Time: 7:33 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CRUDTribe implements CRUDTribeLocal {

    @EJB
    private CrudServiceLocal<Tribe> tribeCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<HumansTribe> humansTribeCrudServiceLocal__;
}
