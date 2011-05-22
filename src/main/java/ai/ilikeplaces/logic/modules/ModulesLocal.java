package ai.ilikeplaces.logic.modules;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.ygp.impl.ClientFactory;

import javax.ejb.Local;

/**
 *
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface ModulesLocal {

    public static final String NAME = ModulesLocal.class.getSimpleName();

    public ai.ilikeplaces.ygp.impl.ClientFactory getYahooGeoPlanetFactory();

    public com.disqus.api.impl.ClientFactory getDisqusAPIFactory();

    public com.google.places.api.impl.ClientFactory getGooglePlacesAPIFactory();
}