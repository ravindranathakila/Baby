package ai.ilikeplaces.security;

import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.security.blowfish.BlowFishLocal;
import ai.ilikeplaces.security.face.SingletonHashingFace;
import ai.ilikeplaces.util.AbstractSNGLTNBCallbacks;

import javax.ejb.EJB;
import javax.ejb.Singleton;

/**
 * @author Ravindranath Akila
 */
@OK
@Singleton
public class SingletonHashing extends AbstractSNGLTNBCallbacks implements SingletonHashingFace {

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
