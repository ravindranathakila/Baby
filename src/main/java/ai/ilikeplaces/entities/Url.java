package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners(EntityLifeCycleListener.class)
public class Url implements Serializable {

    public String url;

    public long type;

    public String metadata;

    public static final int typeMISC= 0;
    public static final int typeHUMAN= 1;

    @Id
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Transient
    public Url setUrlR(final String url) {
        this.url = url;
        return this;
    }

    public long getType() {
        return type;
    }

    public void setType(final long type) {
        this.type = type;
    }

    @Transient
    public Url setTypeR(final long type) {
        this.type = type;
        return this;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(final String metadata) {
        this.metadata = metadata;
    }

    @Transient
    public Url setMetadataR(final String metadata) {
        this.metadata = metadata;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Url url1 = (Url) o;

        if (url != null ? !url.equals(url1.url) : url1.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Url{" +
                "url='" + url + '\'' +
                ", type=" + type +
                '}';
    }
}