package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Okay, we need to
 * <p/>
 * 1. Remember that entries to this entity will be foreign keys. That is, so that anybody can use this entity.
 * <p/>
 * 2. This entity should be super efficient since it will be used everywhere.
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 25, 2010
 * Time: 1:01:22 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "LongMsg", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class LongMsg implements Serializable {
// ------------------------------ FIELDS ------------------------------

    public static final String longMsgIdCOL = "longMsgId";


    final static public int longMsgTypeHUMAN = 1;
    final static public int longMsgTypeMISC = 0;


    @Id
    @Column(name = "longMsgId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long longMsgId;

    @Column(name = "longMsgType")
    public Integer longMsgType;//Wall, Personal


    @Column(name = "longMsgContent", length = 10240)
    @Lob
    public String longMsgContent;

    @Column(name = "longMsgMetadata")
    public String longMsgMetadata;//Anybody can store relavant metadata here

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getLongMsgContent() {
        return longMsgContent;
    }

    public void setLongMsgContent(final String longMsgContent) {
        this.longMsgContent = longMsgContent;
    }

    public Long getLongMsgId() {
        return longMsgId;
    }

    public void setLongMsgId(final Long longMsgId) {
        this.longMsgId = longMsgId;
    }

    public String getLongMsgMetadata() {
        return longMsgMetadata;
    }

    public void setLongMsgMetadata(final String longMsgMetadata) {
        this.longMsgMetadata = longMsgMetadata;
    }

    public Integer getLongMsgType() {
        return longMsgType;
    }

    public void setLongMsgType(final Integer longMsgType) {
        this.longMsgType = longMsgType;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "LongMsg{" +
                "longMsgId=" + longMsgId +
                ", longMsgType=" + longMsgType +
                ", longMsgContent='" + longMsgContent + '\'' +
                ", longMsgMetadata='" + longMsgMetadata + '\'' +
                '}';
    }

// -------------------------- OTHER METHODS --------------------------

    public LongMsg setLongMsgContentR(final String longMsgContent) {
        this.longMsgContent = longMsgContent;
        return this;
    }

    public LongMsg setLongMsgIdR(final Long longMsgId) {
        this.longMsgId = longMsgId;
        return this;
    }

    public LongMsg setLongMsgMetadataR(final String longMsgMetadata) {
        this.longMsgMetadata = longMsgMetadata;
        return this;
    }

    public LongMsg setLongMsgTypeR(final Integer longMsgType) {
        this.longMsgType = longMsgType;
        return this;
    }
}
