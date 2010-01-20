package ai.ilikeplaces.security.face;

import javax.ejb.Local;

/**
 *
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
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
