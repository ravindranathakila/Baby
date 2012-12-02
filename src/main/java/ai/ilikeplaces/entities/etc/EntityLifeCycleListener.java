package ai.ilikeplaces.entities.etc;

import ai.ilikeplaces.doc.License;

import javax.persistence.*;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class EntityLifeCycleListener {

    public static boolean PRE_ACTIONS = false;

    public static boolean POST_ACTIONS = false;

    public EntityLifeCycleListener() {
    }

    @PrePersist
    public void PrePersist(final Object entity) {
        if (PRE_ACTIONS) {
            System.out.println("PrePersist:" + entity);//For Hazelcast until proper logging is done
        }
    }

    @PostPersist
    public void PostPersist(final Object entity) {
        if (POST_ACTIONS) {
            System.out.println("PostPersist:" + entity);//For Hazelcast until proper logging is done
        }

    }

    @PostLoad
    public void PostLoad(final Object entity) {
        if (POST_ACTIONS) {
            System.out.println("PostLoad:" + entity);//For Hazelcast until proper logging is done
        }
    }

    @PreUpdate
    public void PreUpdate(final Object entity) {
        if (PRE_ACTIONS) {
            System.out.println("PreUpdate:" + entity);//For Hazelcast until proper logging is done
        }

    }

    @PostUpdate
    public void PostUpdate(final Object entity) {
        if (POST_ACTIONS) {
            System.out.println("PostUpdate:" + entity);//For Hazelcast until proper logging is done
        }

    }

    @PreRemove
    public void PreRemove(final Object entity) {
        if (PRE_ACTIONS) {
            System.out.println("PreRemove:" + entity);//For Hazelcast until proper logging is done
        }
    }

    @PostRemove
    public void PostRemove(final Object entity) {
        if (POST_ACTIONS) {
            System.out.println("PostRemove:" + entity);//For Hazelcast until proper logging is done
        }
    }
}
