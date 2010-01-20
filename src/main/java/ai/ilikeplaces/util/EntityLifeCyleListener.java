package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class EntityLifeCyleListener {

    public EntityLifeCyleListener() {
        //logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", EntityLifeCyleListener.class, this.hashCode());
    }

    @PrePersist
    public void PrePersist(final Object entity) {
        //logger.debug("PrePersist:{}.", entity);
    }

    @PostPersist
    public void PostPersist(final Object entity) {
        logger.debug("PostPersist:{}.", entity);

    }

    @PostLoad
    public void PostLoad(final Object entity) {
        logger.debug("PostLoad:{}.", entity);

    }

    @PreUpdate
    public void PreUpdate(final Object entity) {
        //logger.debug("PreUpdate:{}.", entity);

    }

    @PostUpdate
    public void PostUpdate(final Object entity) {
        logger.debug("PostUpdate:{}.", entity);

    }

    @PreRemove
    public void PreRemove(final Object entity) {
        //logger.debug("PreRemove:{}.", entity);

    }

    @PostRemove
    public void PostRemove(final Object entity) {
        logger.debug("PostRemove:{}.", entity);

    }

    final static Logger logger = LoggerFactory.getLogger(EntityLifeCyleListener.class);
}
