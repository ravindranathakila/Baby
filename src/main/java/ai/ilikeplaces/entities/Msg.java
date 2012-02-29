package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.util.EntityLifeCycleListener;

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
@Entity
@NamedQuery(name = "FindWallEntriesByWallIdOrderByIdDesc",
        query = "SELECT msg FROM Msg msg WHERE msg.msgId IN (SELECT wall.wallMsgs FROM Wall wall WHERE wall.wallId = :wallId) order by msg.msgId DESC")
public class Msg implements Serializable{

    public Long msgId;
    public static final String msgIdCOL = "msgId";

    final static public String FindWallEntriesByWallIdOrderByIdDesc = "FindWallEntriesByWallIdOrderByIdDesc";


    public Integer msgType;//Wall, Personal

    public String msgContent;

    public String msgMetadata;//Anybody can store relavant metadata here


    final static public int msgTypeHUMAN = 1;
    final static public int msgTypeMISC = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(final Long msgId) {
        this.msgId = msgId;
    }

    public Msg setMsgIdR(final Long msgId) {
        this.msgId = msgId;
        return this;

    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(final Integer msgType) {
        this.msgType = msgType;
    }

    public Msg setMsgTypeR(final Integer msgType) {
        this.msgType = msgType;
        return this;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(final String msgContent) {
        this.msgContent = msgContent;
    }

    public Msg setMsgContentR(final String msgContent) {
        this.msgContent = msgContent;
        return this;
    }

    public String getMsgMetadata() {
        return msgMetadata;
    }

    public void setMsgMetadata(final String msgMetadata) {
        this.msgMetadata = msgMetadata;
    }

    public Msg setMsgMetadataR(final String msgMetadata) {
        this.msgMetadata = msgMetadata;
        return this;

    }
}