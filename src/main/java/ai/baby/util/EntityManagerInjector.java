package ai.baby.util;

import ai.scribble.License;

import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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

        private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ilpMainSchema");
//        private final EntityManagerFactory entityManagerFactory = new EntityManagerFactoryImpl("ilpMainSchema", new HashMap<String, Object>());

//        final EntityManagerFactory entityManagerFactory;
//
//        {
//            final Map<String, String> properties = new HashMap<String, String>();
//            properties.put("kundera.nodes", "localhost");
//            properties.put("kundera.port", "60000");
//            properties.put("kundera.keyspace", "KunderaKeyspace");
//            properties.put("kundera.dialect", "hbase");
//            properties.put("kundera.client.lookup.class", "com.impetus.client.hbase.HBaseClientFactory");
//            properties.put("kundera.cache.provider.class", "com.impetus.kundera.cache.ehcache.EhCacheProvider");
//            properties.put("kundera.cache.config.resource", "/ehcache-test.xml");
//            properties.put("kundera.ddl.auto.prepare", "create");
//
//            entityManagerFactory = Persistence.createEntityManagerFactory("ilpMainSchema", properties);
//        }


        @Override
        protected EntityManager initialValue() {
            return entityManagerFactory.createEntityManager();
        }

        public EntityManager setEntityManager(final EntityManager entityManager) {
            return super.get();
        }

        public EntityManager getEntityManager() {
            return super.get();
        }

        public void removeEntityManager() {
            super.get().flush();
            super.get().close();
        }
    };

    //@AroundInvoke
    public Object injectEntityManager(final InvocationContext invocation) throws Exception {
        boolean joinedTransaction = false;

        try {
            final EntityManager entityManager = THREAD_SAFE_ENTITY_MANAGER.get();

            if (entityManager.getTransaction().isActive()) {
                entityManager.joinTransaction();
                joinedTransaction = true;
            } else {
                entityManager.getTransaction().begin();
            }


            Loggers.INFO.info("*****************************");
            Loggers.INFO.info("*****************************");
            Loggers.INFO.info("*****************************");
            Loggers.INFO.info("***********" + invocation.getMethod().getName() + "************");
            Loggers.INFO.info("***********" + Thread.currentThread().getName() + "************");
            Loggers.INFO.info("***********" + Thread.currentThread().getId() + "************");
            Loggers.INFO.info("***********" + entityManager.toString() + "************");
            Loggers.INFO.info("***********" + "Joined? " + joinedTransaction + "************");
            Loggers.INFO.info("*****************************");
            Loggers.INFO.info("*****************************");
            Loggers.INFO.info("*****************************");

            final Object proceed = invocation.proceed();

            return proceed;
        } catch (final Throwable t) {
            Loggers.ERROR.error("Problem in transaction", t);
            throw new RuntimeException(t);
        } finally {
            final EntityManager entityManager = THREAD_SAFE_ENTITY_MANAGER.get();
            final EntityTransaction transaction = entityManager.getTransaction();
            if (transaction.isActive()) {
                if (!joinedTransaction) {
                    transaction.commit();
                    entityManager.close();
                    THREAD_SAFE_ENTITY_MANAGER.remove();
                }
            }
        }

    }
}
