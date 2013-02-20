package ai.hbase;

import ai.ilikeplaces.entities.Subscriber;
import ai.ilikeplaces.util.Loggers;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;

import java.io.*;

/**
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 26/1/13
 * Time: 7:53 PM
 */
public class Put<T extends SpecificRecord> {
// ------------------------------ FIELDS ------------------------------

    public static final String SUBSCRIBER = "Subscriber";

// -------------------------- STATIC METHODS --------------------------

    public static void writeData(final String __rowKey, final Subscriber subscriber) throws IOException {
        System.out.println("Attempting to write:" + subscriber.toString());

        final ByteArrayOutputStream _outputStream = new ByteArrayOutputStream();
        final BinaryEncoder _binaryEncoder = EncoderFactory.get().binaryEncoder(_outputStream, null);

        final DatumWriter<Subscriber> _datumWriter = new SpecificDatumWriter<Subscriber>(subscriber.getSchema());
        _datumWriter.write(subscriber, _binaryEncoder);
        _binaryEncoder.flush();

        final byte[] dataByteArray = _outputStream.toByteArray();


        final org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();

        //curl -H "Content-Type: application/octet-stream" --data '[Sample Data]' http://localhost:9090/Subscriber/samplerow1/value:value

        final PutMethod _putMethod = new PutMethod("http://localhost:9090/" + SUBSCRIBER + "/" + __rowKey + "/value:value");
        _putMethod.setRequestHeader("Content-Type", "application/octet-stream");
        _putMethod.setRequestEntity(new ByteArrayRequestEntity(dataByteArray));

        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(_putMethod);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        if (statusCode != HttpStatus.SC_OK) {
            throw new RuntimeException(String.valueOf(statusCode));
        }
        InputStream inputStream = null;

        try {
            inputStream = _putMethod.getResponseBodyAsStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String accumulator = "";
        try {
            while ((line = br.readLine()) != null) {
                accumulator += line;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Loggers.debug(accumulator);


//
//        final Cluster _cluster = new Cluster();
//        Loggers.constructed("Created HBase Cluster Object:" + _cluster.toString());
//
//        final String _host = "localhost";
//        final int _port = 9090;
//        _cluster.add(_host, _port);
//        Loggers.configured("Added REST info to the Cluster Object. host:port as" + _host + ":" + _port);
//
//
//        final Client _client = new Client(_cluster);
//        Loggers.constructed("Created REST client:" + _client);
//
//        final RemoteHTable _remoteHTable = new RemoteHTable(_client, SUBSCRIBER);
//        Loggers.constructed("Created RemoteHTable:" + _remoteHTable.toString());
//
//        final Put _put = new Put(Bytes.toBytes(subscriber.getEmailId().toString()));
//        Loggers.constructed("Created Put:" + _put.toString());
//        _put.add(Bytes.toBytes("value"), Bytes.toBytes("value"), dataByteArray);
//        Loggers.configured("Configured Put:" + _put.toString());
//        _remoteHTable.put(_put);
//        Loggers.invoked("RemoteHTable put()");


//        final HBaseAdmin hBaseAdmin = new HBaseAdmin(new Configuration(true));
//        final boolean _subscriber = hBaseAdmin.tableExists(SUBSCRIBER);
//
//        if (_subscriber) {
//            hBaseAdmin.disableTable(SUBSCRIBER);
//            hBaseAdmin.deleteTable(SUBSCRIBER);
//        }
//
//        final HTableDescriptor _desc = new HTableDescriptor(SUBSCRIBER);
//        final HColumnDescriptor _family = new HColumnDescriptor("value");
//
//        _desc.addFamily(_family);
//        hBaseAdmin.createTable(_desc);
//
//        final HTable _hTable = new HTable(new Configuration(true), SUBSCRIBER);
//        final Put _put = new Put(Bytes.toBytes(subscriber.getEmailId().toString()));
//        _put.add(Bytes.toBytes("value"), Bytes.toBytes("value"), dataByteArray);
//        _hTable.put(_put);
    }
}

//
//
//package ai.hbase;
//
//import ai.ilikeplaces.entities.Subscriber;
//import org.apache.avro.io.BinaryEncoder;
//import org.apache.avro.io.DatumWriter;
//import org.apache.avro.io.EncoderFactory;
//import org.apache.avro.specific.SpecificDatumWriter;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.HColumnDescriptor;
//import org.apache.hadoop.hbase.HTableDescriptor;
//import org.apache.hadoop.hbase.MasterNotRunningException;
//import org.apache.hadoop.hbase.ZooKeeperConnectionException;
//import org.apache.hadoop.hbase.client.HBaseAdmin;
//import org.apache.hadoop.hbase.client.HTable;
//import org.apache.hadoop.hbase.client.Put;
//import org.apache.hadoop.hbase.util.Bytes;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
///**
// * Created with IntelliJ IDEA Ultimate.
// * User: http://www.ilikeplaces.com
// * Date: 26/1/13
// * Time: 7:53 PM
// */
//public class AvroCreate {
//
//    public static final String SUBSCRIBER = "Subscriber";
//    final static Configuration CONFIGURATION = new Configuration();
//    static HBaseAdmin H_BASE_ADMIN;
//
//    static {
//        try {
//            H_BASE_ADMIN = new HBaseAdmin(CONFIGURATION);
//        } catch (MasterNotRunningException e) {
//            throw new RuntimeException(e);
//        } catch (ZooKeeperConnectionException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void writeData(final String rowKey, final Subscriber subscriber) throws IOException {
//
//
//        System.out.println("Attempting to write:key:" + rowKey);
//        System.out.println("Attempting to write:value:" + subscriber.toString());
//
//        final ByteArrayOutputStream _outputStream = new ByteArrayOutputStream();
//        final BinaryEncoder _binaryEncoder = EncoderFactory.get().binaryEncoder(_outputStream, null);
//
//        final DatumWriter<Subscriber> _datumWriter = new SpecificDatumWriter<Subscriber>(subscriber.getSchema());
//        _datumWriter.write(subscriber, _binaryEncoder);
//        _binaryEncoder.flush();
//
//        final byte[] dataByteArray = _outputStream.toByteArray();
//
//
//
//        if (!H_BASE_ADMIN.tableExists(SUBSCRIBER)) {
//
//            final HTableDescriptor _desc = new HTableDescriptor(SUBSCRIBER);
//            final HColumnDescriptor _family = new HColumnDescriptor("value");
//            _desc.addFamily(_family);
//
//            H_BASE_ADMIN.createTable(_desc);
//        }
//
//        final HTable _hTable = new HTable(CONFIGURATION, SUBSCRIBER);
//
//        final Put _put = new Put(rowKey.getBytes());
//        _put.add(Bytes.toBytes("value"), Bytes.toBytes("value"), dataByteArray);
//        _hTable.put(_put);
//    }
//}

