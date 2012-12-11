package ai.ilikeplaces.security.blowfish;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface BlowFishLocal {

    /**
     * General Call on Any BlowFish library. Wrapper method.
     *
     * @param passWord
     * @param salt
     * @param salt
     * @param salt
     * @return
     */
    public String getHash(final String passWord, final String salt);
}
