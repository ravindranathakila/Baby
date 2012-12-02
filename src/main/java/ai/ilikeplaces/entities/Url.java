package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.etc.EntityLifeCycleListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "Url", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class Url implements Serializable {
// ------------------------------ FIELDS ------------------------------

    public static final int typeMISC = 0;
    public static final int typeHUMAN = 1;

    @Id
    @Column(name = "url")
    public String url;

    @Column(name = "type")
    public long type;

    @Column(name = "metadata")
    public String metadata;

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(final String metadata) {
        this.metadata = metadata;
    }

    public long getType() {
        return type;
    }

    public void setType(final long type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Url url1 = (Url) o;

        return !(url != null ? !url.equals(url1.url) : url1.url != null);
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

// -------------------------- OTHER METHODS --------------------------

    @Transient
    public Url setMetadataR(final String metadata) {
        this.metadata = metadata;
        return this;
    }

    @Transient
    public Url setTypeR(final long type) {
        this.type = type;
        return this;
    }

    @Transient
    public Url setUrlR(final String url) {
        this.url = url;
        return this;
    }
}
