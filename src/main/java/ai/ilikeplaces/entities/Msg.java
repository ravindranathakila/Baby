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
@Table(name = "Msg", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@NamedQuery(name = "FindWallEntriesByWallIdOrderByIdDesc",
        query = "SELECT msg FROM Msg msg")
@EntityListeners({EntityLifeCycleListener.class})
public class Msg implements Serializable {
// ------------------------------ FIELDS ------------------------------

    public static final String msgIdCOL = "msgId";

    final static public String FindWallEntriesByWallIdOrderByIdDesc = "FindWallEntriesByWallIdOrderByIdDesc";


    final static public int msgTypeHUMAN = 1;
    final static public int msgTypeMISC = 0;

    @Id
    @Column(name = "msgId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long msgId;


    @Column(name = "msgType")
    public Integer msgType;//Wall, Personal

    @Column(name = "msgContent")
    public String msgContent;

    @Column(name = "msgMetadata")
    public String msgMetadata;//Anybody can store relavant metadata here

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(final String msgContent) {
        this.msgContent = msgContent;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(final Long msgId) {
        this.msgId = msgId;
    }

    public String getMsgMetadata() {
        return msgMetadata;
    }

    public void setMsgMetadata(final String msgMetadata) {
        this.msgMetadata = msgMetadata;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(final Integer msgType) {
        this.msgType = msgType;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "Msg{" +
                "msgId=" + msgId +
                ", msgType=" + msgType +
                ", msgContent='" + msgContent + '\'' +
                ", msgMetadata='" + msgMetadata + '\'' +
                '}';
    }

// -------------------------- OTHER METHODS --------------------------

    public Msg setMsgContentR(final String msgContent) {
        this.msgContent = msgContent;
        return this;
    }

    public Msg setMsgIdR(final Long msgId) {
        this.msgId = msgId;
        return this;
    }

    public Msg setMsgMetadataR(final String msgMetadata) {
        this.msgMetadata = msgMetadata;
        return this;
    }

    public Msg setMsgTypeR(final Integer msgType) {
        this.msgType = msgType;
        return this;
    }
}
