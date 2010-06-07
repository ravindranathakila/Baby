package ai.ilikeplaces.security.face;

import ai.ilikeplaces.doc.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface SingletonHashingFace {
    final static public String NAME = "SingletonHashingLocal";
    
    /**
     * @param plaintext__
     * @param salt__
     * @return
     */
    public String getHash(final String plaintext__, final String salt__);
}
