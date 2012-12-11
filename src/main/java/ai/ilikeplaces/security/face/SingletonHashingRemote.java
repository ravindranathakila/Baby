package ai.ilikeplaces.security.face;

import ai.scribble.License;

import javax.ejb.Remote;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Remote
public interface SingletonHashingRemote {
    final static public String NAME = SingletonHashingRemote.class.getSimpleName();

    /**
     * @param plaintext__
     * @param salt__
     * @return
     */
    public String getHash(final String plaintext__, final String salt__);
}
