package ai.ilikeplaces.security;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.security.blowfish.BlowFishLocal;
import ai.ilikeplaces.security.face.SingletonHashingRemote;
import ai.ilikeplaces.util.AbstractSNGLTNBCallbacks;
import ai.ilikeplaces.util.LogNull;
import ai.ilikeplaces.util.Loggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
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
