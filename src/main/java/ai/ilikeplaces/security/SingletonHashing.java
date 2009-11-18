package ai.ilikeplaces.security;

import ai.ilikeplaces.exception.ConstructorInvokationException;
import ai.ilikeplaces.util.AbstractSNGLTNBCallbacks;
import ai.ilikeplaces.security.blowfish.BlowFishLocal;
import ai.ilikeplaces.security.face.SingletonHashingFace;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;

/**
 *
 * @author Ravindranath Akila
 */
@Singleton
public class SingletonHashing extends AbstractSNGLTNBCallbacks implements SingletonHashingFace {

    @EJB
    private BlowFishLocal blowFishLocal;

    /**
     *
     */
    /**
     *
     */
    @PostConstruct
    @Override
    public void postConstruct(){
        boolean initializeFailed = true;
        final StringBuilder log = new StringBuilder();
        init:
        {
            if (this.blowFishLocal == null) {
                log.append("\nSORRY! I COULDN'T INITIALIZED VARIBALE blowFishLocal WHICH IS NOW " + this.blowFishLocal);
                break init;
            }

            /**
             * break. Do not let this statement be reachable if initialization
             * failed. Instead, break immediately where initialization failed.
             * At this point, we set the initializeFailed to false and thereby,
             * allow initialization of an instance
             */
            initializeFailed = false;
        }
        if (initializeFailed) {
            throw new ConstructorInvokationException(log.toString());
        }
        System.out.println(log);
    }

    /**
     *
     * @param plaintext__
     * @param salt__
     * @return
     */
    /**
     *
     * @param plaintext__
     * @param salt__
     * @return
     */
    @Override
    synchronized public String getHash(final String plaintext__, final String salt__) {
        return blowFishLocal.getHash(new String(plaintext__), new String(salt__));/*We would not want an interned string here for hashing! So we use new.*/
    }
}
