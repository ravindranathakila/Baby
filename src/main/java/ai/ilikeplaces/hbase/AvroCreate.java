package ai.ilikeplaces.hbase;

import ai.ilikeplaces.entities.Subscriber;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 26/1/13
 * Time: 7:53 PM
 */
public class AvroCreate {

    public static final String SUBSCRIBER = "Subscriber";

    public static void writeData(final Subscriber subscriber) throws IOException {


        System.out.println("Attempting to write:" + subscriber.toString());

        final ByteArrayOutputStream _outputStream = new ByteArrayOutputStream();
        final BinaryEncoder _binaryEncoder = EncoderFactory.get().binaryEncoder(_outputStream, null);

        final DatumWriter<Subscriber> _datumWriter = new SpecificDatumWriter<Subscriber>(subscriber.getSchema());
        _datumWriter.write(subscriber, _binaryEncoder);
        _binaryEncoder.flush();

        final byte[] dataByteArray = _outputStream.toByteArray();

        final HBaseAdmin hBaseAdmin = new HBaseAdmin(new Configuration());
        final boolean _subscriber = hBaseAdmin.tableExists(SUBSCRIBER);

        if (_subscriber) {
            hBaseAdmin.disableTable(SUBSCRIBER);
            hBaseAdmin.deleteTable(SUBSCRIBER);
        }

        final HTableDescriptor _desc = new HTableDescriptor(SUBSCRIBER);
        final HColumnDescriptor _family = new HColumnDescriptor("value");

        _desc.addFamily(_family);
        hBaseAdmin.createTable(_desc);

        final HTable _hTable = new HTable(new Configuration(), SUBSCRIBER);
        final Put _put = new Put(Bytes.toBytes(subscriber.getEmailId().toString()));
        _put.add(Bytes.toBytes("value"), Bytes.toBytes("value"), dataByteArray);
        _hTable.put(_put);
    }
}
