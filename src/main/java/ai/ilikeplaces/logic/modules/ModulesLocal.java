package ai.ilikeplaces.logic.modules;

import ai.doc.License;

import javax.ejb.Local;

/**
 *
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface ModulesLocal {

    public static final String NAME = ModulesLocal.class.getSimpleName();

    public ai.ilikeplaces.ygp.impl.ClientFactory getYahooGeoPlanetFactory();

    public upcoming.yahoo.api.impl.ClientFactory getYahooUplcomingFactory();

    public com.disqus.api.impl.ClientFactory getDisqusAPIFactory();

    public com.google.places.api.impl.ClientFactory getGooglePlacesAPIFactory();
}
