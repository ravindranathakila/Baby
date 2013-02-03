package ai.ilikeplaces.hbase;

import ai.ilikeplaces.entities.Subscriber;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

/**
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 26/1/13
 * Time: 7:52 PM
 */
public class Avro {

    public static final String SUBSCRIBER = "Subscriber";
    static Subscriber subscriber;
    static protected byte[] dataByteArray;
    static HBaseAdmin hBaseAdmin;

    static {
        try {
            hBaseAdmin = new HBaseAdmin(new Configuration());
        } catch (final MasterNotRunningException e) {
            throw new RuntimeException(e);
        } catch (final ZooKeeperConnectionException e) {
            throw new RuntimeException(e);
        }
    }
}
