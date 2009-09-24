package ai.ilikeplaces.security.blowfish.jbcrypt;

import ai.ilikeplaces.security.blowfish.BlowFishLocal;
import java.security.SecureRandom;

/**
 *
 * @author Ravindranath Akila
 */
public interface JBCryptWrapperFace extends BlowFishLocal{

    /**
     * Hash a password using the OpenBSD bcrypt scheme
     * @param password	the password to hash
     * @param salt	the salt to hash with (perhaps generated
     * using BCrypt.gensalt)
     * @return	the hashed password
     */
    public String hashpw(final String password, final String salt);

    /**
     * Generate a salt for use with the BCrypt.hashpw() method
     * @param log_rounds	the log2 of the number of rounds of
     * hashing to apply - the work factor therefore increases as
     * 2**log_rounds.
     * @param random		an instance of SecureRandom to use
     * @return	an encoded salt value
     */
    public String gensalt(final int log_rounds, final SecureRandom random);

    /**
     * Generate a salt for use with the BCrypt.hashpw() method
     * @param log_rounds	the log2 of the number of rounds of
     * hashing to apply - the work factor therefore increases as
     * 2**log_rounds.
     * @return	an encoded salt value
     */
    public String gensalt(final int log_rounds);

    /**
     * Generate a salt for use with the BCrypt.hashpw() method,
     * selecting a reasonable default for the number of hashing
     * rounds to apply
     * @return	an encoded salt value
     */
    public String gensalt();

    /**
     * Check that a plaintext password matches a previously hashed
     * one
     * @param plaintext	the plaintext password to verify
     * @param hashed	the previously-hashed password
     * @return	true if the passwords match, false otherwise
     */
    public boolean checkpw(final String plaintext, final String hashed);
}
