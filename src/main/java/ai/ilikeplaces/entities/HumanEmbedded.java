/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ilikeplaces.entities;

import ai.ilikeplaces.util.EntityLifeCycleListener;

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
    private String humanId;


    @OneToOne(cascade = CascadeType.REFRESH)
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
