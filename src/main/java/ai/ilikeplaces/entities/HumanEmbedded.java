/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ilikeplaces.entities;

import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 *
 * @author Ravindranath Akila
 */
@Entity
public class HumanEmbedded implements HumanPkJoinFace, Serializable {

    private String humanId;

    private Human human;


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

    public void setHuman(final Human human) {
        this.human = human;
    }
}