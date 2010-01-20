package ai.ilikeplaces.security.blowfish.jbcrypt;

import ai.ilikeplaces.util.AbstractSNGLTNBCallbacks;
import ai.ilikeplaces.security.blowfish.BlowFishLocal;
import java.security.SecureRandom;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
/**
 *
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Singleton
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BlowFish extends AbstractSNGLTNBCallbacks implements BlowFishLocal {

    /**
     * Hash a password using the OpenBSD bcrypt scheme
     * @param password	the password to hash
     * @param salt	the salt to hash with (perhaps generated
     * using BCrypt.gensalt)
     * @return	the hashed password
     */
    public String hashpw(final String password, final String salt) {
        return BCrypt.hashpw(password, salt);
    }

    /**
     * Generate a salt for use with the BCrypt.hashpw() method
     * @param log_rounds	the log2 of the number of rounds of
     * hashing to apply - the work factor therefore increases as
     * 2**log_rounds.
     * @param random		an instance of SecureRandom to use
     * @return	an encoded salt value
     */
    public String gensalt(final int log_rounds, final SecureRandom random) {
        return BCrypt.gensalt(log_rounds, random);
    }

    /**
     * Generate a salt for use with the BCrypt.hashpw() method
     * @param log_rounds	the log2 of the number of rounds of
     * hashing to apply - the work factor therefore increases as
     * 2**log_rounds.
     * @return	an encoded salt     @Override value
     */
    public String gensalt(final int log_rounds) {
        return BCrypt.gensalt(log_rounds);
    }

    /**
     * Generate a salt for use with the BCrypt.hashpw() method,
     * selecting a reasonable default for the number of hashing
     * rounds to apply
     * @return	an encoded salt value
     */
    public String gensalt() {
        return BCrypt.gensalt();
    }

    /**
     * Check that a plaintext password matches a previously hashed
     * one
     * @param plaintext	the plaintext password to verify
     * @param hashed	the previously-hashed password
     * @return	true if the passwords match, false otherwise
     */
    public boolean checkpw(final String plaintext, final String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }

    /**
     * General Call on Any BlowFish library. Wrapper method.
     * @param passWord
     * @param salt 
     * @return
     */
    @Override
    public String getHash(final String passWord, final String salt){
        final String hash = new String(BCrypt.hashpw(new String(passWord), salt));
        if(!BCrypt.checkpw(new String(passWord), new String(hash))){
            throw new java.lang.RuntimeException("SORRY! I AM ENCOUNTERING A HASHING ERROR!");
        } else {
            logger.info("HASH:"+hash);
            logger.info("SALT:"+salt);
        }
        return hash;
    }
}
