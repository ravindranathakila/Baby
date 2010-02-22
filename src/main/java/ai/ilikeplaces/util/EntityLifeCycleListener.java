package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

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
        }
    }

    @PostPersist
    public void PostPersist(final Object entity) {
        //logger.debug("PostPersist:{}.", entity);

    }

    @PostLoad
    public void PostLoad(final Object entity) {
        if (POST_ACTIONS.getObj()) {
            Loggers.DEBUG.debug("PostLoad:{}.", entity);
        }
    }

    @PreUpdate
    public void PreUpdate(final Object entity) {
        if (PRE_ACTIONS.getObj()) {
            Loggers.DEBUG.debug("PreUpdate:{}.", entity);
        }

    }

    @PostUpdate
    public void PostUpdate(final Object entity) {
        //logger.debug("PostUpdate:{}.", entity);

    }

    @PreRemove
    public void PreRemove(final Object entity) {
        if (PRE_ACTIONS.getObj()) {
            Loggers.DEBUG.debug("PreRemove:{}.", entity);
        }

    }

    @PostRemove
    public void PostRemove(final Object entity) {
        //logger.debug("PostRemove:{}.", entity);

    }
}
