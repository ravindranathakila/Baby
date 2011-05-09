package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;

/**
 * Okay, we need to
 *
 * 1. Remember that entries to this entity will be foreign keys. That is, so that anybody can use this entity.
 *
 * 2. This entity should be super efficient since it will be used everywhere.
 *
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 25, 2010
 * Time: 1:01:22 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class LongMsg {

    public Long longMsgId;
    public static final String longMsgIdCOL = "longMsgId";

    public Integer longMsgType;//Wall, Personal

    public String longMsgContent;

    public String longMsgMetadata;//Anybody can store relavant metadata here


    final static public int longMsgTypeHUMAN = 1;
    final static public int longMsgTypeMISC = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getLongMsgId() {
        return longMsgId;
    }

    public void setLongMsgId(final Long longMsgId) {
        this.longMsgId = longMsgId;
    }
    public LongMsg setLongMsgIdR(final Long longMsgId) {
        this.longMsgId = longMsgId;
        return this;

    }

    public Integer getLongMsgType() {
        return longMsgType;
    }

    public void setLongMsgType(final Integer longMsgType) {
        this.longMsgType = longMsgType;
    }

    public LongMsg setLongMsgTypeR(final Integer longMsgType) {
        this.longMsgType = longMsgType;
        return this;
    }

    public String getLongMsgContent() {
        return longMsgContent;
    }

    public void setLongMsgContent(final String longMsgContent) {
        this.longMsgContent = longMsgContent;
    }

    @Column(length = 10240)
    public LongMsg setLongMsgContentR(final String longMsgContent) {
        this.longMsgContent = longMsgContent;
        return this;
    }

    public String getLongMsgMetadata() {
        return longMsgMetadata;
    }

    public void setLongMsgMetadata(final String longMsgMetadata) {
        this.longMsgMetadata = longMsgMetadata;
    }
    public LongMsg setLongMsgMetadataR(final String longMsgMetadata) {
        this.longMsgMetadata = longMsgMetadata;
        return this;

    }
}