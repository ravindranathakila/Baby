package ai.ilikeplaces.entities;

import ai.ilikeplaces.util.EntityLifeCyleListener;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 12, 2010
 * Time: 8:19:40 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners(EntityLifeCyleListener.class)
public class PrivateEvent {

    private Long privateEventId;

    private Human human;

    private List<Human> privateEventOwners;

    private List<Human> privateEventAttendees;

    private List<Human> privateEventAccepts;

    private List<Human> privateEventRejects;

    private HumansPrivateLocation privateLocation;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getPrivateEventId() {
        return privateEventId;
    }

    public void setPrivateEventId(final Long privateEventId) {
        this.privateEventId = privateEventId;
    }

    public HumansPrivateLocation getPrivateLocation() {
        return privateLocation;
    }

    public void setPrivateLocation(final HumansPrivateLocation privateLocation) {
        this.privateLocation = privateLocation;
    }

    public List<Human> getPrivateEventAccepts() {
        return privateEventAccepts;
    }

    public void setPrivateEventAccepts(final List<Human> privateEventAccepts) {
        this.privateEventAccepts = privateEventAccepts;
    }

    public List<Human> getPrivateEventAttendees() {
        return privateEventAttendees;
    }

    public void setPrivateEventAttendees(final List<Human> privateEventAttendees) {
        this.privateEventAttendees = privateEventAttendees;
    }

    public List<Human> getPrivateEventOwners() {
        return privateEventOwners;
    }

    public void setPrivateEventOwners(final List<Human> privateEventOwners) {
        this.privateEventOwners = privateEventOwners;
    }

    public List<Human> getPrivateEventRejects() {
        return privateEventRejects;
    }

    public void setPrivateEventRejects(final List<Human> privateEventRejects) {
        this.privateEventRejects = privateEventRejects;
    }

    @Override
    public String toString() {
        return "PrivateEvent{" +
                "human=" + human +
                ", privateEventId=" + privateEventId +
                ", privateLocation=" + privateLocation +
                '}';
    }
}
