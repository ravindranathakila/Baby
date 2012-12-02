/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ilikeplaces.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Ravindranath Akila
 */
@Table(name = "HumanEmbedded", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumanEmbedded implements HumanPkJoinFace, Serializable {
// ------------------------------ FIELDS ------------------------------

    @Id
    @Column(name = "humanId")
    private String humanId;


    @OneToOne(mappedBy = Human.humansEm, cascade = CascadeType.REFRESH)
    //@PrimaryKeyJoinColumn
    private Human human;

// --------------------- GETTER / SETTER METHODS ---------------------

    public Human getHuman() {
        return human;
    }

    public void setHuman(final Human human) {
        this.human = human;
    }

    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }
}
