package ai.ilikeplaces.security;

import ai.ilikeplaces.util.blowfish.BlowFishLocal;
import ai.ilikeplaces.security.face.SingletonHashingRemote;
import ai.ilikeplaces.util.AbstractSNGLTNBCallbacks;
import ai.scribble.License;
import ai.scribble._ok;

import javax.ejb.EJB;
import javax.ejb.Singleton;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_ok
@Singleton
public class SingletonHashing extends AbstractSNGLTNBCallbacks implements SingletonHashingRemote {

    @EJB
    private BlowFishLocal blowFishLocal;

    /**
     * @param plaintext__
     * @param salt__
     * @return
     */
    @Override
    synchronized public String getHash(final String plaintext__, final String salt__) {
        return blowFishLocal.getHash(new String(plaintext__), new String(salt__));/*We would not want an interned string here for hashing! So we use new.*/
    }
}
