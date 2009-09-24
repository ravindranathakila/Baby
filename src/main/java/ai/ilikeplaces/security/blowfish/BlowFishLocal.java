package ai.ilikeplaces.security.blowfish;

import javax.ejb.Local;

/**
 *
 * @author Ravindranath Akila
 */
@Local
public interface BlowFishLocal {

    /**
     * General Call on Any BlowFish library. Wrapper method.
     * @param passWord
     * @return
     */
    public String getHash(final String passWord, final String salt);
}
