/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "HumansPrivatePhoto", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumansPrivatePhoto implements HumanIdFace, Serializable {
// ------------------------------ FIELDS ------------------------------

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "humanId")
    public String humanId;
    public static final String humanIdCOL = "humanId";

    @OneToOne(mappedBy = Human.humansPrivatePhotoCOL, cascade = CascadeType.REFRESH)
    //@PrimaryKeyJoinColumn
    public Human human;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = humanIdCOL)
    public List<PrivatePhoto> privatePhotos;

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

    public List<PrivatePhoto> getPrivatePhotos() {
        return privatePhotos;
    }

    public void setPrivatePhotos(List<PrivatePhoto> privatePhotos) {
        this.privatePhotos = privatePhotos;
    }


}
