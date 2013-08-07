package ai.ilikeplaces.logic.modules;

import ai.scribble.License;

import javax.ejb.Local;

/**
 *
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface ModulesLocal {

    public static final String NAME = ModulesLocal.class.getSimpleName();

    public api.foursquare.com.impl.ClientFactory getFoursquareFactory();
}
