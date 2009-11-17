package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Human;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDHumanLocal {

    final static public String NAME = HumanCRUDHumanLocal.class.getSimpleName();

    public Human doDirtyRHuman(final String humanId);

    public void doCHuman(final String username, final String password) throws IllegalAccessException;

    public boolean doDirtyCheckHuman(final String humanId);
}
