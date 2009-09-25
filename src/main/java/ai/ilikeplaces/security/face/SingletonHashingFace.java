package ai.ilikeplaces.security.face;

import javax.ejb.Local;

/**
 *
 * @author Ravindranath Akila
 */
@Local
public interface SingletonHashingFace {
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
    public String getHash(final String plaintext__, final String salt__);
}
