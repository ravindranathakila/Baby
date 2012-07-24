package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

import javax.persistence.*;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class EntityLifeCycleListener {

    public static final RefObj<Boolean> PRE_ACTIONS = new Obj<Boolean>(true) {

        @Override
        public void setObj(final Boolean status) {
            if (status != null) {
                obj = status;
            } else {
                throw new SecurityException("SORRY! YOU CANNOT ASSIGN A NULL.");
            }
        }
    };

    public static final RefObj<Boolean> POST_ACTIONS = new Obj<Boolean>(true) {

        @Override
        public void setObj(final Boolean status) {
            if (status != null) {
                obj = status;
            } else {
                throw new SecurityException("SORRY! YOU CANNOT ASSIGN A NULL.");
            }
        }
    };

    public EntityLifeCycleListener() {
    }

    @PrePersist
    public void PrePersist(final Object entity) {
        if (PRE_ACTIONS.getObj()) {
            Loggers.DEBUG.debug("PrePersist:{}.", entity);
            System.out.println("PrePersist:" + entity);//For Hazelcast until proper logging is done
        }
    }

    @PostPersist
    public void PostPersist(final Object entity) {
        if (POST_ACTIONS.getObj()) {
            Loggers.DEBUG.debug("PostPersist:{}.", entity);
            System.out.println("PostPersist:" + entity);//For Hazelcast until proper logging is done
        }

    }

    @PostLoad
    public void PostLoad(final Object entity) {
        if (POST_ACTIONS.getObj()) {
            Loggers.DEBUG.debug("PostLoad:{}.", entity);
            System.out.println("PostLoad:" + entity);//For Hazelcast until proper logging is done
        }
    }

    @PreUpdate
    public void PreUpdate(final Object entity) {
        if (PRE_ACTIONS.getObj()) {
            Loggers.DEBUG.debug("PreUpdate:{}.", entity);
            System.out.println("PreUpdate:" + entity);//For Hazelcast until proper logging is done
        }

    }

    @PostUpdate
    public void PostUpdate(final Object entity) {
        if (POST_ACTIONS.getObj()) {
            Loggers.DEBUG.debug("PostUpdate:{}.", entity);
            System.out.println("PostUpdate:" + entity);//For Hazelcast until proper logging is done
        }

    }

    @PreRemove
    public void PreRemove(final Object entity) {
        if (PRE_ACTIONS.getObj()) {
            Loggers.DEBUG.debug("PreRemove:{}.", entity);
            System.out.println("PreRemove:" + entity);//For Hazelcast until proper logging is done
        }
    }

    @PostRemove
    public void PostRemove(final Object entity) {
        if (POST_ACTIONS.getObj()) {
            Loggers.DEBUG.debug("PostRemove:{}.", entity);
            System.out.println("PostRemove:" + entity);//For Hazelcast until proper logging is done
        }
    }
}
