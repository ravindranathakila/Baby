package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 25, 2010
 * Time: 1:01:22 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
@NOTE(note = "Wall is initially a plain String. Each text is appended, hence non editable." +
        "A wall can be 'cleared' by a owner." +
        "This approach was taken to reduce TTM.")
public class Wall {
    public Long wallId = null;
    public String wallContent = null;

    final static public int WALL_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getWallId() {
        return wallId;
    }

    public void setWallId(Long wallId) {
        this.wallId = wallId;
    }

    @Column(length = WALL_LENGTH)
    public String getWallContent() {
        return wallContent;
    }

    public void setWallContent(String wallContent) {
        this.wallContent = wallContent;
    }

    @Transient
    public Wall setWallRContent(String wallContent) {
        this.wallContent = wallContent;
        return this;
    }

    @Override
    public String toString() {
        return "Wall{" +
                "wallId=" + wallId +
                '}';
    }
}