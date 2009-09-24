package ai.ilikeplaces.security.face;

import javax.ejb.Local;

/**
 *
 * @author Ravindranath Akila
 */
@Local
public interface SingletonHashingFace {
    public String getHash(final String plaintext__, final String salt__);
}
