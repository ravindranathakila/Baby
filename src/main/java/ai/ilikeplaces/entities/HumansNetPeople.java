package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.UNIDIRECTIONAL;

import javax.persistence.*;
import java.util.List;

/**
 * These are the people needed for an event
 * <p/>
 * <p/>
 * User: Ravindranath Akila
 * Date: Dec 8, 2009
 * Time: 9:54:03 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
public class HumansNetPeople implements HumanPkJoinFace {
    private String humanId;
    private Human human;
    private List<HumansNetPeople> humansNetPeoples;

    @Id
    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    @OneToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
    public Human getHuman() {
        return human;
    }

    @Override
    public void setHuman(final Human human) {
        this.human = human;
    }

    @NOTE(note = "MANY IS THE OWNING SIDE, HENCE REFRESH. SINCE THIS IS SELF REFERENTIAL, A REFRESH WITH SELF SHOULD NOT HAPPEN.")
    @UNIDIRECTIONAL(note = "Asymmetric Relationship")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    public List<HumansNetPeople> getHumansNetPeoples() {
        return humansNetPeoples;
    }

    public void setHumansNetPeoples(final List<HumansNetPeople> humansNetPeoples) {
        this.humansNetPeoples = humansNetPeoples;
    }
}
