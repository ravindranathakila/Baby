package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 15, 2010
 * Time: 1:14:18 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class EntityManagerInjector {

    public static final ThreadLocal<EntityManager> THREAD_SAFE_ENTITY_MANAGER = new ThreadLocal<EntityManager>() {

        private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU_DN");

        @Override
        public EntityManager get() {
            return entityManagerFactory.createEntityManager();
        }

        @Override
        public void remove() {
            get().flush();
            get().close();
        }
    };

    @AroundInvoke
    public Object injectEntityManager(final InvocationContext invocation) throws Exception {

        try {
            final EntityManager entityManager = THREAD_SAFE_ENTITY_MANAGER.get();


//            if (entityManager.getTransaction().isActive()) {
//                entityManager.joinTransaction();
//            }

            entityManager.getTransaction().begin();


            Loggers.INFO.info("*****************************");
            Loggers.INFO.info("*****************************");
            Loggers.INFO.info("*****************************");
            Loggers.INFO.info("***********" + Thread.currentThread().getName() + "************");
            Loggers.INFO.info("***********" + Thread.currentThread().getId() + "************");
            Loggers.INFO.info("***********" + entityManager.toString() + "************");
            Loggers.INFO.info("*****************************");
            Loggers.INFO.info("*****************************");
            Loggers.INFO.info("*****************************");

            return invocation.proceed();
        } catch (final Throwable t) {
            Loggers.ERROR.error("Problem in transaction", t);
            throw new RuntimeException(t);
        } finally {
            if (THREAD_SAFE_ENTITY_MANAGER.get().getTransaction().isActive()) {
                THREAD_SAFE_ENTITY_MANAGER.get().getTransaction().commit();
            }
            //THREAD_SAFE_ENTITY_MANAGER.get().close();
            //THREAD_SAFE_ENTITY_MANAGER.remove();
        }

    }
}
