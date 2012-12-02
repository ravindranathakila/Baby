package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.etc.EntityLifeCycleListener;
import ai.ilikeplaces.entities.etc.HumanPkJoinFace;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This entity is NOT related to a humans public location bookings.
 * This comment was placed here to avoid logic confusion.
 * <p/>
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Dec 6, 2009
 * Time: 6:11:09 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "HumansAlbum", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumansAlbum implements HumanPkJoinFace, Serializable {

    @Id
    @Column(name = "humanId")
    public String humanId;


    @OneToOne(mappedBy = Human.humansAlbumCOL, cascade = CascadeType.REFRESH)
    //@PrimaryKeyJoinColumn
    public Human human;


    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }


    public Human getHuman() {
        return human;
    }

    public void setHuman(final Human human) {
        this.human = human;
    }
}
